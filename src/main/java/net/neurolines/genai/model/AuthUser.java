package net.neurolines.genai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class AuthUser {

    @JsonProperty("NAME")
    String name;

    @JsonProperty("EMAIL")
    String email;

    @JsonProperty("COMPANY_CODE")
    String companyCode;

    @JsonProperty("COMPANY_NAME")
    String companyName;

    @JsonProperty("STATUS")
    String status;

    @JsonProperty("SERVICE")
    String service;

    @JsonProperty("PAYMENT_YN")
    String paymentYn;

    @JsonProperty("PAY_DATE")
    int payDate;

    @JsonProperty("START_DATE")
    int startDate;

    @JsonProperty("END_DATE")
    int endDate;

    @JsonProperty("PARTNER_ID")
    String partnerId;

    @JsonProperty("LEVEL")
    String level;
}
