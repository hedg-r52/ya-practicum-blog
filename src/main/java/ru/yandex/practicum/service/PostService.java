package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostShortDto;

public interface PostService {
    Page<PostShortDto> findAll(Pageable pageable);

    Page<PostShortDto> findAllFilteredByTag(String tag, Pageable pageable);

    PostDto getPostById(Long id);

    PostDto save(PostDto post);

    PostDto update(PostDto post);

    void delete(Long id);

    void increaseLikeCount(Long id);
}
