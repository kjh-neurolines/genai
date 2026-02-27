package net.neurolines.genai.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.neurolines.genai.model.AuthUser;
import net.neurolines.genai.model.RequestDTO;
import net.neurolines.genai.model.ResultDTO;
import net.neurolines.genai.model.genai.IvInventory;
import net.neurolines.genai.service.AuthService;
import net.neurolines.genai.service.InventoryService;
import net.neurolines.genai.service.ParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/iv")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @Autowired
    AuthService authService;

    @Autowired
    ParsingService parsingService;

    @Transactional
    @PostMapping("/upload")
    public ResponseEntity<Object> save(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestPart(value = "file", required = true) MultipartFile[] file,
            @RequestPart(value = "requestDTO") RequestDTO requestDTO
    ) {

        List<AuthUser> userInfo = authService.user(request, response);
        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            requestDTO.setEmail(userInfo.get(0).getEmail());
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        ResultDTO resultDTO = new ResultDTO();

        Map<String, Object> result = new HashMap<>();
        result.put("status", 99);

        IvInventory inventory = inventoryService.saveDefaultInfo(requestDTO);

        if(inventory.getStatus() != 99) {
            String fileUrl = inventoryService.uploadFile(inventory, file, requestDTO);

            Map<String, Object> parsingData = parsingService.parsingKoMsds(fileUrl);
            parsingData.put("iviRegno", inventory.getIviRegno());

            if("1".equals(parsingData.get("status").toString())) {

                return ResponseEntity.ok(inventoryService.insertParsingInfo(parsingData));

            }else{
                return ResponseEntity.ok(parsingData);
            }
        }

        return ResponseEntity.ok(resultDTO);
    }

}
