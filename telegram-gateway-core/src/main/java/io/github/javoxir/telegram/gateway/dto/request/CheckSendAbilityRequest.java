package io.github.javoxir.telegram.gateway.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckSendAbilityRequest {

    @JsonProperty("phone_number")
    private final String phoneNumber;

    private CheckSendAbilityRequest(Builder builder) {
        this.phoneNumber = builder.phoneNumber;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public static final class Builder {
        private String phoneNumber;

        private Builder() {
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public CheckSendAbilityRequest build() {
            return new CheckSendAbilityRequest(this);
        }
    }
}
