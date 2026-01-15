package com.example.backendchatwebsocket.adapters.outbound.persistence;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageSpringDataRepository extends JpaRepository<ChatMessageJpaEntity, String> {

    List<ChatMessageJpaEntity> findAllByOrderByCreatedAtDescIdDesc(Pageable pageable);
}
