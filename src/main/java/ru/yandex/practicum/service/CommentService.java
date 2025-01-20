package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findByPostId(Long postId);

    CommentDto findById(Long id);

    CommentDto save(CommentDto comment);

    CommentDto update(CommentDto comment);

    void delete(Long id);

}
