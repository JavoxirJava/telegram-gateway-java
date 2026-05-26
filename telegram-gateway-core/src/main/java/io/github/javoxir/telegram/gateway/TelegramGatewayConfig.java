package io.github.javoxir.telegram.gateway;

import io.github.javoxir.telegram.gateway.validation.TelegramGatewayValidator;

import java.time.Duration;
import java.util.Objects;

public final class TelegramGatewayConfig {

    public static final String DEFAULT_BASE_URL = "https://gatewayapi.telegram.org";
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    private final String token;
    private final String baseUrl;
    private final Duration timeout;

    private TelegramGatewayConfig(Builder builder) {
        this.token = TelegramGatewayValidator.requireNonBlank(builder.token, "token");
        this.baseUrl = normalizeBaseUrl(Objects.requireNonNullElse(builder.baseUrl, DEFAULT_BASE_URL));
        this.timeout = Objects.requireNonNullElse(builder.timeout, DEFAULT_TIMEOUT);
        if (this.timeout.isNegative() || this.timeout.isZero()) {
            throw new IllegalArgumentException("timeout must be greater than zero");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getToken() {
        return token;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Duration getTimeout() {
        return timeout;
    }

    private static String normalizeBaseUrl(String baseUrl) {
        String normalized = baseUrl.trim();
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    public static final class Builder {
        private String token;
        private String baseUrl = DEFAULT_BASE_URL;
        private Duration timeout = DEFAULT_TIMEOUT;

        private Builder() {
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public TelegramGatewayConfig build() {
            return new TelegramGatewayConfig(this);
        }
    }
}
