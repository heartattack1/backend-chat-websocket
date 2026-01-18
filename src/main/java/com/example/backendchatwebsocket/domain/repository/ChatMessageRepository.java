package com.example.backendchatwebsocket.domain.repository;

import com.example.backendchatwebsocket.domain.model.ChatMessage;
import com.example.backendchatwebsocket.domain.model.ChatMessageWithAuthor;
import java.util.List;

/**
 * Хранит и запрашивает сообщения чата.
 *
 * <p>Контракт сортировки: {@link #findLastN(int)} и {@link #findLastNWithAuthor(int)} должны
 * возвращать сообщения в хронологическом порядке (от старых к новым) внутри запрошенного окна.
 */
public interface ChatMessageRepository {
    void save(ChatMessage message);

    List<ChatMessage> findLastN(int count);

    List<ChatMessageWithAuthor> findLastNWithAuthor(int count);
}
