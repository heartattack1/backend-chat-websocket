package com.example.backendchatwebsocket.adapters.inbound.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketSessionEventListener {
    private final Logger logger = LoggerFactory.getLogger(WebSocketSessionEventListener.class);

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        logger.info("websocket_session_connected sessionId={}", event.getMessage().getHeaders().get("simpSessionId"));
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        logger.info("websocket_session_disconnected sessionId={} status={}", event.getSessionId(), event.getCloseStatus());
    }
}
