package com.example.backendchatwebsocket.application;

public interface ChatBroadcaster {
    void broadcast(ChatMessageEvent event);
}
