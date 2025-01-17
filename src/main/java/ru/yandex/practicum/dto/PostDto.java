package ru.yandex.practicum.dto;

import java.util.List;

public record PostDto(
        Long id,
        String name,
        String image,
        String text,
        int likes,
        List<CommentDto> comments,
        List<TagDto> tags
) {}
