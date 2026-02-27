package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IvInventoryTransport {
    private int ivitRegno; // 고유번호
    private int iviRegno; // IV_INVENTORY >IVI_REGNO
    private String unNumber; // 유엔번호
    private String shippingName; // 적정선적명
    private String transHazardClass; // 운송에서의 위험성 등급
    private String packingGroup; // 용기등급
    private String envHazard; // 해양오염물질
    private String typeFireAction; // 화재시 비상조치의 종류
    private String typeStillAction; // 유출시 비상조치의 종류
}

