package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IvInventoryDepartment {
    private int ividRegno; // 고유번호
    private String hqCode; // 사용사업본부 코드
    private String wpCode; // 사용사업장코드
    private int iviRegno; // IV_INVENTORY >IVI_REGNO
    private String departmentCode; // 부서코드
    private int regYmd; // 등록일
    private int status; // 상태(1:active)
}
