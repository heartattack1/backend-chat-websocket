package com.example.backendchatwebsocket.application.command;

import com.example.backendchatwebsocket.domain.model.UserId;

public record PostMessageCommand(UserId authorUserId, String text) {
}
