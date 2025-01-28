package ru.yandex.practicum.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.DatabaseHelper;
import ru.yandex.practicum.domain.Comment;
import ru.yandex.practicum.repository.config.RepositoryConfiguration;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({RepositoryConfiguration.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:test-application.properties")
public class JdbcNativeCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private DatabaseHelper databaseHelper;

    @BeforeEach
    void resetDatabase() {
        databaseHelper.resetDatabase();
    }

    @Test
    public void getCommentsByPostId_shouldReturnComments() {
        List<Comment> comments = commentRepository.getCommentsByPostId(1L);

        assertEquals(2, comments.size());
        assertEquals("Коммент #1 к первому посту", comments.getFirst().getText());
    }

    @Test
    public void getCommentsByPostIdList_shouldReturnComments() {
        Map<Long, List<Comment>> comments = commentRepository.getCommentsByPostIdList(List.of(1L, 2L));
        List<String> secondPostComments = comments.get(2L).stream().map(Comment::getText).toList();

        String expected = "Коммент #4 к второму посту";

        assertEquals(2, comments.size());
        assertEquals(2, comments.get(1L).size());
        assertEquals(2, comments.get(2L).size());
        assertTrue(secondPostComments.contains(expected));
    }

    @Test
    public void getById_shouldReturnComment() {
        Comment comment = commentRepository.getById(1L);

        assertNotNull(comment);
        assertEquals("Коммент #1 к первому посту", comment.getText());
    }

    @Test
    public void save_shouldSaveComments() {
        commentRepository.save(new Comment(1L, null, "Test"));
        List<Comment> comments = commentRepository.getCommentsByPostId(1L);
        List<String> strComments = comments.stream().map(Comment::getText).toList();
        assertEquals(3, comments.size());
        assertTrue(strComments.contains("Test"));
    }

    @Test
    public void update_shouldUpdateComments() {
        commentRepository.update(new Comment(1L, 2L, "Updated"));

        List<Comment> comments = commentRepository.getCommentsByPostId(1L);
        List<String> strComments = comments.stream().map(Comment::getText).toList();
        assertTrue(strComments.contains("Updated"));
    }

    @Test
    public void delete_shouldDeleteComments() {
        commentRepository.delete(3L);
        List<Comment> comments = commentRepository.getCommentsByPostId(2L);

        assertEquals(1, comments.size());
    }
}
