package io.github.javoxir.telegram.gateway.dto.response;

import io.github.javoxir.telegram.gateway.dto.result.CheckSendAbilityResult;

public class CheckSendAbilityResponse extends TelegramGatewayResponse<CheckSendAbilityResult> {

    public CheckSendAbilityResponse(boolean ok, CheckSendAbilityResult result, String error) {
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
}
