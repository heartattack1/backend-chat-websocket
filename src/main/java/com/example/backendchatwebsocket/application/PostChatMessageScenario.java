package com.example.backendchatwebsocket.application;

import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PostChatMessageScenario implements Scenario<Void, ChatMessageEvent, PostChatMessageScenario.Request> {
    private static final int MAX_LENGTH = 1000;
    private final Logger logger = LoggerFactory.getLogger(PostChatMessageScenario.class);

    @Override
    public ChatMessageEvent execute(Request request) {
        String normalizedAuthor = normalizeAuthor(request.author());
        String normalizedText = normalizeText(request.text());
        validateText(normalizedText);

        String id = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        ChatMessageEvent event = new ChatMessageEvent(id, normalizedText, normalizedAuthor, createdAt);
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

    public record Request(String author, String text) {
    }
}
