package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.domain.Post;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostShortDto;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "text", source = "post.content")
    PostDto toPostDto(Post post);

    @Mapping(target = "content", source = "postDto.text")
    Post toPost(PostDto postDto);

    @Mapping(target = "text", source = "post.content")
    PostShortDto toPostShortDto(Post post);

}
