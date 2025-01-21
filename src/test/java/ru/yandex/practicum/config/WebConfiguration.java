package ru.yandex.practicum.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.yandex.practicum.service.config.CommentServiceTestConfiguration;
import ru.yandex.practicum.service.config.PostServiceTestConfiguration;
import ru.yandex.practicum.service.config.TagServiceTestConfiguration;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ru.yandex.practicum"},
excludeFilters = {@ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = {PostServiceTestConfiguration.class, CommentServiceTestConfiguration.class, TagServiceTestConfiguration.class}
)})
public class WebConfiguration {
}