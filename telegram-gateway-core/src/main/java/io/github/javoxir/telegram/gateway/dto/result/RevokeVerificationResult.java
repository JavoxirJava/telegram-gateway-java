package io.github.javoxir.telegram.gateway.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RevokeVerificationResult {

    @JsonProperty("request_id")
    private String requestId;

    private boolean revoked;

    public String getRequestId() {
        return requestId;
    }

    public boolean isRevoked() {
        return revoked;
    }
}
