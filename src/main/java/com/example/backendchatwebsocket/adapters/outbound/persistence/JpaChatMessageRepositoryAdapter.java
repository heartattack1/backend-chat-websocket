package com.example.backendchatwebsocket.adapters.outbound.persistence;

import com.example.backendchatwebsocket.domain.model.ChatMessage;
import com.example.backendchatwebsocket.domain.model.ChatMessageWithAuthor;
import com.example.backendchatwebsocket.domain.model.MessageId;
import com.example.backendchatwebsocket.domain.model.UserId;
import com.example.backendchatwebsocket.domain.repository.ChatMessageRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class JpaChatMessageRepositoryAdapter implements ChatMessageRepository {

    private final ChatMessageSpringDataRepository repository;
    private final ChatMessageJpaMapper mapper;

    public JpaChatMessageRepositoryAdapter(ChatMessageSpringDataRepository repository) {
        this.repository = repository;
        this.mapper = new ChatMessageJpaMapper();
    }

    @Override
    public void save(ChatMessage message) {
        repository.save(mapper.toEntity(message));
    }

    @Override
    public List<ChatMessage> findLastN(int count) {
        Pageable pageable = PageRequest.of(0, count);
        List<ChatMessageJpaEntity> entities = repository.findAllByOrderByCreatedAtDescIdDesc(pageable);
        return entities.stream()
                .map(mapper::toDomain)
                .sorted(Comparator.comparing(ChatMessage::getCreatedAt).thenComparing(message -> message.getId().value()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatMessageWithAuthor> findLastNWithAuthor(int count) {
        Pageable pageable = PageRequest.of(0, count);
        List<ChatMessageWithAuthorProjection> entities = repository.findAllWithAuthorByOrderByCreatedAtDescIdDesc(pageable);
        return entities.stream()
                .map(entity -> new ChatMessageWithAuthor(
                        new MessageId(entity.getId()),
                        new UserId(entity.getAuthorUserId()),
                        entity.getAuthorName(),
                        entity.getText(),
                        entity.getCreatedAt()))
                .sorted(Comparator.comparing(ChatMessageWithAuthor::createdAt)
                        .thenComparing(message -> message.id().value()))
                .collect(Collectors.toList());
    }
}
