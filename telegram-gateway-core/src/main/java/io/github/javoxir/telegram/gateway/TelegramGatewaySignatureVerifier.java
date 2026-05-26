package io.github.javoxir.telegram.gateway;

import io.github.javoxir.telegram.gateway.exception.TelegramGatewayValidationException;
import io.github.javoxir.telegram.gateway.validation.TelegramGatewayValidator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Locale;

public class TelegramGatewaySignatureVerifier {

    private static final String HMAC_SHA256 = "HmacSHA256";

    public boolean verify(String token, String rawBody, String signatureHeader) {
        if (token == null || token.isBlank() || rawBody == null || signatureHeader == null || signatureHeader.isBlank()) {
            return false;
        }

        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(token.getBytes(StandardCharsets.UTF_8), HMAC_SHA256));
            byte[] expectedDigest = mac.doFinal(rawBody.getBytes(StandardCharsets.UTF_8));
            byte[] actualDigest = decodeSignature(normalizeSignature(signatureHeader));
            return actualDigest != null && MessageDigest.isEqual(expectedDigest, actualDigest);
        } catch (Exception ex) {
            return false;
        }
    }

    public void verifyOrThrow(String token, String rawBody, String signatureHeader) {
        TelegramGatewayValidator.requireNonBlank(token, "token");
        TelegramGatewayValidator.requireNonBlank(rawBody, "rawBody");
        TelegramGatewayValidator.requireNonBlank(signatureHeader, "signatureHeader");

        if (!verify(token, rawBody, signatureHeader)) {
            throw new TelegramGatewayValidationException("Invalid Telegram callback signature");
        }
    }

    private String normalizeSignature(String signatureHeader) {
        String value = signatureHeader.trim();
        if (value.regionMatches(true, 0, "sha256=", 0, 7)) {
            return value.substring(7);
        }
        return value.toLowerCase(Locale.ROOT);
    }

    private byte[] decodeSignature(String signatureHex) {
        try {
            return HexFormat.of().parseHex(signatureHex);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
