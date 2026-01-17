package com.example.backendchatwebsocket.adapters.inbound.rest;

import com.example.backendchat.api.ChatApi;
import com.example.backendchat.api.model.ChatHistoryResponse;
import com.example.backendchat.api.model.ChatMessageResponse;
import com.example.backendchat.api.model.ChatUsersResponse;
import com.example.backendchatwebsocket.application.presence.OnlineUserRegistry;
import com.example.backendchatwebsocket.application.scenario.get_chat_history.GetChatHistoryScenario;
import com.example.backendchatwebsocket.application.scenario.get_chat_history.GetChatHistoryScenarioRequest;
import com.example.backendchatwebsocket.domain.model.ChatMessageWithAuthor;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import com.example.lib.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Service
@RequiredArgsConstructor
public class ChatController implements ChatApi {
    private final GetChatHistoryScenario getChatHistoryScenario;
    private final OnlineUserRegistry onlineUserRegistry;

    @Override
    public ResponseEntity<ChatHistoryResponse> getChatHistory(Integer limit) {
        Result<List<ChatMessageWithAuthor>> result = getChatHistoryScenario.execute(new GetChatHistoryScenarioRequest(limit));
        if (result.isSuccess()) {
            List<ChatMessageWithAuthor> history = result.getOrNull();
            ChatHistoryResponse response = new ChatHistoryResponse();
            response.setMessages(history.stream().map(this::toResponse).toList());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<ChatUsersResponse> getChatUsers() {
        ChatUsersResponse response = new ChatUsersResponse();
        response.setUsers(onlineUserRegistry.listDistinctUsernames());
        return ResponseEntity.ok(response);
    }

    private ChatMessageResponse toResponse(ChatMessageWithAuthor message) {
        return new ChatMessageResponse()
                .id(message.id().value())
                .authorId(message.authorUserId().value())
                .authorName(message.authorName())
                .text(message.text())
                .createdAt(OffsetDateTime.ofInstant(message.createdAt(), ZoneOffset.UTC));
    }
}
