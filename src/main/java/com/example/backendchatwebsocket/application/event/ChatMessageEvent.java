package com.example.backendchatwebsocket.application.event;

import java.time.Instant;

public final class ChatMessageEvent {
    private final String id;
    private final String text;
    private final String author;
    private final Instant createdAt;

    public ChatMessageEvent(String id, String text, String author, Instant createdAt) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
