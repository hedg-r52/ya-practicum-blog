package ru.yandex.practicum.service;

import ru.yandex.practicum.domain.Comment;
import ru.yandex.practicum.dto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findByPostId(Long postId);

    CommentDto findById(Long id);

    CommentDto save(Comment comment);

    CommentDto update(Comment comment);

    void delete(Comment comment);

}
