package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.repository.TagRepository;
import ru.yandex.practicum.service.impl.TagServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = TagServiceImpl.class)
@ActiveProfiles("test")
public class TagServiceTest {
    @Autowired
    private TagService tagService;

    @MockitoBean
    private TagRepository tagRepository;

    @BeforeEach
    public void setUp() {
        reset(tagRepository);
    }

    @Test
    public void whenNewTagsAdd_shouldAddAllNewTags() {
        String tags = "tag1,tag2,tag3";
        List<String> listTags = Arrays.stream(tags.split(",")).map(String::trim).toList();
        when(tagRepository.findAbsentTags(listTags)).thenReturn(listTags);

        tagService.saveTags("tag1,tag2,tag3", 1L);

        verify(tagRepository, times(listTags.size())).save(Mockito.anyString());
        verify(tagRepository, times(1)).findTagsByNames(Mockito.anyList());
        verify(tagRepository, times(1)).removeAllRelationForPost(Mockito.anyLong());
        verify(tagRepository, times(1)).addRelationTagsAndPost(Mockito.anyList(), Mockito.anyLong());
    }

    @Test
    public void whenExistingTagsAdd_shouldNotAddExistingTags() {
        String tags = "tag4,tag5";
        List<String> listTags = Arrays.stream(tags.split(",")).map(String::trim).toList();
        when(tagRepository.findAbsentTags(listTags)).thenReturn(List.of());

        tagService.saveTags(tags, 2L);

        verify(tagRepository, times(0)).save(Mockito.anyString());
        verify(tagRepository, times(1)).findTagsByNames(Mockito.anyList());
        verify(tagRepository, times(1)).removeAllRelationForPost(Mockito.anyLong());
        verify(tagRepository, times(1)).addRelationTagsAndPost(Mockito.anyList(), Mockito.anyLong());
    }
}
