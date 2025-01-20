package ru.yandex.practicum.domain;

public class Comment {
    private Long postId;
    private Long id;
    private String text;

    public Comment() {}

    public Comment(Long postId, Long id, String text) {
        this.postId = postId;
        this.id = id;
        this.text = text;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
