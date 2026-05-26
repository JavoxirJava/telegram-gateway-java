package io.github.javoxir.telegram.gateway.dto.response;

public abstract class TelegramGatewayResponse<T> {

    private final boolean ok;
    private final T result;
    private final String error;

    protected TelegramGatewayResponse(boolean ok, T result, String error) {
        this.ok = ok;
        this.result = result;
        this.error = error;
    }

    public boolean isOk() {
        return ok;
    }

    public T getResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}
