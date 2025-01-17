package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.domain.Tag;
import ru.yandex.practicum.dto.TagDto;

@Component
public class TagMapper {

    public TagDto toTagDto(Tag tag) {
        return new TagDto(
                tag.getPostId(),
                tag.getId(),
                tag.getTag()
        );
    }

}
