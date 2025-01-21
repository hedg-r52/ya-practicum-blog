package ru.yandex.practicum.repository.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.repository.impl.JdbcNativeTagRepository;

@Configuration
@Import({JdbcNativeTagRepository.class})
public class TagRepositoryConfiguration {
}
