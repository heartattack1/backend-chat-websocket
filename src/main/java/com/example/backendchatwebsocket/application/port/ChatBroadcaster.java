package com.example.backendchatwebsocket.application.port;

import com.example.backendchatwebsocket.application.event.ChatMessageEvent;

public interface ChatBroadcaster {
    void broadcast(ChatMessageEvent event);
}
