package net.neurolines.genai.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.neurolines.genai.dao.genai.InventoryDao;
import net.neurolines.genai.model.RequestDTO;
import net.neurolines.genai.model.genai.*;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryService {

    @Value("${pdf.upload-path}")
    String filePath;

    @Autowired
    InventoryDao inventoryDao;

    /**
     * 1차 MSDS 파일 기본 정보 저장
     * @param requestData
     * @return
     */
    public IvInventory saveDefaultInfo(RequestDTO requestDTO)
    {

        IvInventory inventory = IvInventory.builder()
                .gubun(!"".equals(requestDTO.getGubun()) ? Integer.parseInt(requestDTO.getGubun()) : 0)
                .productCode(requestDTO.getProductCode())
                .productId(requestDTO.getProductId())
                .vendorCode(requestDTO.getVendorCode())
                .status(0)
                .statusHistoryYn("N")
                .registrationName(requestDTO.getEmail())
                .build();

       /* if((inventory.getProductCode() != null && !"".equals(inventory.getProductCode()))) {
            inventory.setStatus(99);
            inventory.setErrorMsg("존재하지 않는 자재코드입니다.");
        }*/

        inventoryDao.insertDefaultInfo(inventory);

        return inventory;
    }

    /**
     * MSDS 파일 서버 저장
     * @param inventory
     * @param files
     * @return
     */
    public String uploadFile(IvInventory inventory, MultipartFile[] files, RequestDTO requestDTO)
    {

        String fileUrl = "";

        for(int i = 0; i < files.length; i++)
        {
            String orgFilename = files[i].getOriginalFilename();
            if (orgFilename == null || orgFilename.isEmpty()) {
                throw new IllegalArgumentException("Invalid file name");
            }
            int index = i+1;

            String randomFilename = "MSDS_" + inventory.getIviRegno() + "_" + index + getFileExtension(orgFilename);;
            String path = filePath + randomFilename;

            try {
                files[i].transferTo(new File(path));
                IvInventoryFile file = IvInventoryFile
                        .builder()
                        .fileName("/" + randomFilename)
                        .orgFilename(orgFilename)
                        .iviRegno(inventory.getIviRegno()) // AI 파싱하는 파일은 무조건 1번째로 지정
                        .ivifRegno(index)
                        .langCode(requestDTO.getLangCode())
                        .country(requestDTO.getCountry())
                        .build();

                if(i == 0)
                    fileUrl = path;

                inventoryDao.insertInventoryFileInfo(file);

            } catch (IOException | IllegalStateException e) {

                inventory.setStatus(99);
                inventory.setErrorMsg("파일 저장시에 오류가 발생했습니다.");

                inventoryDao.updateInventory(inventory);
            }
        }

        return fileUrl;
    }

    public IvInventory insertParsingInfo(Map<String, Object> data)
    {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        IvInventory inventory = objectMapper.convertValue(data, IvInventory.class);
        List<IvInventoryClassResult> toxcities = new ArrayList<>();

        try {

            //원본 저장
            String orgStr = data.get("orgData").toString();
            IvInventoryOrgData orgData = IvInventoryOrgData.builder()
                            .orgData(orgStr)
                                    .iviRegno(inventory.getIviRegno()).build();

            inventoryDao.insertInventoryOrgData(orgData);

            data.put("status", 1);

            // 유해성 위험성 정보
            List<String> ghsCodes = (List<String>) data.get("classResultCode");

            if (ghsCodes != null && ghsCodes.size() > 0) {
                for (String code : ghsCodes) {
                    toxcities.add(IvInventoryClassResult.builder()
                            .classResultCode(code)
                            .iviRegno(Integer.parseInt(String.valueOf(data.get("iviRegno"))))
                            .build());
                }

                inventoryDao.insertInventoryClassResult(toxcities);
            }

            // 물질 정보
            List<IvInventoryChemical> chemicalList = (List<IvInventoryChemical>) data.get("chemicalList");

            if (chemicalList != null && chemicalList.size() > 0)
                inventoryDao.insertInventoryChemical(chemicalList);

            // 그림 문자

            List<String> parsePictureList = (List<String>) data.get("parsePictureList");
            if(parsePictureList != null && parsePictureList.size() > 0)
            {

                List<IvInventoryPicture> pList = new ArrayList<>();

                for(String str : parsePictureList){
                    IvInventoryPicture picture = new IvInventoryPicture();

                    picture.setIviRegno(inventory.getIviRegno());
                    picture.setPicture(str);

                    pList.add(picture);
                }

                inventoryDao.insertInventoryPictureInfo(pList);
            }

            //HCode
            List<IvInventoryHcode> hCodeList = (List<IvInventoryHcode>) data.get("hCode");

            if(hCodeList != null && hCodeList.size() > 0)
            {
                inventoryDao.insertInventoryHcode(hCodeList);
            }

            // PCode
            List<IvInventoryPcode> pCodeList = (List<IvInventoryPcode>) data.get("pCode");

            if(pCodeList != null && pCodeList.size() > 0)
            {
                inventoryDao.insertInventoryPcode(pCodeList);
            }

            IvInventoryTransport transport = (IvInventoryTransport) data.get("transport");

            if(transport != null)
                inventoryDao.insertInventoryTransport(transport);

            //마지막에 최종정보 UPDATE하면서 등록 완료 상태로 변경
            inventoryDao.updateInventory(inventory);
        } /*catch (Exception e) {

            System.out.print(e.toString());

            inventory.setStatus(99);
            inventory.setErrorMsg("데이터 저장 중 오류가 생겼습니다.");
            inventory.setIviRegno((Integer) data.get("iviRegno"));

            inventoryDao.updateInventory(inventory);
        }*/ catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        return inventory;
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex);
        }
        return ""; // 확장자가 없을 경우 빈 문자열 반환
    }

}
