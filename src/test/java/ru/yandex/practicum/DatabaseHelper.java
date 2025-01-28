package ru.yandex.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DatabaseHelper {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public void resetDatabase() {
        jdbcTemplate.update("DROP ALL OBJECTS", Map.of());
        jdbcTemplate.update("RUNSCRIPT FROM 'classpath:test-schema.sql'", Map.of());

        jdbcTemplate.update("""
                INSERT INTO posts(name, image, content, likes)
                VALUES (
                       'Первый пост',
                       'https://i.pinimg.com/736x/87/2b/4b/872b4bba4d43e54bce4a5bdf07aa5c99.jpg',
                       'Содержимое 1',
                       1)
                """, Map.of());
        jdbcTemplate.update("""
                INSERT INTO posts(name, image, content, likes)
                VALUES (
                        'Второй пост',
                        'https://i.pinimg.com/736x/5f/13/95/5f13958c5d0f633095cef10abec15b85.jpg',
                        'Содержимое 2',
                        2)
                """, Map.of());

        jdbcTemplate.update("INSERT INTO comments(post_id, text) VALUES (1, 'Коммент #1 к первому посту');", Map.of());
        jdbcTemplate.update("INSERT INTO comments(post_id, text) VALUES (1, 'Коммент #2 к первому посту');", Map.of());
        jdbcTemplate.update("INSERT INTO comments(post_id, text) VALUES (2, 'Коммент #3 к второму посту');", Map.of());
        jdbcTemplate.update("INSERT INTO comments(post_id, text) VALUES (2, 'Коммент #4 к второму посту');", Map.of());

        jdbcTemplate.update("INSERT INTO tags(tag) VALUES ('social');" , Map.of());
        jdbcTemplate.update("INSERT INTO tags(tag) VALUES ('auto');" , Map.of());
        jdbcTemplate.update("INSERT INTO tags(tag) VALUES ('it');" , Map.of());
        jdbcTemplate.update("INSERT INTO tags(tag) VALUES ('hobby');" , Map.of());

        jdbcTemplate.update("INSERT INTO post_tags(post_id, tag_id) VALUES (1, 1);", Map.of());
        jdbcTemplate.update("INSERT INTO post_tags(post_id, tag_id) VALUES (1, 2);", Map.of());
        jdbcTemplate.update("INSERT INTO post_tags(post_id, tag_id) VALUES (1, 3);", Map.of());
        jdbcTemplate.update("INSERT INTO post_tags(post_id, tag_id) VALUES (2, 2);", Map.of());
        jdbcTemplate.update("INSERT INTO post_tags(post_id, tag_id) VALUES (2, 3);", Map.of());
        jdbcTemplate.update("INSERT INTO post_tags(post_id, tag_id) VALUES (2, 4);", Map.of());
    }
}
