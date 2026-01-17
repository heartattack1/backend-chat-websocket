package com.example.backendchatwebsocket.adapters.outbound.websocket;

import com.example.backendchatwebsocket.application.event.ChatUsersEvent;
import com.example.backendchatwebsocket.application.port.OnlineUsersBroadcaster;
import java.util.Collection;
import java.util.List;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class OnlineUsersBroadcasterWebSocketAdapter implements OnlineUsersBroadcaster {
    private final SimpMessagingTemplate messagingTemplate;

    public OnlineUsersBroadcasterWebSocketAdapter(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void broadcast(Collection<String> users) {
        List<String> payload = users.stream()
                .distinct()
                .sorted()
                .toList();
        messagingTemplate.convertAndSend("/topic/chat.users", new ChatUsersEvent(payload));
    }
}
