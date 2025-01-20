package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.domain.Tag;
import ru.yandex.practicum.repository.TagRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public void saveTags(String tags, Long postId) {
        List<String> tagsList = Arrays.stream(tags.split(",")).map(String::trim).toList();
        List<String> tagsForAdding = tagRepository.findAbsentTags(tagsList);
        tagsForAdding.forEach(tagRepository::save);
        List<Tag> tagsByNames = tagRepository.findTagsByNames(tagsList);
        tagRepository.removeAllRelationForPost(postId);
        tagRepository.addRelationTagsAndPost(tagsByNames, postId);
    }
}
