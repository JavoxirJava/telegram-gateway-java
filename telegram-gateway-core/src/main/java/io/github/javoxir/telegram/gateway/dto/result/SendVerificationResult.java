package io.github.javoxir.telegram.gateway.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.javoxir.telegram.gateway.model.DeliveryStatus;
import io.github.javoxir.telegram.gateway.model.VerificationStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SendVerificationResult {

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("request_cost")
    private Double requestCost;

    @JsonProperty("remaining_balance")
    private Double remainingBalance;

    @JsonProperty("delivery_status")
    private DeliveryStatus deliveryStatus;

    @JsonProperty("verification_status")
    private VerificationStatus verificationStatus;

    private String payload;

    public String getRequestId() {
        return requestId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Double getRequestCost() {
        return requestCost;
    }

    public Double getRemainingBalance() {
        return remainingBalance;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public String getPayload() {
        return payload;
    }
}
