package com.example.backendchatwebsocket.adapters.outbound.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "chat_messages")
public class ChatMessageEntity {

    @Id
    @Column(length = 26, nullable = false)
    private String id;

    @Column(name = "author_user_id", nullable = false)
    private UUID authorUserId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ChatMessageEntity() {
    }

    public ChatMessageEntity(String id, UUID authorUserId, String text, Instant createdAt) {
        this.id = id;
        this.authorUserId = authorUserId;
        this.text = text;
        this.createdAt = createdAt;
    }

}
