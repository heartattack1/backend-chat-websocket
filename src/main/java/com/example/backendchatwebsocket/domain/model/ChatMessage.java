package com.example.backendchatwebsocket.domain.model;

import java.time.Instant;
import java.util.Objects;

public record ChatMessage(MessageId id, UserId authorUserId, String text, Instant createdAt) {
    public ChatMessage(MessageId id, UserId authorUserId, String text, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "Message id is required");
        this.authorUserId = Objects.requireNonNull(authorUserId, "Author user id is required");
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Message text cannot be null or blank");
        }
        this.text = text;
        this.createdAt = Objects.requireNonNull(createdAt, "Created at is required");
    }

    public static ChatMessage post(MessageId id, UserId authorUserId, String text, Instant createdAt) {
        return new ChatMessage(id, authorUserId, text, createdAt);
    }

}
