package com.example.backendchatwebsocket.adapters.outbound.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByProviderAndProviderUserId(String provider, String providerUserId);
}
