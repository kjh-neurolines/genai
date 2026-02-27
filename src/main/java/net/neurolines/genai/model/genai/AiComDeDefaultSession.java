package net.neurolines.genai.model.genai;

import lombok.Data;

@Data
public class AiComDeDefaultSession {

    /**
     * 고유 등록 번호
     */
    int aiCddsRegNo;

    /**
     * 그룹으로 사용한 사용자 회사 코드
     */
    String userCompanyCode;

    /**
     * 부서 코드
     */
    String dmCode;

    /**
     * 부서 공통 디폴트 세션명
     */
    String sessionName;

    /**
     * 등록일
     */
    int regYmd;

    /**
     * 상태
     */
    int status;

}
