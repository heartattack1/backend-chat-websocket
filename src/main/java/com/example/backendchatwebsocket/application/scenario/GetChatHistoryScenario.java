package com.example.backendchatwebsocket.application.scenario;

import com.example.backendchatwebsocket.domain.model.ChatMessage;
import com.example.backendchatwebsocket.domain.repository.ChatMessageRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetChatHistoryScenario implements Scenario<Void, List<ChatMessage>, GetChatHistoryScenario.Request> {
    public static final int DEFAULT_LIMIT = 50;
    public static final int MAX_LIMIT = 100;

    private final ChatMessageRepository chatMessageRepository;

    public GetChatHistoryScenario(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public List<ChatMessage> execute(Request request) {
        int limit = resolveLimit(request);
        return chatMessageRepository.findLastN(limit);
    }

    private int resolveLimit(Request request) {
        if (request == null || request.limit() == null) {
            return DEFAULT_LIMIT;
        }
        int limit = request.limit();
        if (limit < 1) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }

    public record Request(Integer limit) {
    }
}
