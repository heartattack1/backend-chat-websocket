package com.example.backendchatwebsocket.application;

import com.example.backendchatwebsocket.domain.UserId;

public record PostMessageCommand(UserId authorUserId, String text) {
}
