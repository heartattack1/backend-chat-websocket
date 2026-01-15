package com.example.backendchatwebsocket.domain.repository;

import com.example.backendchatwebsocket.domain.model.ChatMessage;

import java.util.List;

public interface ChatMessageRepository {
    void save(ChatMessage message);

    List<ChatMessage> findLastN(int count);
}
