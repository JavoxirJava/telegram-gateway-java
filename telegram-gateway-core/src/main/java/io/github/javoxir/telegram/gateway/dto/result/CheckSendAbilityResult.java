package io.github.javoxir.telegram.gateway.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckSendAbilityResult {

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("request_cost")
    private Double requestCost;

    public String getRequestId() {
        return requestId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Double getRequestCost() {
        return requestCost;
    }
}
