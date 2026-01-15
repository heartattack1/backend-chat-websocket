package com.example.backendchatwebsocket.adapters.inbound.websocket;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(annotations = Controller.class)
public class ChatWebSocketExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ChatWebSocketExceptionHandler.class);

    @MessageExceptionHandler(IllegalArgumentException.class)
    @SendToUser("/queue/errors")
    public ErrorEvent handleValidationError(IllegalArgumentException ex) {
        logger.warn("chat_message_validation_failed reason={}", ex.getMessage());
        // TODO: attach error metadata (request id, session id) and publish metrics.
        return new ErrorEvent(ex.getMessage(), "VALIDATION_ERROR", Instant.now().toString());
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public ErrorEvent handleUnexpectedError(Exception ex) {
        logger.error("chat_message_unexpected_error", ex);
        // TODO: map specific exceptions to error codes and send error frames.
        return new ErrorEvent("Unexpected error", "UNEXPECTED_ERROR", Instant.now().toString());
    }
}
