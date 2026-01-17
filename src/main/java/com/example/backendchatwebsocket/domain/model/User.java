package com.example.backendchatwebsocket.domain.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public record User(UserId id, String provider, String providerUserId, String displayName, String profileUrl,
                   OffsetDateTime lastLoginAt, boolean isActive, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    public User(UserId id,
                String provider,
                String providerUserId,
                String displayName,
                String profileUrl,
                OffsetDateTime lastLoginAt,
                boolean isActive,
                OffsetDateTime createdAt,
                OffsetDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "User id is required");
        this.provider = Objects.requireNonNull(provider, "Provider is required");
        this.providerUserId = Objects.requireNonNull(providerUserId, "Provider user id is required");
        this.displayName = Objects.requireNonNull(displayName, "Display name is required");
        this.profileUrl = profileUrl;
        this.lastLoginAt = lastLoginAt;
        this.isActive = isActive;
        this.createdAt = Objects.requireNonNull(createdAt, "Created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at is required");
    }

}
