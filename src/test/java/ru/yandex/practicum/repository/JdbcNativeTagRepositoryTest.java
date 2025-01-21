package ru.yandex.practicum.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.domain.Tag;
import ru.yandex.practicum.repository.config.RepositoryConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({RepositoryConfiguration.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:test-application.properties")
public class JdbcNativeTagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    @Order(1)
    void findTagsByPostId_shouldReturnPostTags() {
        List<String> expected = List.of("social", "auto", "it");

        List<Tag> tags = tagRepository.findTagsByPostId(1L);

        List<String> stringTags = tags.stream()
                .map(Tag::getTag)
                .toList();
        assertEquals(3, tags.size());
        assertIterableEquals(expected, stringTags);
    }

    @Test
    @Order(2)
    void findTagsByPostIdList_shouldReturnPostTags() {
        List<String> firstExpected = List.of("social", "auto", "it");
        List<String> secondExpected = List.of("auto", "it", "hobby");
        List<Long> ids = List.of(1L, 2L);

        Map<Long, List<Tag>> tagsMap = tagRepository.findTagsByPostIdList(ids);

        List<String> firstPostTags = tagsMap.get(1L).stream()
                .map(Tag::getTag)
                .toList();
        List<String> secondPostTags = tagsMap.get(2L).stream()
                .map(Tag::getTag)
                .toList();

        assertEquals(3, firstExpected.size());
        assertEquals(3, secondExpected.size());
        assertIterableEquals(firstExpected, firstPostTags);
        assertIterableEquals(secondExpected, secondPostTags);
    }

    @Test
    @Order(3)
    void findTagsByNames_shouldReturnPostTags() {
        List<String> expected = List.of("social", "auto", "it");
        List<Tag> tags = tagRepository.findTagsByNames(List.of("social", "auto", "it"));

        List<String> stringTags = tags.stream()
                .map(Tag::getTag)
                .toList();

        assertEquals(3, tags.size());
        assertIterableEquals(expected, stringTags);
    }

    @Test
    @Order(4)
    void addRelationTagsAndPost_shouldReturnPostTags() {
        List<Tag> tags = tagRepository.findTagsByNames(List.of("social"));

        tagRepository.addRelationTagsAndPost(tags, 2L);

        List<Tag> tagsOfSecondPost = tagRepository.findTagsByPostId(2L);
        Set<String> stringTags = tagsOfSecondPost.stream()
                .map(Tag::getTag)
                .collect(Collectors.toSet());

        assertEquals(4, tagsOfSecondPost.size());
        assertIterableEquals(Set.of("social", "auto", "it", "hobby"), stringTags);
    }

    @Test
    @Order(4)
    void findAbsentTags_shouldReturnAbsentTags() {
        List<String> input = List.of("social", "auto", "it", "travel", "jokes");
        Set<String> expected = Set.of("travel", "jokes");

        Set<String> absentTags = new HashSet<>(tagRepository.findAbsentTags(input));
        assertEquals(2, absentTags.size());
        assertIterableEquals(expected, absentTags);
    }

    @Test
    @Order(5)
    void save_shouldSucceed() {
        tagRepository.save("science");
        List<Tag> foundTags = tagRepository.findTagsByNames(List.of("science"));

        assertEquals(1, foundTags.size());
        assertEquals("science", foundTags.getFirst().getTag());
    }

    @Test
    @Order(6)
    void removeAllRelationTags_shouldSucceed() {
        tagRepository.removeAllRelationForPost(2L);
        List<Tag> foundTags = tagRepository.findTagsByPostId(2L);

        assertEquals(0, foundTags.size());
    }
}
