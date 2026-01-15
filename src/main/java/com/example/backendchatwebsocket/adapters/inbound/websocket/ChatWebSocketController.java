package com.example.backendchatwebsocket.adapters.inbound.websocket;

import com.example.backendchatwebsocket.application.event.ChatMessageEvent;
import com.example.backendchatwebsocket.application.port.ChatBroadcaster;
import com.example.backendchatwebsocket.application.scenario.PostChatMessageScenario;
import java.security.Principal;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {
    private final PostChatMessageScenario postChatMessageScenario;
    private final ChatBroadcaster chatBroadcaster;

    public ChatWebSocketController(PostChatMessageScenario postChatMessageScenario,
                                   ChatBroadcaster chatBroadcaster) {
        this.postChatMessageScenario = postChatMessageScenario;
        this.chatBroadcaster = chatBroadcaster;
    }

    @MessageMapping("/chat.send")
    public void handleChatSend(ChatSendMessageRequest request,
                               Principal principal,
                               @Header(name = "author", required = false) String authorHeader) {
        String author = resolveAuthor(principal, authorHeader);
        String text = request == null ? null : request.getText();
        ChatMessageEvent event = postChatMessageScenario.execute(new PostChatMessageScenario.Request(author, text));
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
