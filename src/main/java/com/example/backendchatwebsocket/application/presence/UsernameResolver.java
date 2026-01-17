package com.example.backendchatwebsocket.application.presence;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class UsernameResolver {
    private final PresenceProperties properties;

    public UsernameResolver(PresenceProperties properties) {
        this.properties = properties;
    }

    public String resolve(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes != null) {
            for (String headerName : properties.getUsernameHeaderNames()) {
                Object value = sessionAttributes.get(headerName);
                if (value instanceof String candidate && !candidate.isBlank()) {
                    return candidate.trim();
                }
            }
        }

        String principalName = Optional.ofNullable(accessor.getUser())
                .map(Object::toString)
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .orElse(null);
        if (principalName != null) {
            return principalName;
        }

        for (String headerName : properties.getUsernameHeaderNames()) {
            List<String> values = accessor.getNativeHeader(headerName);
            if (values == null) continue;
            for (String v : values) {
                if (v != null && !v.isBlank()) return v.trim();
            }
        }

        String sessionId = Optional.ofNullable(accessor.getSessionId()).orElse("unknown");
        String suffix = sessionId.length() > 8 ? sessionId.substring(0, 8) : sessionId;
        return properties.getGuestPrefix() + suffix;
    }
}
