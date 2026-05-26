package io.github.javoxir.telegram.gateway.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckVerificationStatusRequest {

    @JsonProperty("request_id")
    private final String requestId;

    private final String code;

    private CheckVerificationStatusRequest(Builder builder) {
        this.requestId = builder.requestId;
        this.code = builder.code;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getRequestId() {
        return requestId;
    }

    public String getCode() {
        return code;
    }

    public static final class Builder {
        private String requestId;
        private String code;

        private Builder() {
        }

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public CheckVerificationStatusRequest build() {
            return new CheckVerificationStatusRequest(this);
        }
    }
}
