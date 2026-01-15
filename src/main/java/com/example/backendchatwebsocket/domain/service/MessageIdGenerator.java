package com.example.backendchatwebsocket.domain.service;

import com.example.backendchatwebsocket.domain.model.MessageId;

public interface MessageIdGenerator {
    MessageId nextId();
}
