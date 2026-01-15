package com.example.backendchatwebsocket.application.scenario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.example.backendchatwebsocket.application.command.PostMessageCommand;
import com.example.backendchatwebsocket.domain.model.ChatMessage;
import com.example.backendchatwebsocket.domain.model.MessageId;
import com.example.backendchatwebsocket.domain.model.UserId;
import com.example.backendchatwebsocket.domain.repository.ChatMessageRepository;
import com.example.backendchatwebsocket.domain.service.MessageIdGenerator;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PostMessageScenarioTest {

    @Test
    void usesGeneratorAndSavesMessage() {
        MessageId generatedId = new MessageId("01J2M5Z8V6H4C4B9QF6XH1T2Z3");
        MessageIdGenerator generator = () -> generatedId;
        InMemoryChatMessageRepository repository = new InMemoryChatMessageRepository();
        Instant now = Instant.parse("2025-01-01T00:00:00Z");
        Clock clock = Clock.fixed(now, ZoneOffset.UTC);
        PostMessageScenario scenario = new PostMessageScenario(repository, generator, clock);

        UserId authorUserId = new UserId(UUID.randomUUID());
        ChatMessage message = scenario.execute(new PostMessageCommand(authorUserId, "Hello world"));

        assertEquals(generatedId, message.getId());
        assertEquals(now, message.getCreatedAt());
        assertSame(message, repository.lastSaved());
    }

    private static final class InMemoryChatMessageRepository implements ChatMessageRepository {
        private ChatMessage saved;

        @Override
        public void save(ChatMessage message) {
            this.saved = message;
        }

        @Override
        public List<ChatMessage> findLastN(int count) {
            return saved == null ? List.of() : List.of(saved);
        }

        private ChatMessage lastSaved() {
            return saved;
        }
    }
}
