package com.example.backendchatwebsocket.domain.service;

import com.example.backendchatwebsocket.domain.model.MessageId;

/**
 * Генерирует уникальные идентификаторы сообщений чата.
 *
 * <p>Реализация должна возвращать глобально уникальные значения, пригодные для упорядоченного
 * хранения.
 */
public interface MessageIdGenerator {
    MessageId nextId();
}
