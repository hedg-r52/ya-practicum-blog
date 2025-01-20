package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.domain.Comment;

@Component
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getPostId(),
                comment.getId(),
                comment.getText()
        );
    }

    public Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setPostId(commentDto.getPostId());
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        return comment;
    }
}
