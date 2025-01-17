package ru.yandex.practicum.domain;

import java.util.List;

public final class Post {
    private Long id;
    private String name;
    private String image;
    private String content;
    private int likes;
    private List<Tag> tags;
    private List<Comment> comments;

    public Post() {}

    public Post(
            Long id,
            String name,
            String image,
            String content,
            int likes,
            List<Tag> tags,
            List<Comment> comments
    ) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.content = content;
        this.likes = likes;
        this.tags = tags;
        this.comments = comments;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
