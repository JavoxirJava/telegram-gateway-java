package io.github.javoxir.telegram.gateway.exception;

public class TelegramGatewayException extends RuntimeException {

    public TelegramGatewayException(String message) {
        super(message);
    }

    public TelegramGatewayException(String message, Throwable cause) {
        super(message, cause);
    }
}
