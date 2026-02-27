package net.neurolines.genai.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDTO {

    // 자재 ID
    private String productId;

    // 공급업체
    private String companyName;

    private String vendorCode;

    // 품목 코드
    private String gubun;

    // 자재 코드
    private String productCode;

    private String email;

    private String langCode;

    private String country;

}
