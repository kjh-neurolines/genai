package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IvInventoryChemical {
    private int ivicRegno; // 고유번호
    private int iviRegno; // IV_INVENTORY >IVI_REGNO
    private String formula; // 분자식
    private String chmicalName; // 물질명
    private String casNo; // cas no
    private String regNo; // 고유번호
    private String percent; // 함유량

    private String twa; // 노출기준(허용농도값)

    private String oral; // 경구
    private String dermal; // 경피
    private String inhalation; // 흡입
    private String inhalationGas; // 흡입(가스)
    private String inhalationSteam; // 흡입(증기)
    private String inhalationMist; // 흡입(분진, 미스트)
    private String skinCorrosion; // 피부부식성 또는 자극성
    private String eyeDamage; // 심한 눈손상 또는 자극성
    private String respiratoryIrritability; // 호흡기 과민성
    private String skinSensitivity; // 피부과민성
    private String mutagenicity; // 발암성
    private String carcinogenicity; // 생식세포변이원성
    private String reproductiveToxicity; // 생식독성
    private String targetOrganToxicitySingle; // 표적장기 전신독성물질(1회 노출)
    private String targetOrganToxicityRepeat; // 표적장기 전신독성물질(반복 노출)
    private String aspirationHazard; // 흡인유해성

    private String ecotoxicityFish; // 급성수생환경유해성 어류
    private String ecotoxicityInvert; // 급성수생환경유해성 갑각류
    private String ecotoxicityAlgae; // 급성수생환경유해성 조류

    private String chronicFish; // 만성수생환경유해성 어류
    private String chronicInvert; // 만성수생환경유해성 갑각류
    private String chronicAlgae; // 만성수생환경유해성 조류


    private String persistence; // 잔류성 및 분해성 잔류성
    private String degradability; // 잔류성 및 분해성 분해성
    private String bioPotential; // 생물농축성
    private String bioDegradable; // 생분해성
    private String mobilityInSoil; // 토양이동성
    private String hazardousOzne; // 오존층 유해성
    private String otherEffect; // 기타 유해 영향


    private String nationalExrposure; // 노출정보 국내규정
    private String acgihReg; // 노출정보 AGHIH 규정
    private String oshaReg; // 노출정보 OSHA 규정
    private String bioReg; // 생물학적 노출규정

    private String regul1; // 제조금지물질
    private String regul2; // 제조허가대상물질
    private String regul3; // 노출기준설정대상물질
    private String regul4; // 작업자노출기준
    private String regul5; // 관리대상유해물질
    private String regul6; // 작업환경측정대상유해인자
    private String regul7; // 특수건강진단대상유해인자
    private String regul8; // 특별관리물질
    private String regul9; // 허용기준 설정 대상 유해인자
    private String regul10; // 공정안전관리대상물질
    private String regul11; // 영업비밀인정제외물질
    private String regul12; // 국소배기장치 안전검사 대상물질
    private String regul13; // 기존화학물질
    private String regul14; // 유독물질
    private String regul14_1; // 인체급성유해성물질
    private String regul14_2; // 인체만성유해성물질
    private String regul14_3; // 생태유해성물질
    private String regul15; // 허가물질
    private String regul16; // 제한물질
    private String regul17; // 금지물질
    private String regul18; // 사고대비물질
    private String regul19; // 배출량조사대상화학물질
    private String regul20; // 등록대상기존화학물질
    private String regul21; // 암, 돌연변이성물질
    private String regul22; // 유해성 미확인물질
    private String regul23; // 중점관리물질
    private String regul24; // 위험물
    private String regul24_1; // 위험물 1류
    private String regul24_2; // 위험물 2류
    private String regul24_3; // 위험물 3류
    private String regul24_4; // 위험물 4류
    private String regul24_5; // 위험물 5류
    private String regul24_6; // 위험물 6류
    private String regul25; // 특수고압가스
    private String regul26; // 가연성가스
    private String regul27; // 특정고압가스
    private String regul28; // 독성가스
    private String regul29; // 폐유기용제
    private String regul30; // 지정폐기물

    private int regYmd; // 등록일
    private int status; // 상태(1:active)
}
