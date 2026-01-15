package com.example.backendchatwebsocket.application.scenario;

import com.example.backendchatwebsocket.application.command.PostMessageCommand;
import com.example.backendchatwebsocket.domain.model.ChatMessage;
import com.example.backendchatwebsocket.domain.model.MessageId;
import com.example.backendchatwebsocket.domain.repository.ChatMessageRepository;
import com.example.backendchatwebsocket.domain.service.MessageIdGenerator;
import java.time.Clock;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class PostMessageScenario implements Scenario<Void, ChatMessage, PostMessageCommand> {
    private final ChatMessageRepository chatMessageRepository;
    private final MessageIdGenerator messageIdGenerator;
    private final Clock clock;

    public PostMessageScenario(ChatMessageRepository chatMessageRepository,
                               MessageIdGenerator messageIdGenerator,
                               Clock clock) {
        this.chatMessageRepository = chatMessageRepository;
        this.messageIdGenerator = messageIdGenerator;
        this.clock = clock;
    }

    @Override
    public ChatMessage execute(PostMessageCommand command) {
        MessageId id = messageIdGenerator.nextId();
        Instant now = Instant.now(clock);
        ChatMessage message = ChatMessage.post(id, command.authorUserId(), command.text(), now);
        chatMessageRepository.save(message);
        return message;
    }
}
