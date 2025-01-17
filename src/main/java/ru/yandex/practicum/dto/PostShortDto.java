package ru.yandex.practicum.dto;

import java.util.List;

public record PostShortDto(
    Long id,
    String name,
    String image,
    String text,
    int likes,
    int comments,
    String tags
) {}
