package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IvInventoryClassResult {
    private int ivicrRegno; // 고유번호
    private int iviRegno; // IV_INVENTORY >IVI_REGNO
    private String classResultCode; // 분류결과코드
}
