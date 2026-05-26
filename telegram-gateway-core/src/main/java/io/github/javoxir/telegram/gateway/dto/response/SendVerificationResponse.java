package io.github.javoxir.telegram.gateway.dto.response;

import io.github.javoxir.telegram.gateway.dto.result.SendVerificationResult;
import io.github.javoxir.telegram.gateway.model.DeliveryStatus;
import io.github.javoxir.telegram.gateway.model.VerificationStatus;

public class SendVerificationResponse extends TelegramGatewayResponse<SendVerificationResult> {

    public SendVerificationResponse(boolean ok, SendVerificationResult result, String error) {
        super(ok, result, error);
    }

    public String getRequestId() {
        return getResult() == null ? null : getResult().getRequestId();
    }

    public String getPhoneNumber() {
        return getResult() == null ? null : getResult().getPhoneNumber();
    }

    public Double getRequestCost() {
        return getResult() == null ? null : getResult().getRequestCost();
    }

    public Double getRemainingBalance() {
        return getResult() == null ? null : getResult().getRemainingBalance();
    }

    public DeliveryStatus getDeliveryStatus() {
        return getResult() == null ? null : getResult().getDeliveryStatus();
    }

    public VerificationStatus getVerificationStatus() {
        return getResult() == null ? null : getResult().getVerificationStatus();
    }

    public String getPayload() {
        return getResult() == null ? null : getResult().getPayload();
    }
}
