package com.example.backendchatwebsocket.adapters.outbound.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.backendchatwebsocket.domain.model.ChatMessage;
import com.example.backendchatwebsocket.domain.model.MessageId;
import com.example.backendchatwebsocket.domain.model.UserId;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChatMessageRepositoryImplTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private ChatMessageJpaRepository chatMessageJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void findsLastMessagesInChronologicalOrder() {
        UserEntity user = buildUser();
        userJpaRepository.save(user);
        ChatMessageRepositoryImpl adapter =
                new ChatMessageRepositoryImpl(chatMessageJpaRepository);

        ChatMessage first =
                message(
                        "01J2M5Z8V6H4C4B9QF6XH1T2Z1",
                        user.getId(),
                        "first",
                        Instant.parse("2025-01-01T00:00:00Z"));
        ChatMessage second =
                message(
                        "01J2M5Z8V6H4C4B9QF6XH1T2Z2",
                        user.getId(),
                        "second",
                        Instant.parse("2025-01-01T00:01:00Z"));
        ChatMessage third =
                message(
                        "01J2M5Z8V6H4C4B9QF6XH1T2Z3",
                        user.getId(),
                        "third",
                        Instant.parse("2025-01-01T00:02:00Z"));

        adapter.save(first);
        adapter.save(second);
        adapter.save(third);

        List<ChatMessage> lastTwo = adapter.findLastN(2);

        assertThat(lastTwo).extracting(ChatMessage::id).containsExactly(second.id(), third.id());
    }

    private static ChatMessage message(String id, UUID userId, String text, Instant createdAt) {
        return ChatMessage.post(new MessageId(id), new UserId(userId), text, createdAt);
    }

    private static UserEntity buildUser() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        return new UserEntity(
                UUID.randomUUID(),
                "vk",
                "vk-42",
                "Ivanov Ivan",
                "https://example.com/vk",
                now.minusDays(1),
                true,
                now,
                now);
    }
}
