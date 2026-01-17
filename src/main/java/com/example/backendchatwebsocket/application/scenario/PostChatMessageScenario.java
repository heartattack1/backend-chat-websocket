package com.example.backendchatwebsocket.application.scenario;

import com.example.backendchatwebsocket.application.command.PostMessageCommand;
import com.example.backendchatwebsocket.application.event.ChatMessageEvent;
import com.example.backendchatwebsocket.domain.model.ChatMessage;
import com.example.backendchatwebsocket.domain.model.User;
import com.example.backendchatwebsocket.domain.model.UserId;
import com.example.backendchatwebsocket.domain.repository.UserRepository;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PostChatMessageScenario implements Scenario<Void, ChatMessageEvent, PostChatMessageScenario.Request> {
    private static final int MAX_LENGTH = 1000;
    private final Logger logger = LoggerFactory.getLogger(PostChatMessageScenario.class);
    private final PostMessageScenario postMessageScenario;
    private final UserRepository userRepository;
    private final Clock clock;

    public PostChatMessageScenario(PostMessageScenario postMessageScenario,
                                   UserRepository userRepository,
                                   Clock clock) {
        this.postMessageScenario = postMessageScenario;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    @Override
    public ChatMessageEvent execute(Request request) {
        String normalizedAuthor = normalizeAuthor(request.author());
        String normalizedText = normalizeText(request.text());
        validateText(normalizedText);

        UserId authorUserId = toAuthorUserId(normalizedAuthor);
        ensureUserExists(authorUserId, normalizedAuthor);
        ChatMessage savedMessage = postMessageScenario.execute(new PostMessageCommand(authorUserId, normalizedText));

        ChatMessageEvent event = ChatMessageEvent.from(savedMessage, normalizedAuthor);
        logMessage(event);
        return event;
    }

    private String normalizeAuthor(String author) {
        if (author == null || author.isBlank()) {
            return "anonymous";
        }
        return author.trim();
    }

    private String normalizeText(String text) {
        if (text == null) {
            return null;
        }
        return text.trim();
    }

    private void validateText(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Message text must not be blank");
        }
        if (text.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Message text exceeds maximum length of " + MAX_LENGTH);
        }
    }

    private void logMessage(ChatMessageEvent event) {
        String sanitizedText = event.getText().replaceAll("[\r\n]+", " ");
        logger.info(
                "chat_message_posted author={} id={} length={} text={}",
                event.getAuthor(),
                event.getId(),
                event.getText().length(),
                sanitizedText
        );
    }

    private UserId toAuthorUserId(String author) {
        UUID stableUserId = UUID.nameUUIDFromBytes(author.getBytes(StandardCharsets.UTF_8));
        return new UserId(stableUserId);
    }

    private void ensureUserExists(UserId authorUserId, String author) {
        if (userRepository.findById(authorUserId).isPresent()) {
            return;
        }
        OffsetDateTime now = OffsetDateTime.now(clock);
        User user = new User(
                authorUserId,
                "chat",
                author,
                author,
                null,
                null,
                true,
                now,
                now
        );
        userRepository.save(user);
    }

    public record Request(String author, String text) {
    }
}
