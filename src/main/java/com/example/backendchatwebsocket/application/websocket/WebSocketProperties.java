package com.example.backendchatwebsocket.application.websocket;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chat.websocket")
public class WebSocketProperties {
    private List<String> allowedOrigins = List.of("http://localhost:8080");

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}
