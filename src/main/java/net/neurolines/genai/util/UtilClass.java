package net.neurolines.genai.util;

public class UtilClass {

    private static final String clearWordPattern = "[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]"; //단어 정리 패턴(한글, 숫자, 영어 제외 모든 특수 문자 및 공백 삭제용

    public static String makeClearWord(String word)
    {
        return word.replaceAll(clearWordPattern, "");
    }
}
