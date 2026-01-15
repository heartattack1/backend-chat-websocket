package com.example.backendchatwebsocket.domain.model;

import java.time.Instant;
import java.util.Objects;

public final class ChatMessage {
    private final MessageId id;
    private final UserId authorUserId;
    private final String text;
    private final Instant createdAt;

    private ChatMessage(MessageId id, UserId authorUserId, String text, Instant createdAt) {
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

    public MessageId getId() {
        return id;
    }

    public UserId getAuthorUserId() {
        return authorUserId;
    }

    public String getText() {
        return text;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
