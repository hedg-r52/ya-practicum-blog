package ru.yandex.practicum.dto;

public final class CommentDto {
    private  Long id;
    private  String text;

    public CommentDto() {}

    public CommentDto(
            Long id,
            String text
    ) {
        this.id = id;
        this.text = text;
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
