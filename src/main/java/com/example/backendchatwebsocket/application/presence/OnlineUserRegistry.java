package com.example.backendchatwebsocket.application.presence;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class OnlineUserRegistry {
    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    public void register(String sessionId, String username) {
        sessions.put(sessionId, username);
    }

    public Optional<String> remove(String sessionId) {
        return Optional.ofNullable(sessions.remove(sessionId));
    }

    public Collection<String> listUsernames() {
        return Collections.unmodifiableCollection(sessions.values());
    }
}
