package io.github.javoxir.telegram.gateway.exception;

public class TelegramGatewayHttpException extends TelegramGatewayException {

    private final int statusCode;
    private final String responseBody;

    public TelegramGatewayHttpException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
