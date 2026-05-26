package io.github.javoxir.telegram.gateway.dto.response;

import io.github.javoxir.telegram.gateway.dto.result.RevokeVerificationResult;

public class RevokeVerificationResponse extends TelegramGatewayResponse<RevokeVerificationResult> {

    public RevokeVerificationResponse(boolean ok, RevokeVerificationResult result, String error) {
        super(ok, result, error);
    }

    public String getRequestId() {
        return getResult() == null ? null : getResult().getRequestId();
    }

    public boolean isRevoked() {
        return getResult() != null && getResult().isRevoked();
    }
}
