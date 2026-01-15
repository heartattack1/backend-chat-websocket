package com.example.backendchatwebsocket.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class UserId {
    private final UUID value;

    public UserId(UUID value) {
        this.value = Objects.requireNonNull(value, "UserId value cannot be null");
    }

    public UUID value() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UserId userId)) {
            return false;
        }
        return value.equals(userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
