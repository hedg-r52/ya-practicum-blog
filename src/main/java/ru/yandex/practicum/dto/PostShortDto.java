package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostShortDto {
    private Long id;
    private String name;
    private String image;
    private String text;
    private int likes;
    private int comments;
    private String tags;
}
