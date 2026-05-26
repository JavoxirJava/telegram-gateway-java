package io.github.javoxir.telegram.gateway;

import io.github.javoxir.telegram.gateway.dto.request.CheckSendAbilityRequest;
import io.github.javoxir.telegram.gateway.dto.request.CheckVerificationStatusRequest;
import io.github.javoxir.telegram.gateway.dto.request.RevokeVerificationRequest;
import io.github.javoxir.telegram.gateway.dto.request.SendVerificationRequest;
import io.github.javoxir.telegram.gateway.dto.response.CheckSendAbilityResponse;
import io.github.javoxir.telegram.gateway.dto.response.CheckVerificationStatusResponse;
import io.github.javoxir.telegram.gateway.dto.response.RevokeVerificationResponse;
import io.github.javoxir.telegram.gateway.dto.response.SendVerificationResponse;

public interface TelegramGatewayClient {

    SendVerificationResponse sendVerificationMessage(SendVerificationRequest request);

    CheckSendAbilityResponse checkSendAbility(CheckSendAbilityRequest request);

    CheckVerificationStatusResponse checkVerificationStatus(CheckVerificationStatusRequest request);

    RevokeVerificationResponse revokeVerificationMessage(RevokeVerificationRequest request);
}
