package com.example.backendchatwebsocket.adapters.outbound.persistence;

import java.time.Instant;
import java.util.UUID;

public interface ChatMessageWithAuthorProjection {
    String getId();

    UUID getAuthorUserId();

    String getAuthorName();

    String getText();

    Instant getCreatedAt();
}
