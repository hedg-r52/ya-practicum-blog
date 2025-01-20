package ru.yandex.practicum.repository;

import ru.yandex.practicum.domain.Tag;

import java.util.List;
import java.util.Map;

public interface TagRepository {

    List<Tag> findTagsByPostId(Long postId);

    Map<Long, List<Tag>> findTagsByPostIdList(List<Long> idList);

    List<Tag> findTagsByNames(List<String> tagNames);

    void addRelationTagsAndPost(List<Tag> tagsList, Long postId);

    void save(String tag);

    List<String> findAbsentTags(List<String> tagNames);

}
