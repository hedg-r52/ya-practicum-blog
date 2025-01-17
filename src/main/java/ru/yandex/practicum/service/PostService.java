package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostShortDto;
import ru.yandex.practicum.domain.Post;

import java.util.List;

public interface PostService {
    Page<PostShortDto> findAll(Pageable pageable);

    List<PostShortDto> findAllFilteredByTag(String tag, Long limit, Long offset);

    PostDto getPostById(Long id);

    PostDto save(Post post);

    PostDto update(Post post);

    void delete(Post post);

    void delete(Long id);
}
