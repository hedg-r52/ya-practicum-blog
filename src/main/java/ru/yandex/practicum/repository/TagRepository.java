package ru.yandex.practicum.repository;

import ru.yandex.practicum.domain.Tag;

import java.util.List;
import java.util.Map;

public interface TagRepository {

    List<Tag> findTagsByPostId(Long postId);

    Map<Long, List<Tag>> findTagsByPostIdList(List<Long> idList);

    void save(Tag tag);

    void delete(Long id);

}
