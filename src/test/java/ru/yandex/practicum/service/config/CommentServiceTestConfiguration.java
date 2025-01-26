package ru.yandex.practicum.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.impl.CommentServiceImpl;

import static org.mockito.Mockito.mock;

@Configuration
public class CommentServiceTestConfiguration {
    @Bean
    public CommentService commentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        return new CommentServiceImpl(commentRepository, commentMapper);
    }

    @Bean
    public CommentRepository commentRepository() {
        return mock(CommentRepository.class);
    }

    @Bean
    public CommentMapper commentMapper() {
        return new CommentMapper();
    }
}
