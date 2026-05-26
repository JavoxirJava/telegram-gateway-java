package io.github.javoxir.telegram.gateway.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.javoxir.telegram.gateway.model.DeliveryStatus;
import io.github.javoxir.telegram.gateway.model.VerificationStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckVerificationStatusResult {

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("verification_status")
    private VerificationStatus verificationStatus;

    @JsonProperty("delivery_status")
    private DeliveryStatus deliveryStatus;

    private String payload;

    public String getRequestId() {
        return requestId;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getPayload() {
        return payload;
    }
}
