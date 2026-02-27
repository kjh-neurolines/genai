package net.neurolines.genai.model.genai;

import lombok.Data;

@Data
public class AiUserSession {

    /**
     * 고유 등록 번호
     */
    int aiUsRegNo;

    /**
     * 그룹으로 사용한 회사 코드
     */
    String userCompanyCode;

    /**
     * 부서 코드
     */
    String dmCode;

    /**
     * 사용자 이메일
     */
    String email;

    /**
     * 세션 아이디
     */
    String session;

    /**
     * 개인화된 세션명
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
