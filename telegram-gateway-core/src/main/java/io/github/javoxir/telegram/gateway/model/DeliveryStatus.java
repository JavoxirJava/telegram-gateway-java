package io.github.javoxir.telegram.gateway.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;

public enum DeliveryStatus {
    SENT("sent"),
    DELIVERED("delivered"),
    FAILED("failed"),
    PENDING("pending"),
    UNKNOWN("unknown");

    private final String value;

    DeliveryStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static DeliveryStatus fromValue(JsonNode node) {
        if (node == null || node.isNull()) {
            return UNKNOWN;
        }

        String value = null;
        if (node.isTextual()) {
            value = node.asText();
        } else if (node.isObject()) {
            JsonNode statusNode = node.get("status");
            if (statusNode != null && statusNode.isTextual()) {
                value = statusNode.asText();
            } else {
                JsonNode valueNode = node.get("value");
                if (valueNode != null && valueNode.isTextual()) {
                    value = valueNode.asText();
                }
            }
        }

        return fromValue(value);
    }

    public static DeliveryStatus fromValue(String value) {
        if (value == null) {
            return UNKNOWN;
        }
        return Arrays.stream(values())
            .filter(status -> status.value.equalsIgnoreCase(value))
            .findFirst()
            .orElse(UNKNOWN);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
