package com.example.backendchatwebsocket.application.port;

import java.util.Collection;

public interface OnlineUsersBroadcaster {
    void broadcast(Collection<String> users);
}
