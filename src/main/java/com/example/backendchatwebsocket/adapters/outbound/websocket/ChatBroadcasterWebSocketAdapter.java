package com.example.backendchatwebsocket.adapters.outbound.websocket;

import com.example.backendchatwebsocket.application.ChatBroadcaster;
import com.example.backendchatwebsocket.application.ChatMessageEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatBroadcasterWebSocketAdapter implements ChatBroadcaster {
    private final SimpMessagingTemplate messagingTemplate;

    public ChatBroadcasterWebSocketAdapter(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void broadcast(ChatMessageEvent event) {
        messagingTemplate.convertAndSend("/topic/chat.messages", event);
    }
}
