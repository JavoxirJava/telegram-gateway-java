package io.github.javoxir.telegram.gateway.dto.response;

import io.github.javoxir.telegram.gateway.dto.result.CheckVerificationStatusResult;
import io.github.javoxir.telegram.gateway.model.DeliveryStatus;
import io.github.javoxir.telegram.gateway.model.VerificationStatus;

public class CheckVerificationStatusResponse extends TelegramGatewayResponse<CheckVerificationStatusResult> {

    public CheckVerificationStatusResponse(boolean ok, CheckVerificationStatusResult result, String error) {
        super(ok, result, error);
    }

    public String getRequestId() {
        return getResult() == null ? null : getResult().getRequestId();
    }

    public VerificationStatus getVerificationStatus() {
        return getResult() == null ? null : getResult().getVerificationStatus();
    }

    public DeliveryStatus getDeliveryStatus() {
        return getResult() == null ? null : getResult().getDeliveryStatus();
    }

    public String getPayload() {
        return getResult() == null ? null : getResult().getPayload();
    }

    public boolean isVerified() {
        return VerificationStatus.VERIFIED == getVerificationStatus();
    }
}
