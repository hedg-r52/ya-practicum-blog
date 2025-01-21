package ru.yandex.practicum.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.domain.Post;
import ru.yandex.practicum.repository.config.RepositoryConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({RepositoryConfiguration.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:test-application.properties")
public class JdbcNativePostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    @Order(1)
    void findAll_shouldReturnAllPosts() {
        List<Post> posts = postRepository.findAll();

        assertEquals(2, posts.size());
        assertEquals("Первый пост", posts.getFirst().getName());
    }

    @Test
    @Order(2)
    void findAllFiltered_shouldReturnFilteredPosts() {
        List<Post> posts = postRepository.findAllFilteredByTag("social");

        assertEquals(1, posts.size());
        assertEquals("Первый пост", posts.getFirst().getName());
    }

    @Test
    @Order(3)
    void getPost_shouldReturnPost() {
        Post post = postRepository.getPost(1L);

        assertNotNull(post);
        assertEquals("Первый пост", post.getName());
    }


    @Test
    @Order(4)
    void deletePost_shouldDeletePost() {
        postRepository.delete(2L);
        List<Post> posts = postRepository.findAll();

        assertEquals(1, posts.size());
    }

    @Test
    @Order(5)
    void savePost_shouldSavePost() {
        Long id = postRepository.save(new Post(-1L, "Saved", "Image", "Content", 1, List.of(), List.of()));
        Post post = postRepository.getPost(id);

        assertNotNull(post);
        assertEquals("Saved", post.getName());
    }

    @Test
    @Order(6)
    void updatePost_shouldUpdatePost() {
        postRepository.update(new Post(1L, "Updated", "Image", "Content", 1, List.of(), List.of()));
        Post post = postRepository.getPost(1L);

        assertNotNull(post);
        assertEquals("Updated", post.getName());
    }

    @Test
    @Order(7)
    void increaseLikesCount() {
        Post post = postRepository.getPost(1L);
        int expected = post.getLikes() + 1;

        postRepository.increaseLikesCount(1L);

        Post updated = postRepository.getPost(1L);
        assertEquals(expected, updated.getLikes());
    }

}
