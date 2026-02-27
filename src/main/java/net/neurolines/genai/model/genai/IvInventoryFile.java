package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IvInventoryFile {
    private int ivifRegno; // 고유번호
    private int iviRegno; // IV_INVENTORY >IVI_REGNO
    private String country; // 국가
    private String langCode; // 언어
    private String orgFilename; // 원본 파일명
    private String fileName; // 업로드 파일명
    private int status; // 상태(1:active)
    private int regYmd; // 등록일
}

