package com.example.backendchatwebsocket.domain.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MessageIdTest {

    @Test
    void rejectsNullValue() {
        assertThrows(IllegalArgumentException.class, () -> new MessageId(null));
    }

    @Test
    void rejectsBlankValue() {
        assertThrows(IllegalArgumentException.class, () -> new MessageId("   "));
    }

    @Test
    void rejectsWrongLength() {
        assertThrows(IllegalArgumentException.class, () -> new MessageId("short"));
    }

    @Test
    void acceptsValidLength() {
        assertDoesNotThrow(() -> new MessageId("01J2M5Z8V6H4C4B9QF6XH1T2Z3"));
    }
}
