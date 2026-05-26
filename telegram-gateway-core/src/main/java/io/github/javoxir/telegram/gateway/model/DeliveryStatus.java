package io.github.javoxir.telegram.gateway.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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
