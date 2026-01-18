package com.example.backendchatwebsocket.application.validation;

import org.springframework.stereotype.Component;

@Component
public class MessageTextValidator {
    private static final int MAX_LENGTH = 1000;

    public void validate(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Message text must not be blank");
        }
        if (text.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Message text exceeds maximum length of " + MAX_LENGTH);
        }
    }
}
