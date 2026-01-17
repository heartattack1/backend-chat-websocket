package com.example.backendchatwebsocket.adapters.inbound.rest;

import com.example.backendchat.api.ChatApi;
import com.example.backendchat.api.model.ChatHistoryResponse;
import com.example.backendchat.api.model.ChatMessageResponse;
import com.example.backendchat.api.model.ChatUsersResponse;
import com.example.backendchatwebsocket.application.presence.OnlineUserRegistry;
import com.example.backendchatwebsocket.application.scenario.GetChatHistoryScenario;
import com.example.backendchatwebsocket.domain.model.ChatMessage;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Service
public class ChatHistoryApiImpl implements ChatApi {
    private final GetChatHistoryScenario getChatHistoryScenario;
    private final OnlineUserRegistry onlineUserRegistry;

    public ChatHistoryApiImpl(GetChatHistoryScenario getChatHistoryScenario, OnlineUserRegistry onlineUserRegistry) {
        this.getChatHistoryScenario = getChatHistoryScenario;
        this.onlineUserRegistry = onlineUserRegistry;
    }

    @Override
    public ResponseEntity<ChatHistoryResponse> getChatHistory(Integer limit) {
        List<ChatMessage> history = getChatHistoryScenario.execute(new GetChatHistoryScenario.Request(limit));
        ChatHistoryResponse response = new ChatHistoryResponse();
        response.setMessages(history.stream().map(this::toResponse).toList());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ChatUsersResponse> getChatUsers() {
        ChatUsersResponse response = new ChatUsersResponse();
        response.setUsers(onlineUserRegistry.listDistinctUsernames());
        return ResponseEntity.ok(response);
    }

    private ChatMessageResponse toResponse(ChatMessage message) {
        ChatMessageResponse response = new ChatMessageResponse();
        response.setId(message.getId().value());
        response.setAuthorId(message.getAuthorUserId().value());
        response.setText(message.getText());
        response.setCreatedAt(OffsetDateTime.ofInstant(message.getCreatedAt(), ZoneOffset.UTC));
        return response;
    }
}
