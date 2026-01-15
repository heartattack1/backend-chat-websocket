package com.example.backendchatwebsocket.adapters.outbound.persistence;

import com.example.backendchatwebsocket.domain.model.ChatMessage;
import com.example.backendchatwebsocket.domain.model.MessageId;
import com.example.backendchatwebsocket.domain.model.UserId;

public class ChatMessageJpaMapper {

    ChatMessageJpaEntity toEntity(ChatMessage message) {
        return new ChatMessageJpaEntity(
                message.getId().value(),
                message.getAuthorUserId().value(),
                message.getText(),
                message.getCreatedAt());
    }

    ChatMessage toDomain(ChatMessageJpaEntity entity) {
        return ChatMessage.post(
                new MessageId(entity.getId()),
                new UserId(entity.getAuthorUserId()),
                entity.getText(),
                entity.getCreatedAt());
    }
}
