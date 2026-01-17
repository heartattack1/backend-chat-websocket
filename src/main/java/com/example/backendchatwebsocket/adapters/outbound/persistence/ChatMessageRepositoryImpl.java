package com.example.backendchatwebsocket.adapters.outbound.persistence;

import com.example.backendchatwebsocket.domain.model.ChatMessage;
import com.example.backendchatwebsocket.domain.model.ChatMessageWithAuthor;
import com.example.backendchatwebsocket.domain.model.MessageId;
import com.example.backendchatwebsocket.domain.model.UserId;
import com.example.backendchatwebsocket.domain.repository.ChatMessageRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    private final ChatMessageJpaRepository repository;

    @Override
    public void save(ChatMessage message) {
        repository.save(this.toEntity(message));
    }

    @Override
    public List<ChatMessage> findLastN(int count) {
        Pageable pageable = PageRequest.of(0, count);
        List<ChatMessageEntity> entities = repository.findAllByOrderByCreatedAtDescIdDesc(pageable);
        return entities.stream()
                .map(this::toDomain)
                .sorted(Comparator.comparing(ChatMessage::createdAt).thenComparing(message -> message.id().value()))
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

    ChatMessageEntity toEntity(ChatMessage message) {
        return new ChatMessageEntity(
                message.id().value(),
                message.authorUserId().value(),
                message.text(),
                message.createdAt());
    }

    ChatMessage toDomain(ChatMessageEntity entity) {
        return ChatMessage.post(
                new MessageId(entity.getId()),
                new UserId(entity.getAuthorUserId()),
                entity.getText(),
                entity.getCreatedAt());
    }
}
