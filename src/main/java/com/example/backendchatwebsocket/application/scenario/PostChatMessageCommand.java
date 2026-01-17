package com.example.backendchatwebsocket.application.scenario;

public record PostChatMessageCommand(String author, String text) {
}