package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IvInventoryPcode {
    private int ivpRegno; // 고유번호
    private int iviRegno; // IV_INVENTORY >IVI_REGNO
    private int status; // 상태(1:active)
    private int type; // 타입(1:예방,2:대응,3:저장,4:폐기)
    private String content; // 내용
}
