package com.example.backendchatwebsocket.domain;

import java.util.List;

public interface ChatMessageRepository {
    void save(ChatMessage message);

    List<ChatMessage> findLastN(int count);
}
