package io.github.javoxir.telegram.gateway.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendVerificationRequest {

    @JsonProperty("phone_number")
    private final String phoneNumber;

    @JsonProperty("request_id")
    private final String requestId;

    @JsonProperty("sender_username")
    private final String senderUsername;

    private final String code;

    @JsonProperty("code_length")
    private final Integer codeLength;

    @JsonProperty("callback_url")
    private final String callbackUrl;

    private final String payload;

    @JsonProperty("ttl")
    private final Integer ttl;

    private SendVerificationRequest(Builder builder) {
        this.phoneNumber = builder.phoneNumber;
        this.requestId = builder.requestId;
        this.senderUsername = builder.senderUsername;
        this.code = builder.code;
        this.codeLength = builder.codeLength;
        this.callbackUrl = builder.callbackUrl;
        this.payload = builder.payload;
        this.ttl = builder.ttl;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getCode() {
        return code;
    }

    public Integer getCodeLength() {
        return codeLength;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public String getPayload() {
        return payload;
    }

    public Integer getTtl() {
        return ttl;
    }

    public static final class Builder {
        private String phoneNumber;
        private String requestId;
        private String senderUsername;
        private String code;
        private Integer codeLength;
        private String callbackUrl;
        private String payload;
        private Integer ttl;

        private Builder() {
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder senderUsername(String senderUsername) {
            this.senderUsername = senderUsername;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder codeLength(Integer codeLength) {
            this.codeLength = codeLength;
            return this;
        }

        public Builder callbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
            return this;
        }

        public Builder payload(String payload) {
            this.payload = payload;
            return this;
        }

        public Builder ttl(Integer ttl) {
            this.ttl = ttl;
            return this;
        }

        public SendVerificationRequest build() {
            return new SendVerificationRequest(this);
        }
    }
}
