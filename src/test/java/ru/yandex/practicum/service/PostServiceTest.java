package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.domain.Comment;
import ru.yandex.practicum.domain.Post;
import ru.yandex.practicum.domain.Tag;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostShortDto;
import ru.yandex.practicum.mapper.CommentMapperImpl;
import ru.yandex.practicum.mapper.PostMapperImpl;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.impl.PostServiceImpl;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = {PostMapperImpl.class, CommentMapperImpl.class, PostServiceImpl.class})
@ActiveProfiles("test")
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockitoBean
    private PostRepository postRepository;

    @MockitoBean
    private CommentRepository commentRepository;

    @MockitoBean
    private TagRepository tagRepository;

    @Test
    public void whenFindAll_shouldReturnAllPosts() {
        when(postRepository.findAll()).thenReturn(List.of(
                new Post(1L, "Описание 1", "url image 1", "Содержимое 1", 1),
                new Post(2L, "Описание 2", "url image 2", "Содержимое 2", 3)
        ));
        when(commentRepository.getCommentsByPostIdList(anyList())).thenReturn(
                Map.of(
                        1L, List.of(
                                new Comment(1L, 1L, "Коммент 1"),
                                new Comment(1L, 2L, "Коммент 2")
                        ),
                        2L, List.of(
                                new Comment(2L, 3L, "Коммент 3"),
                                new Comment(2L, 4L, "Коммент 4")
                        )
                )
        );
        when(tagRepository.findTagsByPostIdList(anyList())).thenReturn(
                Map.of(
                        1L, List.of(
                                new Tag(1L, 1L, "tag1"),
                                new Tag(1L, 2L, "tag2")
                        ),
                        2L, List.of(
                                new Tag(2L, 2L, "tag2"),
                                new Tag(2L, 3L, "tag3")
                        )
                )
        );

        Page<PostShortDto> posts = postService.findAll(PageRequest.of(0, 10));

        assertEquals(2, posts.getContent().size());
        PostShortDto firstPost = posts.get().findFirst().orElse(null);
        assertNotNull(firstPost);
        assertEquals(1L, firstPost.getId());
        assertEquals("Описание 1", firstPost.getName());
        assertEquals("url image 1", firstPost.getImage());
        assertEquals("Содержимое 1", firstPost.getText());
        assertEquals(1, firstPost.getLikes());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    public void whenFindFilteredByTag_shouldReturnPostsWithThisTag() {
        when(postRepository.findAllFilteredByTag("tag4")).thenReturn(List.of(
                new Post(
                        4L,
                        "Описание 4",
                        "url image 4",
                        "Содержимое 4",
                        4
                )
        ));
        when(commentRepository.getCommentsByPostIdList(anyList())).thenReturn(
                Map.of(
                        4L, List.of(
                                new Comment(4L, 5L, "Коммент 1"),
                                new Comment(4L, 6L, "Коммент 2")
                        )
                )
        );
        when(tagRepository.findTagsByPostIdList(anyList())).thenReturn(
                Map.of(
                        1L, List.of(
                                new Tag(4L, 1L, "tag5"),
                                new Tag(4L, 2L, "tag4")
                        )
                )
        );

        Page<PostShortDto> posts = postService.findAllFilteredByTag("tag4", PageRequest.of(0, 10));

        assertEquals(1, posts.getContent().size());
        PostShortDto firstPost = posts.get().findFirst().orElse(null);
        assertNotNull(firstPost);
        assertEquals(4L, firstPost.getId());
        assertEquals("Описание 4", firstPost.getName());
        assertEquals("url image 4", firstPost.getImage());
        assertEquals("Содержимое 4", firstPost.getText());
        assertEquals(4, firstPost.getLikes());
        verify(postRepository, times(1)).findAllFilteredByTag(anyString());
    }


    @Test
    public void whenGetPostById_shouldReturnPost() {
        when(postRepository.getPost(anyLong())).thenReturn(
                new Post(
                        5L,
                        "Описание 5",
                        "url image 5",
                        "Содержимое 5",
                        5
                ));

        PostDto postDto = postService.getPostById(5L);

        assertNotNull(postDto);
        assertEquals(5L, postDto.getId());
        assertEquals("Описание 5", postDto.getName());
        assertEquals("url image 5", postDto.getImage());
        assertEquals("Содержимое 5", postDto.getText());
        assertEquals(5, postDto.getLikes());
        verify(postRepository, times(1)).getPost(anyLong());
    }


    @Test
    public void whenCreatePost_shouldReturnPost() {
        when(postRepository.save(any(Post.class))).thenReturn(11L);

        PostDto postDto = postService.save(new PostDto(11L, "Name", "Image", "Text", 5, List.of(), "tag1,tag2"));

        assertNotNull(postDto);
        assertEquals(11L, postDto.getId());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void whenUpdatePost_shouldUpdatePost() {
        PostDto updated = postService.update(new PostDto(12L, "Name2", "Image2", "Text2", 7, List.of(), "tag2,tag3"));

        assertNotNull(updated);
        assertEquals(12L, updated.getId());
        verify(postRepository, times(1)).update(any(Post.class));
    }

    @Test
    public void whenDeletePost_shouldDeletePost() {
        postService.delete(13L);

        verify(postRepository, times(1)).delete(anyLong());
    }

    @Test
    public void whenIncreaseLikeCount_shouldBeLikesPlusOne() {
        postService.increaseLikeCount(13L);

        verify(postRepository, times(1)).increaseLikesCount(anyLong());
    }
}
