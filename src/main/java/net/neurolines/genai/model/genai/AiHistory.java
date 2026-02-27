package net.neurolines.genai.model.genai;

import lombok.Data;

/**
 * 사용자 질문(PROMPT) 내역
 */
@Data
public class AiHistory {

    /**
     * 고유등록번호
     */
    int aihRegNo;

    /**
     * 사용자 이메일
     */
    String email;

    /**
     * 세션 ID
     */
    String session;

    /**
     * 그룹으로 사용한 사용자 회사 코드
     */
    String userCompanyCode;

    /**
     * 질문
     */
    String prompt;

    /**
     * 등록일
     */
    int regYmd;

    /**
     * 상태
     */
    int status;

}
