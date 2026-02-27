package net.neurolines.genai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.neurolines.genai.model.genai.IvInventoryChemical;
import net.neurolines.genai.model.genai.IvInventoryHcode;
import net.neurolines.genai.model.genai.IvInventoryPcode;
import net.neurolines.genai.model.genai.IvInventoryTransport;
import net.neurolines.genai.util.Section2Enum;
import net.neurolines.genai.util.Synonym15UtilClass;
import net.neurolines.genai.util.Synonym2UtilClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParsingService {

    String cleanText = "^\\[|\\]$";

    private static final Pattern CAS_PATTERN = Pattern.compile("\\b\\d{2,7}-\\d{2}-\\d\\b");
    private static final Pattern KE_PATTERN  = Pattern.compile("\\bKE-\\d+\\b");

    @Autowired
    @Qualifier("dopeApiRestTemplate")
    private RestTemplate restTemplate;

    final String uploadSyncLink = "/documents/analyze-sync";

    public Map<String, Object> parsingKoMsds(String filePath)
    {

        Map<String, Object> sectionData = new HashMap<>();
        List<IvInventoryChemical> chemicalList = new ArrayList<>();

        try {

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.MULTIPART_FORM_DATA); //API 호출 시 파일 전송을 위해 설정

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            body.add("file", new FileSystemResource(filePath)); // 파라미터에 업로드 된 파일 추가

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            String url = uploadSyncLink + "?document_type=msds_ko&timeout=1000&poll_interval=2";

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});

            Map<String, Object> parseResult = (Map<String, Object>) response.getBody().get("result");
            String productName = "";

            /**
             * 제품명
             */
            if(parseResult.containsKey("01010100")) {
                Map<String, Object> productData = (Map<String, Object>) parseResult.get("01010100");

                String result = productData.get("RESULT").toString().replaceAll(cleanText, "");

                productName = result;

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("productName", result);
            }
            else
                sectionData.put("productName", "");

            /**
             * MSDS 번호
             */
            if (parseResult.containsKey("1605000")) {
                Map<String, Object> section16Data = (Map<String, Object>) parseResult.get("1605000");

                String result = section16Data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 45)
                    result = result.substring(0, 45);

                sectionData.put("msdsNo", result);
            } else
                sectionData.put("msdsNo", "");

            /**
             * 회사명
             */
            if(parseResult.containsKey("01030101")) {
                Map<String, Object> companyData = (Map<String, Object>) parseResult.get("01030101");

                String result = companyData.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 200)
                    result = result.substring(0, 200);

                sectionData.put("companyName",result);
            }
            else
                sectionData.put("companyName", "");

            /**
             * 전화번호
             */
            if(parseResult.containsKey("01030103"))
            {
                Map<String, Object> telData = (Map<String, Object>) parseResult.get("01030103");

                String result = telData.get("RESULT").toString().replaceAll(cleanText, "");

                if(!"".equals(result)) {
                    if(result.length() > 100)
                        result = result.substring(0, 100);

                    sectionData.put("tel", result);
                }
            }
            if(parseResult.containsKey("01030105"))
            {
                Map<String, Object> telData = (Map<String, Object>) parseResult.get("01030105");

                String result = telData.get("RESULT").toString().replaceAll(cleanText, "");

                if(!"".equals(result)) {
                    if(result.length() > 100)
                        result = result.substring(0, 100);

                    sectionData.put("tel", result);
                }
            }
            if(parseResult.containsKey("01030203"))
            {
                Map<String, Object> telData = (Map<String, Object>) parseResult.get("01030203");

                String result = telData.get("RESULT").toString().replaceAll(cleanText, "");
                if(!"".equals(result)) {
                    if(result.length() > 100)
                        result = result.substring(0, 100);

                    sectionData.put("tel", result);
                }
            }
            if(parseResult.containsKey("01030205"))
            {
                Map<String, Object> telData = (Map<String, Object>) parseResult.get("01030205");

                String result = telData.get("RESULT").toString().replaceAll(cleanText, "");

                if(!"".equals(result)) {
                    if(result.length() > 100)
                        result = result.substring(0, 100);

                    sectionData.put("tel", result);
                }
            }

            if("".equals(sectionData.get("tel")) || sectionData.get("tel") == null)
                sectionData.put("tel", "");

            /**
             * 신호어
             */
            if(parseResult.containsKey("02020200")){
                Map<String, Object> wordData = (Map<String, Object>) parseResult.get("02020200");

                String result = wordData.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 45)
                    result = result.substring(0, 45);

                sectionData.put("word", result);
            }else{
                sectionData.put("word", "");
            }

            /**
             * 성상(물리적 상태)
             */
            if(parseResult.containsKey("09010100")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09010100");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 200)
                    result = result.substring(0, 200);

                sectionData.put("pscMatter", result);
            }

            /**
             * 색상
             */
            if(parseResult.containsKey("09010200")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09010200");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 200)
                    result = result.substring(0, 200);

                sectionData.put("pscColor", result);
            }

            /**
             * 녹는점/어는점
             */
            if(parseResult.containsKey("09050000")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09050000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 200)
                    result = result.substring(0, 200);

                sectionData.put("pscMeltingPoint", result);
            }

            /**
             * 끓는 점/끓는 점 범위
             */
            if(parseResult.containsKey("09060000")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09060000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("pscBoilingPoint", result);
            }else{
                sectionData.put("pscBoilingPoint", "");
            }

            /**
             * 인화점
             */
            if(parseResult.containsKey("09070000")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09070000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("physicalFlashPoint", result);
            }else{
                sectionData.put("physicalFlashPoint", "");
            }

            /**
             * 인화성(고체, 기체)
             */
            if(parseResult.containsKey("09090000")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09090000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("physicalFlammable", result);
            }else{
                sectionData.put("physicalFlammable", "");
            }

            /**
             * 09100000 폭발 범위 상한/하한
             * physical_explosion_max
             */
            if(parseResult.containsKey("09100000")) {
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09100000");

                List<String> result = (List<String>) data.get("RESULT");

                if (result.size() == 0) {
                    sectionData.put("pscExplosionMax", "");
                    sectionData.put("pscExplosionMin", "");
                } else if (result.size() == 1) {

                    if(result.get(0).indexOf("/") > -1)
                    {
                        String max = result.get(0).split("/")[0];
                        String min = result.get(0).split("/")[1];

                        sectionData.put("pscExplosionMax", max);
                        sectionData.put("pscExplosionMin", min);
                    }else {
                        sectionData.put("pscExplosionMax", result.get(0));
                        sectionData.put("pscExplosionMin", result.get(0));
                    }
                } else {
                    sectionData.put("pscExplosionMax", result.get(0));
                    sectionData.put("pscExplosionMin", result.get(1));
                }
            }else{
                sectionData.put("pscExplosionMax", "");
                sectionData.put("pscExplosionMin", "");
            }

            /**
             * 09110000 증기압
             * physical_apor_pressure
             */
            if(parseResult.containsKey("09110000")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09110000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("pscAporPressure", result);
            }else{
                sectionData.put("pscAporPressure", "");
            }

            /**
             * 용해도
             */
            if(parseResult.containsKey("09120000")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09120000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("pscSolubility", result);
            }else{
                sectionData.put("pscSolubility", "");
            }

            /**
             * 증기밀도
             */
            if(parseResult.containsKey("09130000")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09130000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("pscVapourDensity", result);
            }else{
                sectionData.put("pscVapourDensity", "");
            }

            /**
             *비중(상대밀도)
             */
            if(parseResult.containsKey("09140000")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09140000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("pscRelativeDensity", result);
            }else{
                sectionData.put("pscRelativeDensity", "");
            }

            /**
             * n-옥탄올/물분배계수
             */
            if(parseResult.containsKey("09150000")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09150000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("pscPartitionCoefficient", result);
            }else{
                sectionData.put("pscPartitionCoefficient", "");
            }

            /**
             * 자연발화온도
             */
            if (parseResult.containsKey("09160000")) {
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09160000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if (result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("pscAutoTemperature", result);
            } else {
                sectionData.put("pscAutoTemperature", "");
            }

            /**
             * 분해온도
             */
            if(parseResult.containsKey("09170000")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09170000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("pscDecompositionTemperature", result);
            }else{
                sectionData.put("pscDecompositionTemperature", "");
            }

            /**
             * 점도
             */
            if(parseResult.containsKey("09180000")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09180000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("pscViscosity", result);
            }else{
                sectionData.put("pscViscosity", "");
            }

            /**
             * 점도
             */
            if(parseResult.containsKey("09180000")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09180000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("pscViscosity", result);
            }else{
                sectionData.put("pscViscosity", "");
            }

            /**
             * 분자량
             */
            if(parseResult.containsKey("09190000")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("09190000");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("pscMolecularWeight", result);
            }else{
                sectionData.put("pscMolecularWeight", "");
            }

            /**
             * 버전
             */
            if(parseResult.containsKey("16030300")){
                Map<String, Object> data = (Map<String, Object>) parseResult.get("16030300");

                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 100)
                    result = result.substring(0, 100);

                sectionData.put("version", result);
            }else{
                sectionData.put("version", "");
            }

            /**
             * 최초작성일자
             */
            if (parseResult.containsKey("16020000")) {
                Map<String, Object> section16Data = (Map<String, Object>) parseResult.get("16020000");

                String result = section16Data.get("RESULT").toString();

                if(result != null && !"".equals(result))
                    sectionData.put("firstWriteDate", findDate(result));
            }

            /**
             * 개정일자
             */
            if (parseResult.containsKey("16030200")) {
                Map<String, Object> section16Data = (Map<String, Object>) parseResult.get("16030200");

                String result = section16Data.get("RESULT").toString();

                if(result != null && !"".equals(result))
                    sectionData.put("revisionDate", findDate(result));
            }

            /**
             * 유해성 분류 코드
             */
            if(parseResult.containsKey("02010000")) {
                Map<String, Object> ghsDatas = (Map<String, Object>) parseResult.get("02010000");
                Object result = ghsDatas.get("RESULT");
                List<String> ghsCodes = new ArrayList<>();
                List<String> ghsCodeName = new ArrayList<>();

                if(result instanceof String)
                    ghsCodes = Synonym2UtilClass.stringParsingGhsCode((String) result);
                else if (result instanceof List){
                    List<?> resultList = (List<?>) result;
                    ghsCodes = Synonym2UtilClass.listParsingGhsCode((List<String>) resultList);
                }
                sectionData.put("classResultCode", ghsCodes);

                for(String code : ghsCodes)
                {
                    ghsCodeName.add(Section2Enum.valueOf(code).getName());
                }

                sectionData.put("classResultName", ghsCodeName);
            }
            else {
                sectionData.put("classResultCode",new ArrayList<>());
                sectionData.put("classResultName", new ArrayList<>());
            }

            Map<String, Object> section03Sets = (Map<String, Object>) parseResult.get("03000000");
            List<Map<String, Object>> chemicals = (List<Map<String, Object>>) section03Sets.get("SETS");

            if(chemicals != null)
            {
                for (int i = 0; i < chemicals.size(); i++) {
                    IvInventoryChemical chemical = new IvInventoryChemical();

                    /**
                     * 물질명
                     */
                    if (chemicals.get(i).containsKey("03010000")) {
                        Map<String, Object> data = (Map<String, Object>) chemicals.get(i).get("03010000");

                        String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 1000)
                            result = result.substring(0, 1000);

                        chemical.setChmicalName(result);
                    }

                    /**
                     * Cas No
                     */
                    if (chemicals.get(i).containsKey("03030000")) {

                        String casNo;
                        String keNo;


                        Map<String, Object> data = (Map<String, Object>) chemicals.get(i).get("03030000");

                        String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                        Matcher casMatcher = CAS_PATTERN.matcher(result);
                        Matcher keMatcher  = KE_PATTERN.matcher(result);

                         casNo = casMatcher.find() ? casMatcher.group() : null;
                         keNo = keMatcher.find() ? keMatcher.group() : null;

                        chemical.setCasNo(casNo == null ? "" : casNo);
                        chemical.setRegNo(keNo == null ? "" : keNo);
                    }

                    /**
                     * 함유량
                     */
                    if (chemicals.get(i).containsKey("03040000")) {
                        Map<String, Object> data = (Map<String, Object>) chemicals.get(i).get("03040000");

                        String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 45)
                            result = result.substring(0, 45);

                        chemical.setPercent(result);
                    }

                    chemicalList.add(chemical);

                }
            }

            /**
             * 국내 노출 기준
             */
            if (parseResult.containsKey("08010000")) {
                Map<String, Object> section8Data = (Map<String, Object>) parseResult.get("08010000");
                List<Map<String, Object>> section08Sets = (List<Map<String, Object>>) section8Data.get("SETS");

                for(int i = 0; i < section08Sets.size(); i++)
                {
                    Map<String, Object> data = section08Sets.get(i);

                    chemicalList.forEach(chem ->
                    {
                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            Map<String, Object> findData = (Map<String, Object>) data.get("08010100");

                            String resultStr = findData.get("RESULT").toString().replaceAll(cleanText, "");

                            if (resultStr.length() > 100)
                                resultStr = resultStr.substring(0, 100);

                            chem.setTwa(resultStr);
                        }
                    });
                }
            }

            if (parseResult.containsKey("11020100")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("11020100");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList){
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            if (data.containsKey("11020101")) {
                                Map<String, Object> findData = (Map<String, Object>) data.get("11020101");
                                String result = findData.get("RESULT").toString().replaceAll(cleanText, "");

                                if (result.length() > 200)
                                    result = result.substring(0, 200);

                                chem.setOral(result);
                            }

                            if (data.containsKey("11020102")) {
                                Map<String, Object> findData = (Map<String, Object>) data.get("11020102");
                                String result = findData.get("RESULT").toString().replaceAll(cleanText, "");

                                if (result.length() > 200)
                                    result = result.substring(0, 200);

                                chem.setDermal(result);
                            }

                            if (data.containsKey("11020103")) {
                                Map<String, Object> findData = (Map<String, Object>) data.get("11020103");
                                String result = findData.get("RESULT").toString().replaceAll(cleanText, "");

                                if (result.length() > 200)
                                    result = result.substring(0, 200);

                                chem.setInhalation(result);
                            }

                            if (data.containsKey("11020103.01")) {
                                Map<String, Object> findData = (Map<String, Object>) data.get("11020103.01");
                                String result = findData.get("RESULT").toString().replaceAll(cleanText, "");

                                if (result.length() > 200)
                                    result = result.substring(0, 200);

                                chem.setInhalationGas(result);
                            }

                            if (data.containsKey("11020103.02")) {
                                Map<String, Object> findData = (Map<String, Object>) data.get("11020103.02");
                                String result = findData.get("RESULT").toString().replaceAll(cleanText, "");

                                if (result.length() > 200)
                                    result = result.substring(0, 200);

                                chem.setInhalationSteam(result);
                            }

                            if (data.containsKey("11020103.03")) {
                                Map<String, Object> findData = (Map<String, Object>) data.get("11020103.03");
                                String result = findData.get("RESULT").toString().replaceAll(cleanText, "");

                                if (result.length() > 200)
                                    result = result.substring(0, 200);

                                chem.setInhalationMist(result);
                            }

                            chemFlag = true;
                        }
                    }
                    if(defaultData != null && !chemFlag)
                    {
                        if (defaultData.containsKey("11020101")) {
                            Map<String, Object> findData = (Map<String, Object>) defaultData.get("11020101");
                            String result = findData.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setOral(result);
                        }

                        if (defaultData.containsKey("11020102")) {
                            Map<String, Object> findData = (Map<String, Object>) defaultData.get("11020102");
                            String result = findData.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setDermal(result);
                        }

                        if (defaultData.containsKey("11020103")) {
                            Map<String, Object> findData = (Map<String, Object>) defaultData.get("11020103");
                            String result = findData.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setInhalation(result);
                        }
                    }
                };
            }

            if (parseResult.containsKey("11020200")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("11020200");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setSkinCorrosion(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setSkinCorrosion(result);
                    }
                }
            }

            if (parseResult.containsKey("11020300")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("11020300");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setEyeDamage(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setEyeDamage(result);
                    }
                }
            }

            if (parseResult.containsKey("11020400")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("11020400");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setRespiratoryIrritability(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setRespiratoryIrritability(result);
                    }
                }
            }

            if (parseResult.containsKey("11020500")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("11020500");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setSkinSensitivity(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setSkinSensitivity(result);
                    }
                }
            }

            // TODO 발암성 확인 필요
            if (parseResult.containsKey("11020600")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("11020600");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setMutagenicity(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setMutagenicity(result);
                    }
                }
            }

            if (parseResult.containsKey("11020700")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("11020700");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setCarcinogenicity(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setCarcinogenicity(result);
                    }
                }
            }

            if (parseResult.containsKey("11020800")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("11020800");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setReproductiveToxicity(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setReproductiveToxicity(result);
                    }
                }
            }

            if (parseResult.containsKey("11020900")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("11020900");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setTargetOrganToxicitySingle(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setTargetOrganToxicitySingle(result);
                    }
                }
            }

            if (parseResult.containsKey("11021000")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("11021000");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setTargetOrganToxicityRepeat(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setTargetOrganToxicityRepeat(result);
                    }
                }
            }

            if (parseResult.containsKey("11021100")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("11021100");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setAspirationHazard(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setAspirationHazard(result);
                    }
                }
            }


            // TODO 급성수생환경유해성 / 만성수생환경유해성 변경 필요
            if (parseResult.containsKey("12010201")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("12010201");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            List<String> resultList = (List<String>) data.get("RESULT");

                            if(resultList.size() > 1) {

                                for (String str : resultList) {
                                    if (str.contains("급성")) {

                                        if (str.length() > 200)
                                            str = str.substring(0, 200);

                                        chem.setEcotoxicityFish(str);
                                    } else if (str.contains("만성")) {

                                        if (str.length() > 200)
                                            str = str.substring(0, 200);

                                        chem.setChronicFish(str);
                                    }

                                }
                            }else {

                                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                                if (result.length() > 200)
                                    result = result.substring(0, 200);

                                chem.setEcotoxicityFish(result);
                            }
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setEcotoxicityFish(result);
                    }
                }
            }

            if (parseResult.containsKey("12010202")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("12010202");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;


                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            List<String> resultList = (List<String>) data.get("RESULT");

                            if(resultList.size() > 1) {

                                for (String str : resultList) {
                                    if (str.contains("급성")) {

                                        if (str.length() > 200)
                                            str = str.substring(0, 200);

                                        chem.setEcotoxicityInvert(str);
                                    } else if (str.contains("만성")) {

                                        if (str.length() > 200)
                                            str = str.substring(0, 200);

                                        chem.setChronicInvert(str);
                                    }

                                }
                            }else {

                                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                                if (result.length() > 200)
                                    result = result.substring(0, 200);

                                chem.setEcotoxicityInvert(result);
                            }
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setEcotoxicityInvert(result);
                    }
                }
            }

            if (parseResult.containsKey("12010203")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("12010203");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            List<String> resultList = (List<String>) data.get("RESULT");

                            if(resultList.size() > 1) {

                                for (String str : resultList) {
                                    if (str.contains("급성")) {

                                        if (str.length() > 200)
                                            str = str.substring(0, 200);

                                        chem.setEcotoxicityAlgae(str);
                                    } else if (str.contains("만성")) {

                                        if (str.length() > 200)
                                            str = str.substring(0, 200);

                                        chem.setChronicAlgae(str);
                                    }

                                }
                            }else {

                                String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                                if (result.length() > 200)
                                    result = result.substring(0, 200);

                                chem.setEcotoxicityAlgae(result);
                            }
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setEcotoxicityAlgae(result);
                    }
                }
            }

            if (parseResult.containsKey("12020100")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("12020100");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setPersistence(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setPersistence(result);
                    }
                }
            }

            if (parseResult.containsKey("12020200")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("12020200");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setDegradability(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setDegradability(result);
                    }
                }
            }

            if (parseResult.containsKey("12030200")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("12030200");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setBioPotential(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setBioPotential(result);
                    }
                }
            }

            if (parseResult.containsKey("12040000")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("12040000");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setMobilityInSoil(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setMobilityInSoil(result);
                    }
                }
            }

            if (parseResult.containsKey("12070000")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("12070000");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setHazardousOzne(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setHazardousOzne(result);
                    }
                }
            }


            if (parseResult.containsKey("12080000")) {
                Map<String, Object> section11Data = (Map<String, Object>) parseResult.get("12080000");
                List<Map<String, Object>> section11Sets = (List<Map<String, Object>>) section11Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section11Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section11Sets.size(); i++) {
                        Map<String, Object> data = section11Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String result = data.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setOtherEffect(result);
                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setOtherEffect(result);
                    }
                }
            }

            if (parseResult.containsKey("08010000")) {
                Map<String, Object> section08Data = (Map<String, Object>) parseResult.get("08010000");
                List<Map<String, Object>> section08Sets = (List<Map<String, Object>>) section08Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section08Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;
                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section08Sets.size(); i++) {
                        Map<String, Object> data = section08Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            Map<String, Object> regData = (Map<String, Object>) data.get("08010100");

                            String result = regData.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setNationalExrposure(result);

                            regData = (Map<String, Object>) data.get("08010200");

                            result = regData.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setAcgihReg(result);

                            regData = (Map<String, Object>) data.get("08010300");

                            result = regData.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setOshaReg(result);

                            regData = (Map<String, Object>) data.get("08010600");

                            result = regData.get("RESULT").toString().replaceAll(cleanText, "");

                            if (result.length() > 200)
                                result = result.substring(0, 200);

                            chem.setBioReg(result);


                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String result = defaultData.get("RESULT").toString().replaceAll(cleanText, "");

                        if (result.length() > 200)
                            result = result.substring(0, 200);

                        chem.setOtherEffect(result);
                    }
                }
            }

            if (parseResult.containsKey("15000000")) {
                Map<String, Object> section15Data = (Map<String, Object>) parseResult.get("15000000");
                List<Map<String, Object>> section15Sets = (List<Map<String, Object>>) section15Data.get("SETS");

                Map<String, Object> defaultData = null;
                for (Map<String, Object> data : section15Sets) {
                    if (("".equals(data.get("NAME").toString())) || productName.contains(data.get("NAME").toString()) || data.get("NAME").toString().contains(productName))
                        defaultData = data;

                }

                for(IvInventoryChemical chem : chemicalList) {
                    boolean chemFlag = false;

                    for (int i = 0; i < section15Sets.size(); i++) {
                        Map<String, Object> data = section15Sets.get(i);

                        if (chem.getChmicalName().toUpperCase().contains(data.get("NAME").toString().toUpperCase())
                                || data.get("NAME").toString().toUpperCase().contains(chem.getChmicalName().toUpperCase())) {

                            String parseStr = "";

                            if (data.containsKey("15010000")) {
                                Map<String, Object> findData = (Map<String, Object>) data.get("15010000");
                                List<String> result = (List<String>) findData.get("RESULT");

                                parseStr += "," + result.stream()
                                        .reduce((a, b) -> a + "," + b)      // 문자열 합치기
                                        .orElse("");
                            }

                            if (data.containsKey("15020000")) {
                                Map<String, Object> findData = (Map<String, Object>) data.get("15020000");
                                List<String> result = (List<String>) findData.get("RESULT");

                                parseStr += "," + result.stream()
                                        .reduce((a, b) -> a + "," + b)      // 문자열 합치기
                                        .orElse("");
                            }

                            String regul15Str = "";
                            /**
                             * 위험물안전관리법에 의한 규제
                             */
                            if (data.containsKey("15030000")) {
                                Map<String, Object> parseData = (Map<String, Object>) data.get("15030000");

                                String result = parseData.get("RESULT").toString();
                                regul15Str = result;
                            }

                            String regul16Str = "";
                            /**
                             * 폐기물관리법에 의한 규제
                             */
                            if (data.containsKey("15040000")) {
                                Map<String, Object> parseData = (Map<String, Object>) data.get("15040000");

                                String result = parseData.get("RESULT").toString();

                                regul16Str = result;
                            }

                            Map<String, String> regulInfo = Synonym15UtilClass.checkWordAvail(parseStr);

                            chem.setRegul1(regulInfo.get("제조등금지물질"));
                            chem.setRegul2(regulInfo.get("제조허가대상물질"));
                            chem.setRegul3(regulInfo.get("노출기준설정물질"));
                            chem.setRegul4(regulInfo.get("작업자노출기준"));
                            chem.setRegul5(regulInfo.get("관리대상유해물질"));
                            chem.setRegul6(regulInfo.get("작업환경측정대상물질"));
                            chem.setRegul7(regulInfo.get("특수건강진단대상유해물질"));
                            chem.setRegul8(regulInfo.get("특별관리물질"));
                            chem.setRegul9(regulInfo.get("허용기준설정대상유해인자"));
                            chem.setRegul10(regulInfo.get("공정안정관리대상유해인자"));

                            chem.setRegul11(regulInfo.get("영업비밀인정제외물질")); //없음
                            chem.setRegul12(regulInfo.get("국소배기장치안전검사대상물질")); // 국소배기장치 안전검사 대상물질 없음

                            chem.setRegul13(regulInfo.get("기존화학물질"));
                            chem.setRegul14(regulInfo.get("유독물질"));
                            chem.setRegul14_1(regulInfo.get("인체급성유해성물질"));
                            chem.setRegul14_2(regulInfo.get("인체만성유해성물질"));
                            chem.setRegul14_3(regulInfo.get("생태유해성물질"));

                            chem.setRegul15(regulInfo.get("허가물질"));
                            chem.setRegul16(regulInfo.get("제한물질"));
                            chem.setRegul17(regulInfo.get("금지물질"));

                            chem.setRegul18(regulInfo.get("사고대비물질"));
                            chem.setRegul19(regulInfo.get("배출량조사대상화학물질"));
                            chem.setRegul20(regulInfo.get("등록대상기존화학물질"));
                            chem.setRegul21(regulInfo.get("암,돌연변이성물질"));
                            chem.setRegul22(regulInfo.get("유해성미확인물질"));
                            chem.setRegul23(regulInfo.get("중점관리물질"));
                            chem.setRegul24(Synonym15UtilClass.checkRegul15(regul15Str));
                            if("Y".equals(chem.getRegul24()))
                            {
                                if (regul15Str.contains("1류"))
                                    chem.setRegul24_1("Y");
                                else if(regul15Str.contains("2류"))
                                    chem.setRegul24_2("Y");
                                else if(regul15Str.contains("3류"))
                                    chem.setRegul24_3("Y");
                                else if(regul15Str.contains("4류"))
                                    chem.setRegul24_4("Y");
                                else if(regul15Str.contains("5류"))
                                    chem.setRegul24_5("Y");
                                else if(regul15Str.contains("6류"))
                                    chem.setRegul24_6("Y");

                            }

                            chem.setRegul25(regulInfo.get("특수고압가스"));
                            chem.setRegul26(regulInfo.get("가연성가스"));
                            chem.setRegul27(regulInfo.get("특정고압가스"));
                            chem.setRegul28(regulInfo.get("독성가스"));
                            chem.setRegul29(regulInfo.get("폐유기용제"));
                            chem.setRegul30(Synonym15UtilClass.checkRegul16(regul16Str));


                        }
                    }

                    if(defaultData != null && !chemFlag)
                    {
                        String parseStr = "";

                        if (defaultData.containsKey("15010000")) {
                            Map<String, Object> findData = (Map<String, Object>) defaultData.get("15010000");
                            List<String> result = (List<String>) findData.get("RESULT");

                            parseStr += "," + result.stream()
                                    .reduce((a, b) -> a + "," + b)      // 문자열 합치기
                                    .orElse("");
                        }

                        if (defaultData.containsKey("15020000")) {
                            Map<String, Object> findData = (Map<String, Object>) defaultData.get("15020000");
                            List<String> result = (List<String>) findData.get("RESULT");

                            parseStr += "," + result.stream()
                                    .reduce((a, b) -> a + "," + b)      // 문자열 합치기
                                    .orElse("");
                        }

                        String regul15Str = "";
                        /**
                         * 위험물안전관리법에 의한 규제
                         */
                        if (defaultData.containsKey("15030000")) {
                            Map<String, Object> parseData = (Map<String, Object>) defaultData.get("15030000");

                            String result = parseData.get("RESULT").toString();
                            regul15Str = result;
                        }

                        String regul16Str = "";
                        /**
                         * 폐기물관리법에 의한 규제
                         */
                        if (defaultData.containsKey("15040000")) {
                            Map<String, Object> parseData = (Map<String, Object>) defaultData.get("15040000");

                            String result = parseData.get("RESULT").toString();

                            regul16Str = result;
                        }

                        Map<String, String> regulInfo = Synonym15UtilClass.checkWordAvail(parseStr);

                        chem.setRegul1(regulInfo.get("제조등금지물질"));
                        chem.setRegul2(regulInfo.get("제조허가대상물질"));
                        chem.setRegul3(regulInfo.get("노출기준설정물질"));
                        chem.setRegul4(regulInfo.get("작업자노출기준"));
                        chem.setRegul5(regulInfo.get("관리대상유해물질"));
                        chem.setRegul6(regulInfo.get("작업환경측정대상물질"));
                        chem.setRegul7(regulInfo.get("특수건강진단대상유해물질"));
                        chem.setRegul8(regulInfo.get("특별관리물질"));
                        chem.setRegul9(regulInfo.get("허용기준설정대상유해인자"));
                        chem.setRegul10(regulInfo.get("공정안정관리대상유해인자"));

                        chem.setRegul11(regulInfo.get("영업비밀인정제외물질")); //없음
                        chem.setRegul12(regulInfo.get("국소배기장치안전검사대상물질")); // 국소배기장치 안전검사 대상물질 없음

                        chem.setRegul13(regulInfo.get("기존화학물질"));
                        chem.setRegul14(regulInfo.get("유독물질"));
                        chem.setRegul14_1(regulInfo.get("인체급성유해성물질"));
                        chem.setRegul14_2(regulInfo.get("인체만성유해성물질"));
                        chem.setRegul14_3(regulInfo.get("생태유해성물질"));

                        chem.setRegul15(regulInfo.get("허가물질"));
                        chem.setRegul16(regulInfo.get("제한물질"));
                        chem.setRegul17(regulInfo.get("금지물질"));

                        chem.setRegul18(regulInfo.get("사고대비물질"));
                        chem.setRegul19(regulInfo.get("배출량조사대상화학물질"));
                        chem.setRegul20(regulInfo.get("등록대상기존화학물질"));
                        chem.setRegul21(regulInfo.get("암,돌연변이성물질"));
                        chem.setRegul22(regulInfo.get("유해성미확인물질"));
                        chem.setRegul23(regulInfo.get("중점관리물질"));
                        chem.setRegul24(Synonym15UtilClass.checkRegul15(regul15Str));
                        if("Y".equals(chem.getRegul24()))
                        {
                            if (regul15Str.contains("1류"))
                                chem.setRegul24_1("Y");
                            else if(regul15Str.contains("2류"))
                                chem.setRegul24_2("Y");
                            else if(regul15Str.contains("3류"))
                                chem.setRegul24_3("Y");
                            else if(regul15Str.contains("4류"))
                                chem.setRegul24_4("Y");
                            else if(regul15Str.contains("5류"))
                                chem.setRegul24_5("Y");
                            else if(regul15Str.contains("6류"))
                                chem.setRegul24_6("Y");

                        }

                        chem.setRegul25(regulInfo.get("특수고압가스"));
                        chem.setRegul26(regulInfo.get("가연성가스"));
                        chem.setRegul27(regulInfo.get("특정고압가스"));
                        chem.setRegul28(regulInfo.get("독성가스"));
                        chem.setRegul29(regulInfo.get("폐유기용제"));
                        chem.setRegul30(Synonym15UtilClass.checkRegul16(regul16Str));
                    }
                }
            }

            /**
             * 유해성 분류 코드
             */
            if(parseResult.containsKey("02010000")) {
                Map<String, Object> ghsDatas = (Map<String, Object>) parseResult.get("02010000");
                Object result = ghsDatas.get("RESULT");
                List<String> ghsCodes = new ArrayList<>();
                List<String> ghsCodeName = new ArrayList<>();

                if(result instanceof String)
                    ghsCodes = Synonym2UtilClass.stringParsingGhsCode((String) result);
                else if (result instanceof List){
                    List<?> resultList = (List<?>) result;
                    ghsCodes = Synonym2UtilClass.listParsingGhsCode((List<String>) resultList);
                }
                sectionData.put("classResultCode", ghsCodes);

                for(String code : ghsCodes)
                {
                    ghsCodeName.add(Section2Enum.valueOf(code).getName());
                }

                sectionData.put("classResultName", ghsCodeName);
            }
            else {
                sectionData.put("classResultCode",new ArrayList<>());
                sectionData.put("classResultName", new ArrayList<>());
            }

            if(parseResult.containsKey("02020100"))
            {
                List<String> parsePictureList = new ArrayList<>();
                Map<String, Object> section02Data = (Map<String, Object>) parseResult.get("02020100");
                List<String> pictureList = (List<String>) section02Data.get("RESULT");

                for(String picture : pictureList)
                {
                    if(picture.contains("01") || picture.toUpperCase().contains("EXPLODING BOMB"))
                        parsePictureList.add("GHS01");
                    if(picture.contains("02") || picture.toUpperCase().contains("FLAME"))
                        parsePictureList.add("GHS02");
                    if(picture.contains("03") || picture.toUpperCase().contains("FLAME OVER CIRCLE"))
                        parsePictureList.add("GHS03");
                    if(picture.contains("04") || picture.toUpperCase().contains("GAS CYLINDER"))
                        parsePictureList.add("GHS04");
                    if(picture.contains("05") || picture.toUpperCase().contains("CORROSION"))
                        parsePictureList.add("GHS05");
                    if(picture.contains("06") || picture.toUpperCase().contains("SKULL AND CROSSBONES"))
                        parsePictureList.add("GHS06");
                    if(picture.contains("07") || picture.toUpperCase().contains("EXCLAMATION MARK"))
                        parsePictureList.add("GHS07");
                    if(picture.contains("08") || picture.toUpperCase().contains("HEALTH HAZARD"))
                        parsePictureList.add("GHS08");
                    if(picture.contains("09") || picture.toUpperCase().contains("ENVIRONMENT"))
                        parsePictureList.add("GHS09");

                }

                sectionData.put("pictures", parsePictureList);

            }

            if(parseResult.containsKey("02020300"))
            {
                List<IvInventoryHcode> parseHCodeList = new ArrayList<>();
                Map<String, Object> section02Data = (Map<String, Object>) parseResult.get("02020300");
                List<String> hCodeList = (List<String>) section02Data.get("RESULT");

                for(String hCode : hCodeList)
                {
                    IvInventoryHcode inventoryHcode = new IvInventoryHcode();

                    inventoryHcode.setContent(hCode);

                    parseHCodeList.add(inventoryHcode);
                }

                sectionData.put("hCode", parseHCodeList);
            }

            List<IvInventoryPcode> parsePCodeList = new ArrayList<>();

            if(parseResult.containsKey("02020401"))
            {
                Map<String, Object> section02Data = (Map<String, Object>) parseResult.get("02020401");
                List<String> pCodeList = (List<String>) section02Data.get("RESULT");

                for(String pCode : pCodeList)
                {
                    IvInventoryPcode inventoryPcode = new IvInventoryPcode();
                    inventoryPcode.setType(1);
                    inventoryPcode.setContent(pCode);

                    parsePCodeList.add(inventoryPcode);
                }
            }

            if(parseResult.containsKey("02020402"))
            {
                Map<String, Object> section02Data = (Map<String, Object>) parseResult.get("02020402");
                List<String> pCodeList = (List<String>) section02Data.get("RESULT");

                for(String pCode : pCodeList)
                {
                    IvInventoryPcode inventoryPcode = new IvInventoryPcode();
                    inventoryPcode.setType(2);
                    inventoryPcode.setContent(pCode);

                    parsePCodeList.add(inventoryPcode);
                }
            }

            if(parseResult.containsKey("02020403"))
            {
                Map<String, Object> section02Data = (Map<String, Object>) parseResult.get("02020403");
                List<String> pCodeList = (List<String>) section02Data.get("RESULT");

                for(String pCode : pCodeList)
                {
                    IvInventoryPcode inventoryPcode = new IvInventoryPcode();
                    inventoryPcode.setType(3);
                    inventoryPcode.setContent(pCode);

                    parsePCodeList.add(inventoryPcode);
                }
            }

            if(parseResult.containsKey("02020404"))
            {
                Map<String, Object> section02Data = (Map<String, Object>) parseResult.get("02020404");
                List<String> pCodeList = (List<String>) section02Data.get("RESULT");

                for(String pCode : pCodeList)
                {
                    IvInventoryPcode inventoryPcode = new IvInventoryPcode();
                    inventoryPcode.setType(4);
                    inventoryPcode.setContent(pCode);

                    parsePCodeList.add(inventoryPcode);
                }
            }

            sectionData.put("pCode", parsePCodeList);

            IvInventoryTransport transport = new IvInventoryTransport();

            if (parseResult.containsKey("14010000")) {
                Map<String, Object> section16Data = (Map<String, Object>) parseResult.get("14010000");

                String result = section16Data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 45)
                    result = result.substring(0, 45);

                transport.setUnNumber(result);
            } else
                transport.setUnNumber("");

            if (parseResult.containsKey("14020000")) {
                Map<String, Object> section16Data = (Map<String, Object>) parseResult.get("14020000");

                String result = section16Data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 45)
                    result = result.substring(0, 45);

                transport.setShippingName(result);
            } else
                transport.setShippingName("");

            if (parseResult.containsKey("14030000")) {
                Map<String, Object> section16Data = (Map<String, Object>) parseResult.get("14030000");

                String result = section16Data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 45)
                    result = result.substring(0, 45);

                transport.setTransHazardClass(result);
            } else
                transport.setTransHazardClass("");

            if (parseResult.containsKey("14040000")) {
                Map<String, Object> section16Data = (Map<String, Object>) parseResult.get("14040000");

                String result = section16Data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 45)
                    result = result.substring(0, 45);

                transport.setPackingGroup(result);
            } else
                transport.setPackingGroup("");

            if (parseResult.containsKey("14050000")) {
                Map<String, Object> section16Data = (Map<String, Object>) parseResult.get("14050000");

                String result = section16Data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 45)
                    result = result.substring(0, 45);

                transport.setEnvHazard(result);
            } else
                transport.setEnvHazard("");

            if (parseResult.containsKey("14060100")) {
                Map<String, Object> section16Data = (Map<String, Object>) parseResult.get("14060100");

                String result = section16Data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 45)
                    result = result.substring(0, 45);

                transport.setTypeFireAction(result);
            } else
                transport.setTypeFireAction("");

            if (parseResult.containsKey("14060200")) {
                Map<String, Object> section16Data = (Map<String, Object>) parseResult.get("14060200");

                String result = section16Data.get("RESULT").toString().replaceAll(cleanText, "");

                if(result.length() > 45)
                    result = result.substring(0, 45);

                transport.setTypeStillAction(result);
            } else
                transport.setTypeStillAction("");

            sectionData.put("transport", transport);

            sectionData.put("chemicalList", chemicalList);
            sectionData.put("status", 1);
            sectionData.put("orgData", parseResult.toString());

            return sectionData;


        }catch(RestClientException e)
        {
            sectionData.put("status", 99);
            sectionData.put("errorMsg", "API 통신 도중 오류가 발생했습니다.");
        }
        catch(Exception e)
        {
            sectionData.put("status", 99);
            sectionData.put("errorMsg", "파싱 도중 오류가 발생했습니다.");
        }


        return sectionData;
    }


    private static final Pattern[] datepatterns = new Pattern[]{
            Pattern.compile("(\\d{4})[-./](\\d{1,2})[-./](\\d{1,2})"), // YYYY-MM-DD, YYYY/MM/DD
            Pattern.compile("(\\d{4})년(\\d{1,2})월(\\d{1,2})일"),     // YYYY년MM월DD일
            Pattern.compile("(\\d{1,2})월(\\d{1,2})일(\\d{4})년"),     // MM월DD일YYYY년
            Pattern.compile("(\\d{1,2})[-/](\\d{1,2})[-/](\\d{4})")    // MM/DD/YYYY
    };

    public String findDate(String input) {
        for (Pattern pattern : datepatterns) {
            Matcher m = pattern.matcher(input);
            if (m.find()) {
                String yyyy, mm, dd;
                if (pattern.pattern().startsWith("(\\d{4})")) {
                    yyyy = m.group(1);
                    mm = m.group(2);
                    dd = m.group(3);
                } else {
                    yyyy = m.group(3);
                    mm = m.group(1);
                    dd = m.group(2);
                }
                mm = String.format("%02d", Integer.parseInt(mm));
                dd = String.format("%02d", Integer.parseInt(dd));
                return yyyy + mm + dd;
            }
        }
        return "0";
    }
}
