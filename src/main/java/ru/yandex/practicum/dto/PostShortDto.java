package ru.yandex.practicum.dto;

public record PostShortDto(
    Long id,
    String name,
    String image,
    String text,
    int likes,
    int comments,
    String tags
) {}
