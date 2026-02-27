package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IvHeadQuarters {

    String hqCode;

    /**
     * 사업사용본부명
     */
    String hqName;

    /**
     * 사용자 이메일
     */
    String email;

    /**
     * 사용자 회사
     */
    String userCompanyCode;

    /**
     * 등록일
     */
    int regYmd;

    /**
     * 상태(1:정상, 0 : Block)
     */
    int status;
}

