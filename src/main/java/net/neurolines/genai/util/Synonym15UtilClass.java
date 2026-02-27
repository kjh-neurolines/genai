package net.neurolines.genai.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

public class Synonym15UtilClass {

    private static final Map<String, List<String>> SYNONYMS = new HashMap<>();

    static {
        SYNONYMS.put("제조등금지물질", Arrays.asList("제조등금지물질", "법제117조에따른제조등이금지되는유해물질에해당되지않음", "금지대상유해물질",
                "제조등의금지유해물질", "본제품은산업안전보건법117조에따른제조등금지물질이아님", "산업안전보건법제117조제조등의금지", "법제37조에따른제조등이금지되는유해물질에해당되지않음", "제조금지물질허가대상유해물질허용기준이하유지대상물질", "제조등금지되어있는유해물", "제조금지물질"));

        SYNONYMS.put("제조허가대상물질", Arrays.asList("제조허가대상물질", "허가대상유해물질", "허가대상물질", "법제118조에따른허가대상유해물질에해당되지않음", "한국허가대상유해물질", "본제품은산업안전보건법118조에따른허가대상물질이아님",
                "산업안전보건법제118조제조등의허가", "법제38조에따른허가대상유해물질에해당되지않음", "제조금지물질허가대상유해물질허용기준이하유지대상물질", "제조또는사용허가대상유해물질", "제조의허가를받아야하는유해물"));

        SYNONYMS.put("작업자노출기준", Arrays.asList("작업자노출기준"));

        SYNONYMS.put("노출기준설정물질", Arrays.asList("노출기준설정대상물질", "노출기준설정물질", "화학물질의노출기준설정물질", "노출기준설정대상유해인자", "본제품은산업안전보건법106조에따른노출기준설정물질이아님", "노출기준설정물질에해당되지않음"));

        SYNONYMS.put("관리대상유해물질", Arrays.asList("관리대상유해물질", "산업안전보건기준에관한규칙제420조에따른관리대상유해물질에해당되지않음", "본제품은산업안전보건기준에관한규칙420조제1호에따른관리대상유해물질이아님", "산업안전보건기준에관한규칙별표12관리대상유해물질의종류", "관리대상유해물질에해당되지않음"));

        SYNONYMS.put("작업환경측정대상물질", Arrays.asList("작업환경측정대상물질", "작업환경측정물질", "시행규칙제186조제1항에따른작업환경측정물질에해당되지않음",
                "작업환경측정물질측정주기6개월", "작업환경측정대상물질측정주기6개월", "작업환경측정대상유해인자", "본제품은산업안전보건법시행규칙별표21의작업환경측정대상유해인자가아님",
                "산업안전보건법시행규칙별표21작업환경측정대상유해인자", "시행규칙제93조제1항에따른작업환경측정물질에해당되지않음", "작업환경측정물질에해당되지않음", "작업환경측정대상"));

        SYNONYMS.put("특수건강진단대상유해물질", Arrays.asList("특수건강진단대상유해물질", "특수건강진단물질", "특수건강검진대상물질", "시행규칙제201조에따른특수건강진단물질에해당되지않음", "특수건강진단대상물질", "특수건강진단대상유해인자", "특수건강진단대상물질진단주기12개월",
                "본제품은산업안전보건법시행규칙별표22의특수건강진단대상유해인자가아님", "산업안전보건법시행규칙별표22특수건강진단대상유해인자", "시행규칙제98조제2호에따른특수건강진단물질에해당되지않음", "특수건강진단대상"));

        SYNONYMS.put("특별관리물질", Arrays.asList("특별관리물질", "특별관리대상유해물질"));

        SYNONYMS.put("허용기준설정대상유해인자", Arrays.asList("허용기준이하유지대상물질", "허용기준준수물질", "허용기준설정대상유해인자", "허용기준이하유지대상유해인자",
                "본제품은산업안전보건법107조1항에따른허용기준설정물질이아님", "산업안전보건법시행규칙별표19유해인자별노출농도의허용기준", "허용기준설정물질", "유해물질허용기준이하유지대상물질"));

        SYNONYMS.put("공정안정관리대상유해인자", Arrays.asList("PSM대상물질", "공정안전보고서PSM제출대상물질", "본제품은산업안전보건법시행령43조에따른공정안전보고서PSM제출대상물질이아님", "공정안정관리대상물질", "공정안전보고서", "공정안전보고서(PSM)"));

        SYNONYMS.put("영업비밀인정제외물질", Arrays.asList("영업비밀인정제외물질"));

        SYNONYMS.put("국소배기장치안전검사대상물질", Arrays.asList("국소배기장치안전검사대상물질"));

        SYNONYMS.put("기존화학물질", Arrays.asList("기존화학물질"));


        SYNONYMS.put("유독물질", Arrays.asList("화학물질관리법제2조제2호의규정에의한유독물에해당되지않음", "유독물질", "법제2조제3호및제4호의규정에따라유독물및관찰물질에해당되지않음", "법제2조제2호의규정에따른유독물질",
                "본제품은화학물질관리법2조2항에따른유독물이아님", "화학물질의등록및평가등에관한법률제20조유독물질의지정", "화학물질관리법법제2조에의한유독물질에해당되지않음"));

        SYNONYMS.put("인체급성유해성물질", Arrays.asList("인체급성유해성물질") );

        SYNONYMS.put("인체만성유해성물질", Arrays.asList("인체만성유해성물질") );

        SYNONYMS.put("생태유해성물질", Arrays.asList("생태유해성물질") );

        SYNONYMS.put("허가물질", Arrays.asList("화학물질관리법제2조제3호의규정에의한허가물질에해당되지않음", "허가물질", "법제2조제3호의규정에따른허가물질", "본제품은화학물질관리법2조3항에따른허가물질이아님",
                "화학물질관리법제19조승인대상화학물질의등록및평가등에관한법률제25조", "화학물질관리법법제2조에의한허가물질에해당되지않음"));

        SYNONYMS.put("제한물질", Arrays.asList("제한또는금지물질", "화학물질관리법제2조제4호의규정에의한제한물질에해당되지않음", "제한물질금지물질", "법제32조의규정에따라취급제한금지물질에해당되지않음",
                "취급제한물질", "제한물질", "법제2조제4호의규정에따른제한물질", "본제품은화학물질관리법2조4항에따른제한물질이아님", "화학물질의등록및평가등에관한법률제27조제한물질", "화학물질관리법법제2조에의한제한물질에해당되지않음"));

        SYNONYMS.put("금지물질", Arrays.asList("제한또는금지물질", "화학물질관리법제2조제5호의규정에의한금지물질에해당되지않음", "제한물질금지물질", "법제32조의규정에따라취급제한금지물질에해당되지않음",
                "금지물질", "법제2조제5호의규정에따른금지물질", "본제품은화학물질관리법2조5항에따른금지물질이아님", "화학물질의등록및평가등에관한법률제27조금지물질", "화학물질관리법법제2조에의한금지물질에해당되지않음"));

        SYNONYMS.put("사고대비물질", Arrays.asList("화학물질관리법제2조제6호의규정에의한사고대비물질에해당되지않음", "사고대비물질", "법제38조의규정에따라사고대비물질에해당되지않음", "법제2조제6호의규정에따른사고대비물질",
                "본제품은화학물질관리법2조6항에따른사고대비물질이아님", "화학물질관리법제39조사고대비물질", "화학물질관리법법제2조에의한사고대비물질에해당되지않음"));

        SYNONYMS.put("배출량조사대상화학물질", Arrays.asList("배출량조사대상화학물질", "화학물질관리법시행령제6조에의한배출량조사대상화학물질에해당되지않음", "화학물질의배출량조사대상물질", "본제품에함유된성분은화학물질의배출량조사대상화학물질에해당되지않음",
                "본제품은화학물질배출량조사의대상이되는화학물질을함유하지않음", "화학물질관리법제11조화학물질배출량조사"));

        SYNONYMS.put("관찰물질", Arrays.asList("관찰물질", "유독물및관찰물질"));

        SYNONYMS.put("등록대상기존화학물질", Arrays.asList("등록대상기존화학물질", "기존물질", "기존화학물질", "등록대상기존화학물질PEC환경부고시제201592"));

        SYNONYMS.put("신규화학물질", Arrays.asList("신규화학물질", "변이원성의인정된화학물질신규제출화학물질"));

        SYNONYMS.put("중점관리물질", Arrays.asList("중점관리물질", "중점관리물질의지정"));

        SYNONYMS.put("위험물안전관리법", Arrays.asList("위험물안전관리법", "라위험물안전관리법에의한규제", "153위험물안전관리법에의한규제", "위험물안전관리법에의한규제", "다위험물안전관리법에의한규제", "다위험물안전관리법", "3위험물안전관리법에의한규제", "한국의위험물질안전관리법"));

        SYNONYMS.put("폐기물관리법", Arrays.asList("폐기물관리법", "마폐기물관리법에의한규제", "154폐기물관리법에의한규제", "폐기물관리법에의한규제", "라폐기물관리법에의한규제", "라폐기물관리", "4폐기물관리법에의한규제", "한국의폐기물관리법"));

        SYNONYMS.put("유해성미확인물질", Arrays.asList("유해성미확인물질") );
    }

    static String[] negativeWord = { "아님", "해당없음", "않음", "미규정", "없음", "비해당"};

    /**
     * 위험물 관리법 부정어
     */
    static String[] regul15Negative = { "규제되지 않음", "해당없음", "해당되지 않음" , "위험해당사항 없음", "비해당", "해당 없음", "위험물이 아님" , "해당사항없음" , "비위험물", "관련없음", "규정사항 없음", "해당하지 않음", "N/A", "자료없음", "미규정"};

     /**
      * 위험물 관리법 부정어
     */
    static String[] regul16Negative = {"규제되지 않음", "해당없음", "비해당", "해당 없음", "해당사항없음", "자료없음", "N/A"};

    /**
     * 규제정보 해당 여부 체크 로직
     * @param words
     * @return
     */
    public static Map<String, String> checkWordAvail(String words)
    {

        String[] wordList = words.split(",");
        List<String> clearWordList = new ArrayList<>();

        for(String str : wordList)
            clearWordList.add(UtilClass.makeClearWord(str));

        clearWordList = clearWordList.stream()
                .filter(s -> !s.isEmpty()) // 빈 문자열이 아닌 경우만 필터링
                .collect(Collectors.toList());

        // 물질별 규제정보 해당 여부 반환 Map
        Map<String, String> result = new HashMap<>();
        SYNONYMS.keySet().forEach(key -> {
            result.put(key, "N");
        });

        for(String word : clearWordList) {

            if(!Arrays.stream(negativeWord).anyMatch(word::contains)) {
                SYNONYMS.forEach((key, list) -> {
                    if ("N".equals(result.get(key))) // Flag가 Y로 바뀐 값은 해당됨으로 인식하여 미체크
                    {
                        for (String keyword : list) {
                            if (keyword.contains(word) || word.contains(keyword)) {
                                result.put(key, "Y");
                                break;
                            }
                        }
                    }
                });
            }
        }
        return result;
    }

    /**
     * 위험물 안전 관리법 부정어 체크
     * @param words
     * @return
     */
    public static String checkRegul15(String words)
    {
        String[] wordList = words.split(",");
        List<String> clearWordList = new ArrayList<>();

        for(String str : wordList)
            clearWordList.add(UtilClass.makeClearWord(str));

        String flag = "Y";

        for(String word : clearWordList) {
            for(String negative : regul15Negative)
            {
                String clear = UtilClass.makeClearWord(negative);
                if(word.equals(clear) || word.contains(clear) || clear.contains(word)) {
                    flag = "N";
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 폐기물 관리법 부정어 체크
     * @param words
     * @return
     */
    public static String checkRegul16(String words)
    {
        String[] wordList = words.split(",");
        List<String> clearWordList = new ArrayList<>();

        for(String str : wordList)
            clearWordList.add(UtilClass.makeClearWord(str));

        String flag = "Y";

        for(String word : clearWordList) {
            for(String negative : regul16Negative)
            {
                String clear = UtilClass.makeClearWord(negative);
                if(word.equals(clear) || word.contains(clear) || clear.contains(word)) {
                    flag = "N";
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 들어온 String Data Json으로 파싱 가능한지 판별하여, RESULT값 추출
     * @param str
     * @return
     */
    public String setJsonToStr(String str)
    {
        ObjectMapper objectMapper = new ObjectMapper();

        StringBuffer sb = new StringBuffer();

        try{

            JsonNode node  = objectMapper.readTree(str);
            Map<String, Map<String, Object>> jsonMap = objectMapper.readValue(str, new TypeReference<Map<String, Map<String, Object>>>() {});
            jsonMap.forEach((key, value) -> {
                if(value.get("RESULT") != null)
                    sb.append(value.get("RESULT"));
            });

        }catch (Exception e){
            System.out.print(e.toString());
        }

        return sb.toString();
    }

}