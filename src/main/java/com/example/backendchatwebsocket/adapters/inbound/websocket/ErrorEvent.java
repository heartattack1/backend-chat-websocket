package com.example.backendchatwebsocket.adapters.inbound.websocket;

public class ErrorEvent {
    private final String message;
    private final String code;
    private final String timestamp;

    public ErrorEvent(String message, String code, String timestamp) {
        this.message = message;
        this.code = code;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
