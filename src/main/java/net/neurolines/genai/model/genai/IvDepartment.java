package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IvDepartment {

    /**
     * 사용사업본부 코드
     */
    String hqCode;

    String hqName;

    /**
     * 사용사업장 코드
     */
    String wpCode;

    String wpName;

    /**
     * 부서코드
     */
    String dmCode;

    /**
     * 부서명
     */
    String dmName;

    /**
     * 이메일
     */
    String email;

    /**
     * 그룹으로 사용한 사용자 회사 코드
     */
    String userCompanyCode;

    /**
     * 등록일
     */
    int regYmd;

    /**
     * 상태(1:정상, 0: Block)
     */
    int status;
}
