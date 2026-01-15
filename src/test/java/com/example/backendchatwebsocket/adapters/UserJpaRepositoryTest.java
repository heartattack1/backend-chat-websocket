package com.example.backendchatwebsocket.adapters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserJpaRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void savesAndLoadsUserById() {
        UserEntity user = buildUser("github", "octo-1");

        userJpaRepository.save(user);

        Optional<UserEntity> loaded = userJpaRepository.findById(user.getId());

        assertThat(loaded).isPresent();
        assertThat(loaded.get().getDisplayName()).isEqualTo("Octo User");
        assertThat(loaded.get().getProfileUrl()).isEqualTo("https://example.com/octo");
    }

    @Test
    void enforcesUniqueProviderAndProviderUserId() {
        UserEntity first = buildUser("google", "sub-123");
        UserEntity second = buildUser("google", "sub-123");

        userJpaRepository.save(first);

        assertThatThrownBy(() -> {
            userJpaRepository.save(second);
            userJpaRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    private UserEntity buildUser(String provider, String providerUserId) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        return new UserEntity(
                UUID.randomUUID(),
                provider,
                providerUserId,
                "Octo User",
                "https://example.com/octo",
                now.minusDays(1),
                true,
                now,
                now);
    }
}
