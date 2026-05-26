package io.github.javoxir.telegram.gateway.validation;

import io.github.javoxir.telegram.gateway.exception.TelegramGatewayValidationException;

import java.util.regex.Pattern;

public final class TelegramGatewayValidator {

    private static final Pattern E164_PATTERN = Pattern.compile("^\\+[1-9]\\d{7,14}$");

    private TelegramGatewayValidator() {
    }

    public static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new TelegramGatewayValidationException(fieldName + " must not be blank");
        }
        return value;
    }

    public static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !E164_PATTERN.matcher(phoneNumber).matches()) {
            throw new TelegramGatewayValidationException(
                "phoneNumber must be in E.164 format and contain 8-15 digits"
            );
        }
    }

    public static void validateCodeLength(Integer codeLength) {
        if (codeLength != null && (codeLength < 4 || codeLength > 8)) {
            throw new TelegramGatewayValidationException("codeLength must be between 4 and 8");
        }
    }

    public static void validateTtl(Integer ttl) {
        if (ttl != null && (ttl < 30 || ttl > 3600)) {
            throw new TelegramGatewayValidationException("ttl must be between 30 and 3600 seconds");
        }
    }
}
