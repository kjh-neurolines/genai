package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IvInventoryPicture {
    private int ivipRegno; // 고유번호
    private int iviRegno; // IV_INVENTORY >IVI_REGNO
    private String picture; // 그림문자
}
