package com.example.backendchatwebsocket.adapters;

import com.example.backendchatwebsocket.domain.ChatMessage;
import com.example.backendchatwebsocket.domain.ChatMessageRepository;
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
}
