package ru.yandex.practicum.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class Post {
    private Long id;
    private String name;
    private String image;
    private String content;
    private int likes;
    private List<Tag> tags;
    private List<Comment> comments;
}
