package ru.yandex.practicum.dto;

import java.util.List;

public final class PostDto {
    private Long id;
    private String name;
    private String image;
    private String text;
    private int likes;
    private List<CommentDto> comments;
    private String tags;

    public PostDto() {}

    public PostDto(
            Long id,
            String name,
            String image,
            String text,
            int likes,
            List<CommentDto> comments,
            String tags
    ) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.text = text;
        this.likes = likes;
        this.comments = comments;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
