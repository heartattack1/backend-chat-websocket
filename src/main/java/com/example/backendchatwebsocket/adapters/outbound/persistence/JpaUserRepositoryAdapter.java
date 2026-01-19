package com.example.backendchatwebsocket.adapters.outbound.persistence;

import com.example.backendchatwebsocket.domain.model.User;
import com.example.backendchatwebsocket.domain.model.UserId;
import com.example.backendchatwebsocket.domain.repository.UserRepository;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(UserId id) {
        return userJpaRepository.findById(id.value()).map(this::toDomain);
    }

    @Override
    public void save(User user) {
        userJpaRepository.save(toEntity(user));
    }

    private User toDomain(UserEntity entity) {
        return new User(
                new UserId(entity.getId()),
                entity.getAuthProvider(),
                entity.getAuthProviderUserId(),
                entity.getDisplayName(),
                entity.getProfileUrl(),
                entity.getLastLoginAt(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(
                user.id().value(),
                user.provider(),
                user.providerUserId(),
                user.displayName(),
                user.profileUrl(),
                user.lastLoginAt(),
                user.isActive(),
                user.createdAt(),
                user.updatedAt()
        );
    }
}
