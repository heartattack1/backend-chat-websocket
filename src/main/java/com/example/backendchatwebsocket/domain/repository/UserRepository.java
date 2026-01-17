package com.example.backendchatwebsocket.domain.repository;

import com.example.backendchatwebsocket.domain.model.User;
import com.example.backendchatwebsocket.domain.model.UserId;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(UserId id);

    void save(User user);
}
