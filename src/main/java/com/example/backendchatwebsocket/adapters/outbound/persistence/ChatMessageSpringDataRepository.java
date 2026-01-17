package com.example.backendchatwebsocket.adapters.outbound.persistence;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMessageSpringDataRepository extends JpaRepository<ChatMessageJpaEntity, String> {

    List<ChatMessageJpaEntity> findAllByOrderByCreatedAtDescIdDesc(Pageable pageable);

    @Query("""
            select m.id as id,
                   m.authorUserId as authorUserId,
                   u.displayName as authorName,
                   m.text as text,
                   m.createdAt as createdAt
            from ChatMessageJpaEntity m
            join UserEntity u on u.id = m.authorUserId
            order by m.createdAt desc, m.id desc
            """)
    List<ChatMessageWithAuthorProjection> findAllWithAuthorByOrderByCreatedAtDescIdDesc(Pageable pageable);
}
