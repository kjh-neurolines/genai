package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaAuth {
    private String hqCode; // 사용사업본부 코드
    private String wpCode; // 사용사업장코드
    private String dmCode; // 부서코드
    private String email; // 이메일
    private String auth; // 권한
}
