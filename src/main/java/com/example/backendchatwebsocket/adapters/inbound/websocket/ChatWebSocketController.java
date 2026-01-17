package com.example.backendchatwebsocket.adapters.inbound.websocket;

import com.example.backendchatwebsocket.application.event.ChatMessageEvent;
import com.example.backendchatwebsocket.application.port.ChatBroadcaster;
import com.example.backendchatwebsocket.application.scenario.PostChatMessageCommand;
import com.example.backendchatwebsocket.application.scenario.PostChatMessageScenario;

import java.security.Principal;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final PostChatMessageScenario postChatMessageScenario;
    private final ChatBroadcaster chatBroadcaster;

    @MessageMapping("/chat.send")
    public void handleChatSend(ChatSendMessageRequest request,
                               Principal principal,
                               @Header(name = "author", required = false) String authorHeader) {
        String author = resolveAuthor(principal, authorHeader);
        String text = request == null ? null : request.getText();
        ChatMessageEvent event = postChatMessageScenario.execute(new PostChatMessageCommand(author, text));
        chatBroadcaster.broadcast(event);
    }

    private String resolveAuthor(Principal principal, String authorHeader) {
        if (authorHeader != null && !authorHeader.isBlank()) {
            return authorHeader;
        }
        if (principal != null && StringUtils.isNotBlank(principal.getName())) {
            return principal.getName();
        }
        return "anonymous";
    }
}
