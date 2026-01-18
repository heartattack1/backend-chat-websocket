package com.example.backendchatwebsocket.domain.repository;

import com.example.backendchatwebsocket.domain.model.User;
import com.example.backendchatwebsocket.domain.model.UserId;
import java.util.Optional;

/**
 * Репозиторий пользователей чата.
 *
 * <p>Реализация должна обеспечивать согласованность результатов поиска по одному {@link UserId}
 * в рамках транзакции.
 */
public interface UserRepository {
    Optional<User> findById(UserId id);

    void save(User user);
}
