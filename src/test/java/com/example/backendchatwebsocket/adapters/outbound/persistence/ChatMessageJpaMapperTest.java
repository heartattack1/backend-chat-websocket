package com.example.backendchatwebsocket.adapters.outbound.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.backendchatwebsocket.domain.model.ChatMessage;
import com.example.backendchatwebsocket.domain.model.MessageId;
import com.example.backendchatwebsocket.domain.model.UserId;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ChatMessageJpaMapperTest {

    private final ChatMessageJpaMapper mapper = new ChatMessageJpaMapper();

    @Test
    void mapsDomainToEntityAndBack() {
        MessageId messageId = new MessageId("01J2M5Z8V6H4C4B9QF6XH1T2Z3");
        UserId authorUserId = new UserId(UUID.fromString("c1b0e39f-0b99-4fb1-a3e1-d4c3b243f11e"));
        Instant createdAt = Instant.parse("2025-01-01T10:15:30Z");
        ChatMessage message = ChatMessage.post(messageId, authorUserId, "Hello", createdAt);

        ChatMessageJpaEntity entity = mapper.toEntity(message);
        ChatMessage mappedBack = mapper.toDomain(entity);

        assertThat(entity.getId()).isEqualTo(messageId.value());
        assertThat(entity.getAuthorUserId()).isEqualTo(authorUserId.value());
        assertThat(entity.getText()).isEqualTo("Hello");
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(mappedBack.getId()).isEqualTo(messageId);
        assertThat(mappedBack.getAuthorUserId()).isEqualTo(authorUserId);
        assertThat(mappedBack.getText()).isEqualTo("Hello");
        assertThat(mappedBack.getCreatedAt()).isEqualTo(createdAt);
    }
}
