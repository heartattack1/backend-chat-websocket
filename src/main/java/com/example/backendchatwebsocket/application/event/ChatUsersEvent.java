package com.example.backendchatwebsocket.application.event;

import java.util.List;

public record ChatUsersEvent(List<String> users) {}
