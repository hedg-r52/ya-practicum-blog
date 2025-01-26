package ru.yandex.practicum.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.impl.PostServiceImpl;

import static org.mockito.Mockito.mock;

@Configuration
@Import({CommentServiceTestConfiguration.class, TagServiceTestConfiguration.class})
public class PostServiceTestConfiguration {

    @Bean
    public PostService postService(PostRepository postRepository, CommentRepository commentRepository, TagRepository tagRepository, PostMapper postMapper) {
        return new PostServiceImpl(postRepository, commentRepository, tagRepository, postMapper);
    }

    @Bean
    public PostRepository postRepository() {
        return mock(PostRepository.class);
    }

    @Bean
    public PostMapper postMapper(CommentMapper commentMapper) {
        return new PostMapper(commentMapper);
    }
}
