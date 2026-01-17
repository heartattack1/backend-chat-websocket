package com.example.backendchatwebsocket.domain.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public class User {
    private final UserId id;
    private final String provider;
    private final String providerUserId;
    private final String displayName;
    private final String profileUrl;
    private final OffsetDateTime lastLoginAt;
    private final boolean isActive;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

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

    public UserId getId() {
        return id;
    }

    public String getProvider() {
        return provider;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public OffsetDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
