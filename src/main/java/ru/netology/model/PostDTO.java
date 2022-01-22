package ru.netology.model;

public class PostDTO {
    private final long id;
    private final String content;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
