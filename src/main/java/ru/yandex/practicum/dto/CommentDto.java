package ru.yandex.practicum.dto;

public final class CommentDto {
    private Long parentId;
    private Long id;
    private String text;

    public CommentDto() {}

    public CommentDto(
            Long parentId,
            Long id,
            String text
    ) {
        this.parentId = parentId;
        this.id = id;
        this.text = text;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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
