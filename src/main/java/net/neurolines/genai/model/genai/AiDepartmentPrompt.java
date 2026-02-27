package net.neurolines.genai.model.genai;

import lombok.Data;

/**
 * 부서에서 자주 사용하는 질문(PROMPT) 내역
 */
@Data
public class AiDepartmentPrompt {

    /**
     * 고유 등록 번호
     */
    int adipRegNo;

    /**
     * 부서 코드
     */
    String dmCode;

    /**
     * 사용자 이메일
     */
    String email;

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
