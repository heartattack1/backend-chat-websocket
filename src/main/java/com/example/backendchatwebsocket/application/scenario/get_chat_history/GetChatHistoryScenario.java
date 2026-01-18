package com.example.backendchatwebsocket.application.scenario.get_chat_history;

import com.example.backendchatwebsocket.application.scenario.Scenario;
import com.example.backendchatwebsocket.domain.model.ChatMessageWithAuthor;
import com.example.backendchatwebsocket.domain.repository.ChatMessageRepository;
import com.example.lib.Result;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Возвращает историю чата с ограничением по количеству сообщений.
 *
 * <p>Если лимит не задан или выходит за допустимые границы, применяется значение по умолчанию.
 */
@Service
@RequiredArgsConstructor
public class GetChatHistoryScenario
        implements Scenario<Void, Result<List<ChatMessageWithAuthor>>, GetChatHistoryScenarioRequest> {
    public static final int DEFAULT_LIMIT = 50;
    public static final int MAX_LIMIT = 100;

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public Result<List<ChatMessageWithAuthor>> execute(
            GetChatHistoryScenarioRequest getChatHistoryScenarioRequest) {
        int limit = resolveLimit(getChatHistoryScenarioRequest);
        return Result.success(chatMessageRepository.findLastNWithAuthor(limit));
    }

    private int resolveLimit(GetChatHistoryScenarioRequest getChatHistoryScenarioRequest) {
        if (getChatHistoryScenarioRequest == null || getChatHistoryScenarioRequest.limit() == null) {
            return DEFAULT_LIMIT;
        }
        int limit = getChatHistoryScenarioRequest.limit();
        if (limit < 1) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }
}
