package ru.yandex.practicum.repository.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.DatabaseHelper;
import ru.yandex.practicum.config.DataSourceConfiguration;

@Configuration
@Import({
        DataSourceConfiguration.class,
        TagRepositoryConfiguration.class,
        CommentRepositoryConfiguration.class,
        PostRepositoryConfiguration.class,
        DatabaseHelper.class
})
public class RepositoryConfiguration {
}
