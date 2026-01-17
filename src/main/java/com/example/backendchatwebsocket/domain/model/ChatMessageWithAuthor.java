package com.example.backendchatwebsocket.domain.model;

import java.time.Instant;
import java.util.Objects;

public record ChatMessageWithAuthor(MessageId id,
                                    UserId authorUserId,
                                    String authorName,
                                    String text,
                                    Instant createdAt) {
    public ChatMessageWithAuthor {
        Objects.requireNonNull(id, "Message id is required");
        Objects.requireNonNull(authorUserId, "Author user id is required");
        Objects.requireNonNull(authorName, "Author name is required");
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Message text cannot be null or blank");
        }
        Objects.requireNonNull(createdAt, "Created at is required");
    }
}
