package ru.yandex.practicum.repository;

import ru.yandex.practicum.domain.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findAll();

    List<Post> findAllFilteredByTag(String tag, Long limit, Long offset);

    Post getPost(Long id);

    void save(Post post);

    Post update(Post post);

    void delete(Long id);
}
