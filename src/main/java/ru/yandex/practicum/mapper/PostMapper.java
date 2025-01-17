package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.domain.Tag;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostShortDto;
import ru.yandex.practicum.domain.Post;

import java.util.stream.Collectors;

@Component
public class PostMapper {

    private final int SHORT_POSTS_LENGTH = 250;
    private final CommentMapper commentMapper;
    private final TagMapper tagMapper;

    public PostMapper(CommentMapper commentMapper, TagMapper tagMapper) {
        this.commentMapper = commentMapper;
        this.tagMapper = tagMapper;
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
                        .map(tagMapper::toTagDto)
                        .toList()
        );
    }

    public PostShortDto toPostShortDto(Post post) {
        return new PostShortDto(
                post.getId(),
                post.getName(),
                post.getImage(),
                post.getContent().substring(0, SHORT_POSTS_LENGTH) + "...",
                post.getLikes(),
                post.getComments().size(),
                post.getTags().stream()
                        .map(Tag::getTag)
                        .collect(Collectors.joining(", "))
        );
    }

}
