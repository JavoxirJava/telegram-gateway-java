package io.github.javoxir.telegram.gateway.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RevokeVerificationRequest {

    @JsonProperty("request_id")
    private final String requestId;

    private RevokeVerificationRequest(Builder builder) {
        this.requestId = builder.requestId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getRequestId() {
        return requestId;
    }

    public static final class Builder {
        private String requestId;

        private Builder() {
        }

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public RevokeVerificationRequest build() {
            return new RevokeVerificationRequest(this);
        }
    }
}
