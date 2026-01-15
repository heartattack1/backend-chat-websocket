package com.example.backendchatwebsocket.adapters.inbound.websocket;

public class ChatSendMessageRequest {
    private String text;

    public ChatSendMessageRequest() {
    }

    public ChatSendMessageRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
