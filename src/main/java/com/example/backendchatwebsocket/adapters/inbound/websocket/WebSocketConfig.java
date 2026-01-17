package com.example.backendchatwebsocket.adapters.inbound.websocket;

import com.example.backendchatwebsocket.application.presence.PresenceProperties;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final PresenceProperties presenceProperties;

    public WebSocketConfig(PresenceProperties presenceProperties) {
        this.presenceProperties = presenceProperties;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .addInterceptors(new UsernameHandshakeInterceptor(presenceProperties))
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue");
    }

    private static final class UsernameHandshakeInterceptor implements HandshakeInterceptor {
        private final PresenceProperties presenceProperties;

        private UsernameHandshakeInterceptor(PresenceProperties presenceProperties) {
            this.presenceProperties = presenceProperties;
        }

        @Override
        public boolean beforeHandshake(
                ServerHttpRequest request,
                ServerHttpResponse response,
                WebSocketHandler wsHandler,
                Map<String, Object> attributes
        ) {
            List<String> headerNames = presenceProperties.getUsernameHeaderNames();
            for (String headerName : headerNames) {
                List<String> values = request.getHeaders().get(headerName);
                if (values == null) {
                    continue;
                }
                String candidate = values.stream()
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(value -> !value.isEmpty())
                        .findFirst()
                        .orElse(null);
                if (candidate != null) {
                    attributes.put(headerName, candidate);
                    break;
                }
            }
            return true;
        }

        @Override
        public void afterHandshake(
                ServerHttpRequest request,
                ServerHttpResponse response,
                WebSocketHandler wsHandler,
                Exception exception
        ) {
            // no-op
        }
    }
}
