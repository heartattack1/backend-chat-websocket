package com.example.backendchatwebsocket.adapters.inbound.websocket;

import com.example.backendchatwebsocket.application.ChatBroadcaster;
import com.example.backendchatwebsocket.application.ChatMessageEvent;
import com.example.backendchatwebsocket.application.PostChatMessageUseCase;
import java.security.Principal;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {
    private final PostChatMessageUseCase postChatMessageUseCase;
    private final ChatBroadcaster chatBroadcaster;

    public ChatWebSocketController(PostChatMessageUseCase postChatMessageUseCase,
                                   ChatBroadcaster chatBroadcaster) {
        this.postChatMessageUseCase = postChatMessageUseCase;
        this.chatBroadcaster = chatBroadcaster;
    }

    @MessageMapping("/chat.send")
    public void handleChatSend(ChatSendMessageRequest request,
                               Principal principal,
                               @Header(name = "author", required = false) String authorHeader) {
        String author = resolveAuthor(principal, authorHeader);
        ChatMessageEvent event = postChatMessageUseCase.handle(author, request == null ? null : request.getText());
        chatBroadcaster.broadcast(event);
    }

    private String resolveAuthor(Principal principal, String authorHeader) {
        if (authorHeader != null && !authorHeader.isBlank()) {
            return authorHeader;
        }
        if (principal != null && principal.getName() != null && !principal.getName().isBlank()) {
            return principal.getName();
        }
        return "anonymous";
    }
}
