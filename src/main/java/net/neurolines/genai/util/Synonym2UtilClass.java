package net.neurolines.genai.util;

import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Synonym2UtilClass {

    final static Pattern pattern = Pattern.compile("(.*?)(구분|등급|형식|분류)\\s*(.*)");

    public static List<String> stringParsingGhsCode(String str)
    {
        return listParsingGhsCode(java.util.List.of(str.split("\\\\n")));
    }

    public static List<String> listParsingGhsCode(List<String> list) {
        list = list.stream()
                .map(s -> {
                    Matcher matcher = pattern.matcher(s);
                    if (matcher.find()) {
                        String precedingSentence = UtilClass.makeClearWord(matcher.group(1)).trim(); // 키워드 앞
                        String followingWord = UtilClass.makeClearWord(matcher.group(3)).trim();     // 키워드 뒤 (전체 텍스트)
                        return precedingSentence + followingWord; // 앞뒤 조합
                    } else {
                        return UtilClass.makeClearWord(s);
                    }
                })
                .collect(Collectors.toList());

        return getGHSCode(list);
    }

    public static List<String> getGHSCode(List<String> parsingData) {
        List<String> ghsCodes = new ArrayList<>();

        Pattern pattern = Pattern.compile("([0-9A-Za-z]+)$");
        Matcher matcher;

        for (String data : parsingData) {

            matcher = pattern.matcher(data);
            String classResult = matcher.find() ? matcher.group(1) : "";

            String ghsCode = checkGhsType(data, classResult);

            if ("".equals(ghsCode))
                for (Section2Enum section2Enum : Section2Enum.values()) {
                    if (data.contains(section2Enum.getTotalClear())) {
                        ghsCode = section2Enum.name();
                        break;
                    }
                }

            if(!"".equals(ghsCode))
                ghsCodes.add(ghsCode);

        }

        if (ghsCodes.size() < 1)
            ghsCodes.add(Section2Enum.GHS999999.name());
        else
            ghsCodes = ghsCodes.stream()
                    .distinct()
                    .collect(Collectors.toList());


        return ghsCodes;
    }

    public static String checkGhsType(String data, String classResult) // 따로 구분 해야 하는 타입(단어 체크 필요)들 정리(급성 독성, 특정 표적 장기, 전신독성, 수생 환경 유해성)
    {
/*        Pattern pattern = Pattern.compile("([0-9A-Za-z]+)$");

        pattern = Pattern.compile("([0-9A-Za-z]+)$");

        Matcher matcher = pattern.matcher(data);*/

        if (data.contains("급성독성")) {
            // 경구 경피 흡입/가스 흡입/증기 흡입/분진/미스트
            if (data.contains("경구")) {
                switch (classResult) {
                    case "1":
                        return Section2Enum.GHS020101.name();
                    case "2":
                        return Section2Enum.GHS020102.name();
                    case "3":
                        return Section2Enum.GHS020103.name();
                    case "4":
                        return Section2Enum.GHS020104.name();
                    case "5":
                        return Section2Enum.GHS020105.name();
                }
            } else if (data.contains("경피")) {
                switch (classResult) {
                    case "1":
                        return Section2Enum.GHS020201.name();
                    case "2":
                        return Section2Enum.GHS020202.name();
                    case "3":
                        return Section2Enum.GHS020203.name();
                    case "4":
                        return Section2Enum.GHS020204.name();
                    case "5":
                        return Section2Enum.GHS020205.name();
                }
            } else if (data.contains("흡입")) {
                if (data.contains("증기")) {
                    switch (classResult) {
                        case "1":
                            return Section2Enum.GHS020401.name();
                        case "2":
                            return Section2Enum.GHS020402.name();
                        case "3":
                            return Section2Enum.GHS020403.name();
                        case "4":
                            return Section2Enum.GHS020404.name();
                        case "5":
                            return Section2Enum.GHS020405.name();
                    }
                } else if (data.contains("분진") || data.contains("미스트")) {
                    switch (classResult) {
                        case "1":
                            return Section2Enum.GHS020501.name();
                        case "2":
                            return Section2Enum.GHS020502.name();
                        case "3":
                            return Section2Enum.GHS020503.name();
                        case "4":
                            return Section2Enum.GHS020504.name();
                        case "5":
                            return Section2Enum.GHS020505.name();
                    }
                } else //구분 불가능한 경우 가스로 처리
                {
                    switch (classResult) {
                        case "1":
                            return Section2Enum.GHS020301.name();
                        case "2":
                            return Section2Enum.GHS020302.name();
                        case "3":
                            return Section2Enum.GHS020303.name();
                        case "4":
                            return Section2Enum.GHS020304.name();
                        case "5":
                            return Section2Enum.GHS020305.name();
                    }
                }
            }
        }

        if (data.contains("특정표적장기") || data.contains("전신독성"))
        {
            if("".equals(classResult))
                classResult = data;


            if (data.contains("1회")) {
                if (data.contains("호흡기")) {
                    return Section2Enum.GHS021303.name();
                } else if (data.contains("마취")) {
                    return Section2Enum.GHS021304.name();
                }

                if (classResult.contains("3"))
                    return Section2Enum.GHS021303.name();
                else if (classResult.contains("2"))
                    return Section2Enum.GHS021302.name();
                else if (classResult.contains("1"))
                    return Section2Enum.GHS021301.name();
            }else {


                if (classResult.contains("2"))
                    return Section2Enum.GHS021402.name();
                else if (classResult.contains("1"))
                    return Section2Enum.GHS021401.name();

            }
        }

        if (data.contains("수생")) {
            if (data.contains("급성"))
                return Section2Enum.GHS030101.name();
            else {
                switch (classResult) {
                    case "1":
                        return Section2Enum.GHS030201.name();
                    case "2":
                        return Section2Enum.GHS030202.name();
                    case "3":
                        return Section2Enum.GHS030203.name();
                    case "4":
                        return Section2Enum.GHS030204.name();
                }
            }
        }

        if(data.contains("피부") && (data.contains("자극") || data.contains("부식"))) {
            if (classResult.contains("2"))
                return Section2Enum.GHS020602.name();
            else if(classResult.contains("1A"))
                return Section2Enum.GHS020604.name();
            else if(classResult.contains("1B"))
                return Section2Enum.GHS020605.name();
            else if(classResult.contains("1C"))
                return Section2Enum.GHS020606.name();
            else if(classResult.contains("1"))
                return Section2Enum.GHS020601.name();
        }


        if(data.contains("눈") && data.contains("손상")) {
            if (classResult.contains("1"))
                return Section2Enum.GHS020701.name();
        }

        if(data.contains("눈") && data.contains("자극")) {
            if (classResult.contains("2A"))
                return Section2Enum.GHS020703.name();
            else if (classResult.contains("2B"))
                return Section2Enum.GHS020704.name();
            else if (classResult.contains("2"))
                return Section2Enum.GHS020702.name();

        }

        if(data.contains("생식") && data.contains("독성"))
        {
            if(classResult.contains("1A"))
                return Section2Enum.GHS021201.name();
            else if(classResult.contains("1B"))
                return Section2Enum.GHS021202.name();
            else if(classResult.contains("2"))
                return Section2Enum.GHS021203.name();
            else if(classResult.contains("1"))
                return Section2Enum.GHS021205.name();
            else
                if(data.contains("수유"))
                    return Section2Enum.GHS021204.name();
        }

        return "";
    }

}
