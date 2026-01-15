package com.example.backendchatwebsocket.domain.model;

import java.util.Objects;

public final class MessageId {
    public static final int ULID_LENGTH = 26;

    private final String value;

    public MessageId(String value) {
        if (value == null) {
            throw new IllegalArgumentException("MessageId value cannot be null");
        }
        if (value.isBlank()) {
            throw new IllegalArgumentException("MessageId value cannot be blank");
        }
        if (value.length() != ULID_LENGTH) {
            throw new IllegalArgumentException("MessageId value must be 26 characters");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MessageId messageId)) {
            return false;
        }
        return value.equals(messageId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
