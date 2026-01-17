package com.example.backendchatwebsocket.adapters.outbound.persistence;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, String> {

    List<ChatMessageEntity> findAllByOrderByCreatedAtDescIdDesc(Pageable pageable);

    @Query("""
            select m.id as id,
                   m.authorUserId as authorUserId,
                   u.displayName as authorName,
                   m.text as text,
                   m.createdAt as createdAt
            from ChatMessageEntity m
            join UserEntity u on u.id = m.authorUserId
            order by m.createdAt desc, m.id desc
            """)
    List<ChatMessageWithAuthorProjection> findAllWithAuthorByOrderByCreatedAtDescIdDesc(Pageable pageable);
}
