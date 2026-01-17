package com.example.backendchatwebsocket.adapters.inbound.websocket;

import com.example.backendchatwebsocket.application.presence.OnlineUserRegistry;
import com.example.backendchatwebsocket.application.presence.UsernameResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketSessionEventListener {
    private final Logger logger = LoggerFactory.getLogger(WebSocketSessionEventListener.class);
    private final OnlineUserRegistry onlineUserRegistry;
    private final UsernameResolver usernameResolver;

    public WebSocketSessionEventListener(OnlineUserRegistry onlineUserRegistry, UsernameResolver usernameResolver) {
        this.onlineUserRegistry = onlineUserRegistry;
        this.usernameResolver = usernameResolver;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String username = usernameResolver.resolve(accessor);
        onlineUserRegistry.register(sessionId, username);
        logger.info("websocket_session_connected sessionId={} username={}", sessionId, username);
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String username = onlineUserRegistry.remove(sessionId).orElse(null);
        logger.info(
                "websocket_session_disconnected sessionId={} status={} username={}",
                sessionId,
                event.getCloseStatus(),
                username);
    }
}
