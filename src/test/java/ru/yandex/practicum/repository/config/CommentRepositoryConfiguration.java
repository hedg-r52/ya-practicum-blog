package ru.yandex.practicum.repository.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.repository.impl.JdbcNativeCommentRepository;

@Configuration
@Import({JdbcNativeCommentRepository.class})
public class CommentRepositoryConfiguration {
}
