package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.domain.Tag;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostShortDto;
import ru.yandex.practicum.domain.Post;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    private final int SHORT_POSTS_LENGTH = 250;
    private final CommentMapper commentMapper;

    public PostMapper(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public PostDto toPostDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getName(),
                post.getImage(),
                post.getContent(),
                post.getLikes(),
                post.getComments().stream()
                        .map(commentMapper::toCommentDto)
                        .toList(),
                post.getTags().stream()
                        .map(Tag::getTag)
                        .collect(Collectors.joining(","))
        );
    }

    public PostShortDto toPostShortDto(Post post) {
        List<Tag> tags = post.getTags() == null ? Collections.emptyList() : post.getTags();
        return new PostShortDto(
                post.getId(),
                post.getName(),
                post.getImage(),
                truncateIfHuge(post.getContent()),
                post.getLikes(),
                post.getComments() == null ? 0 : post.getComments().size(),
                tags.stream()
                        .map(Tag::getTag)
                        .collect(Collectors.joining(","))
        );
    }

    public Post toPost(PostDto postDto) {
        return new Post(
                postDto.getId(),
                postDto.getName(),
                postDto.getImage(),
                postDto.getText(),
                postDto.getLikes(),
                List.of(),
                List.of()
        );
    }

    private String truncateIfHuge(String text) {
        if (text.length() > SHORT_POSTS_LENGTH) {
            return text.substring(0, SHORT_POSTS_LENGTH) + "...";
        } else {
            return text;
        }
    }
}
