package com.example.backendchatwebsocket.domain.repository;

import com.example.backendchatwebsocket.domain.model.ChatMessage;
import com.example.backendchatwebsocket.domain.model.ChatMessageWithAuthor;

import java.util.List;

public interface ChatMessageRepository {
    void save(ChatMessage message);

    List<ChatMessage> findLastN(int count);

    List<ChatMessageWithAuthor> findLastNWithAuthor(int count);
}
