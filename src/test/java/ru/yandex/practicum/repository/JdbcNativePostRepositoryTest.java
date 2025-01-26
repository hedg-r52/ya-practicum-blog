package ru.yandex.practicum.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.domain.Post;
import ru.yandex.practicum.repository.config.RepositoryConfiguration;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({RepositoryConfiguration.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:test-application.properties")
public class JdbcNativePostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void resetDatabase() {
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

    @Test
    void findAll_shouldReturnAllPosts() {
        List<Post> posts = postRepository.findAll();

        assertEquals(2, posts.size());
        assertEquals("Первый пост", posts.getFirst().getName());
    }

    @Test
    void findAllFiltered_shouldReturnFilteredPosts() {
        List<Post> posts = postRepository.findAllFilteredByTag("social");

        assertEquals(1, posts.size());
        assertEquals("Первый пост", posts.getFirst().getName());
    }

    @Test
    void getPost_shouldReturnPost() {
        Post post = postRepository.getPost(1L);

        assertNotNull(post);
        assertEquals("Первый пост", post.getName());
    }


    @Test
    void deletePost_shouldDeletePost() {
        postRepository.delete(2L);
        List<Post> posts = postRepository.findAll();

        assertEquals(1, posts.size());
    }

    @Test
    void savePost_shouldSavePost() {
        Long id = postRepository.save(new Post(-1L, "Saved", "Image", "Content", 1));
        Post post = postRepository.getPost(id);

        assertNotNull(post);
        assertEquals("Saved", post.getName());
    }

    @Test
    void updatePost_shouldUpdatePost() {
        postRepository.update(new Post(1L, "Updated", "Image", "Content", 1));
        Post post = postRepository.getPost(1L);

        assertNotNull(post);
        assertEquals("Updated", post.getName());
    }

    @Test
    void increaseLikesCount() {
        Post post = postRepository.getPost(1L);
        int expected = post.getLikes() + 1;

        postRepository.increaseLikesCount(1L);

        Post updated = postRepository.getPost(1L);
        assertEquals(expected, updated.getLikes());
    }

}
