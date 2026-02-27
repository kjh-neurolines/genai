package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaProductId {
    private String productId; // 자재 ID
    private String productCode; // 자재코드
    private String productCodeName; // 자재코드명
    private String vendorCode; // 벤더코드
    private String vendorName; //DB에 없음 벤더명 추출용
    private String userCompanyCode;
    private int status; // 상태
    private int regDate; // 등록일자
}
