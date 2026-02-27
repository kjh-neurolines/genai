package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IvWorkplace {

    /**
     * 사용사업본부코드
     */
    String hqCode;

    /**
     * 사용사업본부명(웹단 사용)
     */
    String hqName;

    /**
     * 사용사업장코드
     */
    String wpCode;

    /**
     * 사용사업자명
     */
    String wpName;

    /**
      * 사용자 이메일
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
     * 상태(1 : 정상, 0 : Block)
     */
    int status;


}
