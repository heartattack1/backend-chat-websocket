package com.example.backendchatwebsocket.application.port;

import java.util.Collection;

/**
 * Рассылает обновления о составе онлайн-пользователей.
 *
 * <p>Реализация должна быть устойчивой к транспортным ошибкам и не блокировать вызывающий поток.
 */
public interface OnlineUsersBroadcaster {
    void broadcast(Collection<String> users);
}
