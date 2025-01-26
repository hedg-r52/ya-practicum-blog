package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.domain.Comment;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.service.config.CommentServiceTestConfiguration;
import ru.yandex.practicum.service.impl.CommentServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = {CommentServiceTestConfiguration.class, CommentServiceImpl.class})
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    public void setUp() {
        reset(commentRepository);
    }

    @Test
    public void whenFindByPostId_shouldReturnListOfCommentDto() {
        when(commentRepository.getCommentsByPostId(1L)).thenReturn(
                List.of(
                        new Comment(1L, 1L, "Первый коммент"),
                        new Comment(1L, 2L, "Второй коммент"),
                        new Comment(1L, 3L, "Третий коммент")
                )
        );

        List<CommentDto> result = commentService.findByPostId(1L);

        assertEquals(3, result.size());
        assertEquals(1L, result.getFirst().getPostId());
        assertEquals(1L, result.getFirst().getId());
        assertEquals("Первый коммент", result.getFirst().getText());
        assertInstanceOf(CommentDto.class, result.getFirst());
        verify(commentRepository, times(1)).getCommentsByPostId(1L);
    }

    @Test
    public void whenFindById_shouldReturnCommentDto() {
        when(commentRepository.getById(2L)).thenReturn(
                new Comment(2L, 4L, "Четвертый коммент")
        );

        CommentDto commentDto = commentService.findById(2L);

        assertEquals(2L, commentDto.getPostId());
        assertEquals(4L, commentDto.getId());
        assertEquals("Четвертый коммент", commentDto.getText());
        assertInstanceOf(CommentDto.class, commentDto);

        verify(commentRepository, times(0)).getById(1L);
        verify(commentRepository, times(1)).getById(2L);

    }

    @Test
    public void whenSaveComment_shouldInvokeRepositorySaveMethodOnce() {
        CommentDto commentDto = new CommentDto(3L, 5L, "Пятый коммент");
        commentService.save(commentDto);

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void whenUpdate_shouldInvokeRepositoryUpdateMethodOnce() {
        CommentDto commentDto = new CommentDto(4L, 6L, "Шестой коммент");
        CommentDto updated = commentService.update(commentDto);

        assertEquals(4L, updated.getPostId());
        assertEquals(6L, updated.getId());
        assertEquals("Шестой коммент", updated.getText());
        verify(commentRepository, times(1)).update(any(Comment.class));
    }

    @Test
    public void whenDelete() {
        commentService.delete(7L);

        verify(commentRepository, times(1)).delete(7L);
    }

}
