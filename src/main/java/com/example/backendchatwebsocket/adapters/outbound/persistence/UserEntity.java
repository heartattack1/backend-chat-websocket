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
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;

    @Column(name = "auth_provider", nullable = false, length = 32)
    private String authProvider;

    @Column(name = "auth_provider_user_id", nullable = false, length = 128)
    private String authProviderUserId;

    @Column(name = "display_name", nullable = false, length = 64)
    private String displayName;

    @Column(name = "profile_url", length = 512)
    private String profileUrl;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected UserEntity() {
    }

    public UserEntity(UUID id,
                      String provider,
                      String authProviderUserId,
                      String displayName,
                      String profileUrl,
                      Instant lastLoginAt,
                      Boolean isActive,
                      Instant createdAt,
                      Instant updatedAt) {
        this.id = id;
        this.authProvider = provider;
        this.authProviderUserId = authProviderUserId;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
        this.lastLoginAt = lastLoginAt;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
