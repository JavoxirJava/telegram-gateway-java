package io.github.javoxir.telegram.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.javoxir.telegram.gateway.dto.TelegramGatewayApiResponse;
import io.github.javoxir.telegram.gateway.dto.request.CheckSendAbilityRequest;
import io.github.javoxir.telegram.gateway.dto.request.CheckVerificationStatusRequest;
import io.github.javoxir.telegram.gateway.dto.request.RevokeVerificationRequest;
import io.github.javoxir.telegram.gateway.dto.request.SendVerificationRequest;
import io.github.javoxir.telegram.gateway.dto.response.CheckSendAbilityResponse;
import io.github.javoxir.telegram.gateway.dto.response.CheckVerificationStatusResponse;
import io.github.javoxir.telegram.gateway.dto.response.RevokeVerificationResponse;
import io.github.javoxir.telegram.gateway.dto.response.SendVerificationResponse;
import io.github.javoxir.telegram.gateway.exception.TelegramGatewayApiException;
import io.github.javoxir.telegram.gateway.exception.TelegramGatewayHttpException;
import io.github.javoxir.telegram.gateway.exception.TelegramGatewaySerializationException;
import io.github.javoxir.telegram.gateway.validation.TelegramGatewayValidator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;

public class DefaultTelegramGatewayClient implements TelegramGatewayClient {

    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String USER_AGENT = "User-Agent";
    private static final String APPLICATION_JSON = "application/json";
    private static final String DEFAULT_USER_AGENT = "telegram-gateway-java";

    private static final String SEND_VERIFICATION_PATH = "sendVerificationMessage";
    private static final String CHECK_SEND_ABILITY_PATH = "checkSendAbility";
    private static final String CHECK_VERIFICATION_STATUS_PATH = "checkVerificationStatus";
    private static final String REVOKE_VERIFICATION_PATH = "revokeVerificationMessage";

    private final TelegramGatewayConfig config;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DefaultTelegramGatewayClient(TelegramGatewayConfig config) {
        this(config, defaultHttpClient(config), new ObjectMapper());
    }

    public DefaultTelegramGatewayClient(TelegramGatewayConfig config, HttpClient httpClient, ObjectMapper objectMapper) {
        if (config == null) {
            throw new IllegalArgumentException("config must not be null");
        }
        this.config = config;
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient must not be null");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");
    }

    @Override
    public SendVerificationResponse sendVerificationMessage(SendVerificationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        TelegramGatewayValidator.validatePhoneNumber(request.getPhoneNumber());
        TelegramGatewayValidator.validateCodeLength(request.getCodeLength());
        TelegramGatewayValidator.validateTtl(request.getTtl());

        TelegramGatewayApiResponse<io.github.javoxir.telegram.gateway.dto.result.SendVerificationResult> response = callApi(
            SEND_VERIFICATION_PATH,
            request,
            new TypeReference<>() {
            }
        );

        return new SendVerificationResponse(response.isOk(), response.getResult(), response.getError());
    }

    @Override
    public CheckSendAbilityResponse checkSendAbility(CheckSendAbilityRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        TelegramGatewayValidator.validatePhoneNumber(request.getPhoneNumber());

        TelegramGatewayApiResponse<io.github.javoxir.telegram.gateway.dto.result.CheckSendAbilityResult> response = callApi(
            CHECK_SEND_ABILITY_PATH,
            request,
            new TypeReference<>() {
            }
        );

        return new CheckSendAbilityResponse(response.isOk(), response.getResult(), response.getError());
    }

    @Override
    public CheckVerificationStatusResponse checkVerificationStatus(CheckVerificationStatusRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        TelegramGatewayValidator.requireNonBlank(request.getRequestId(), "requestId");
        TelegramGatewayValidator.requireNonBlank(request.getCode(), "code");

        TelegramGatewayApiResponse<io.github.javoxir.telegram.gateway.dto.result.CheckVerificationStatusResult> response = callApi(
            CHECK_VERIFICATION_STATUS_PATH,
            request,
            new TypeReference<>() {
            }
        );

        return new CheckVerificationStatusResponse(response.isOk(), response.getResult(), response.getError());
    }

    @Override
    public RevokeVerificationResponse revokeVerificationMessage(RevokeVerificationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        TelegramGatewayValidator.requireNonBlank(request.getRequestId(), "requestId");

        TelegramGatewayApiResponse<io.github.javoxir.telegram.gateway.dto.result.RevokeVerificationResult> response = callApi(
            REVOKE_VERIFICATION_PATH,
            request,
            new TypeReference<>() {
            }
        );

        return new RevokeVerificationResponse(response.isOk(), response.getResult(), response.getError());
    }

    private <T> TelegramGatewayApiResponse<T> callApi(String path, Object payload, TypeReference<TelegramGatewayApiResponse<T>> type) {
        String requestBody = serialize(payload);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(config.getBaseUrl() + "/" + path))
            .header(AUTHORIZATION, "Bearer " + config.getToken())
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .header(USER_AGENT, userAgentValue())
            .timeout(config.getTimeout())
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException ex) {
            throw new TelegramGatewayHttpException("HTTP request failed", -1, ex.getMessage());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new TelegramGatewayHttpException("HTTP request interrupted", -1, ex.getMessage());
        }

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new TelegramGatewayHttpException(
                "Unexpected HTTP status: " + response.statusCode(),
                response.statusCode(),
                response.body()
            );
        }

        TelegramGatewayApiResponse<T> parsed = deserialize(response.body(), type);
        if (!parsed.isOk()) {
            String error = parsed.getError() == null || parsed.getError().isBlank()
                ? "Telegram Gateway API returned an error"
                : parsed.getError();
            throw new TelegramGatewayApiException(error);
        }
        return parsed;
    }

    private String serialize(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            throw new TelegramGatewaySerializationException("Failed to serialize request", ex);
        }
    }

    private <T> TelegramGatewayApiResponse<T> deserialize(String body, TypeReference<TelegramGatewayApiResponse<T>> type) {
        try {
            return objectMapper.readValue(body, type);
        } catch (JsonProcessingException ex) {
            throw new TelegramGatewaySerializationException("Failed to deserialize response", ex);
        }
    }

    private static HttpClient defaultHttpClient(TelegramGatewayConfig config) {
        Duration timeout = config == null ? TelegramGatewayConfig.DEFAULT_TIMEOUT : config.getTimeout();
        return HttpClient.newBuilder().connectTimeout(timeout).build();
    }

    private String userAgentValue() {
        String version = DefaultTelegramGatewayClient.class.getPackage().getImplementationVersion();
        if (version == null || version.isBlank()) {
            return DEFAULT_USER_AGENT + "/dev";
        }
        return DEFAULT_USER_AGENT + "/" + version;
    }
}
