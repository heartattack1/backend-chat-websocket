package com.example.backendchatwebsocket.application.port;

import com.example.backendchatwebsocket.application.event.ChatMessageEvent;

/**
 * Рассылает события сообщений чата подключённым клиентам.
 *
 * <p>Реализация не должна блокировать поток сценария и должна быть устойчива к сбоям доставки.
 */
public interface ChatBroadcaster {
    void broadcast(ChatMessageEvent event);
}
