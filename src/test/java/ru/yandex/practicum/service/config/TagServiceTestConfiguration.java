package ru.yandex.practicum.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.TagService;
import ru.yandex.practicum.service.TagServiceImpl;

import static org.mockito.Mockito.mock;

@Configuration
public class TagServiceTestConfiguration {
    @Bean
    public TagService tagService(TagRepository tagRepository) {
        return new TagServiceImpl(tagRepository);
    }

    @Bean
    public TagRepository tagRepository() {
        return mock(TagRepository.class);
    }
}
