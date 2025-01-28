package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.domain.Post;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostShortDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    int SHORT_POSTS_LENGTH = 250;

    @Mapping(target = "text", source = "post.content")
    PostDto toPostDto(Post post);

    @Mapping(target = "content", source = "postDto.text")
    Post toPost(PostDto postDto);

    @Mapping(target = "text", expression = "java(truncateIfHuge(post.getContent()))")
    PostShortDto toPostShortDto(Post post);

    default String truncateIfHuge(String text) {
        if (text.length() > SHORT_POSTS_LENGTH) {
            return text.substring(0, SHORT_POSTS_LENGTH) + "...";
        } else {
            return text;
        }
    }
}
