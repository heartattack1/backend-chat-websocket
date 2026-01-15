package com.example.backendchatwebsocket.adapters;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 32)
    private String provider;

    @Column(name = "provider_user_id", nullable = false, length = 128)
    private String providerUserId;

    @Column(name = "display_name", nullable = false, length = 64)
    private String displayName;

    @Column(name = "profile_url", length = 512)
    private String profileUrl;

    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected UserEntity() {
    }

    public UserEntity(UUID id,
                      String provider,
                      String providerUserId,
                      String displayName,
                      String profileUrl,
                      OffsetDateTime lastLoginAt,
                      boolean isActive,
                      OffsetDateTime createdAt,
                      OffsetDateTime updatedAt) {
        this.id = id;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
        this.lastLoginAt = lastLoginAt;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
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
