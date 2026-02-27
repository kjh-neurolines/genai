package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaPidDepMapping {
    private String productId; // 자재 ID
    private String departmentCode; // 부서코드
    private String userCompanyCode;
    private int status; // 상태
    private int regDate; // 등록일자
}
