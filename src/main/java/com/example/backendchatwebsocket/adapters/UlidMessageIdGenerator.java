package com.example.backendchatwebsocket.adapters;

import com.example.backendchatwebsocket.domain.MessageId;
import com.example.backendchatwebsocket.domain.MessageIdGenerator;
import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Component;

@Component
public class UlidMessageIdGenerator implements MessageIdGenerator {
    @Override
    public MessageId nextId() {
        String ulid = UlidCreator.getMonotonicUlid().toString();
        return new MessageId(ulid);
    }
}
