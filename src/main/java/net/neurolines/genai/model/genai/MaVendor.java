package net.neurolines.genai.model.genai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaVendor {
    private String vendorCode;
    private String vendorName;
    private String manager;
    private String tel;
    private String email;
    private String managerTel;
    private String userCompanyCode;
    private int status;
    private int regDate;
}
