package com.example.backendchatwebsocket.application;

public interface Scenario<ERROR, RESULT, VALUE> {
    RESULT execute(VALUE value);
}
