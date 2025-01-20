package ru.yandex.practicum.repository;

import ru.yandex.practicum.domain.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepository {

    List<Comment> getCommentsByPostId(Long postId);

    Map<Long, List<Comment>> getCommentsByPostIdList(List<Long> idList);

    Comment getById(Long id);

    void save(Comment comment);

    Comment update(Comment comment);

    void delete(Long id);

}
