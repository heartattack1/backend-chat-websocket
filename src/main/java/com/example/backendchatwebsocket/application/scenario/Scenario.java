package com.example.backendchatwebsocket.application.scenario;

public interface Scenario<ERROR, RESULT, VALUE> {
    RESULT execute(VALUE value);
}
