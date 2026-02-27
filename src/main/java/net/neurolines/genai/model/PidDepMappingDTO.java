package net.neurolines.genai.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class PidDepMappingDTO {

    String productId;

    String productCode;

    String productCodeName;

    String departmentCode;

    String departmentName;

    String regDate;

    String status;
}
