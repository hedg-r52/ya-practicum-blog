package ru.yandex.practicum.domain;

public class Tag {
    private Long postId;
    private Long id;
    private String tag;

    public Tag() {}

    public Tag(Long postId, Long id, String tag) {
        this.postId = postId;
        this.id = id;
        this.tag = tag;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
