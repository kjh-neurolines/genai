package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IvInventoryHcode {
    private int ivhRegno; // 고유번호
    private int iviRegno; // IV_INVENTORY >IVI_REGNO
    private int status; // 상태(1:active)
    private String content; // 내용
}
