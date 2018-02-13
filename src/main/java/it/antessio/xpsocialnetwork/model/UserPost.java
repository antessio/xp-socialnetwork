package it.antessio.xpsocialnetwork.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserPost {
    private String username;
    private String content;
    private LocalDateTime created;


    public UserPost(String username, String content, LocalDateTime created) {
        this.username = username;
        this.content = content;
        this.created = created;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPost userPost = (UserPost) o;
        return Objects.equals(username, userPost.username) &&
                Objects.equals(content, userPost.content) &&
                Objects.equals(created, userPost.created);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, content, created);
    }

    @Override
    public String toString() {
        return "UserPost{" +
                "username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", created=" + created +
                '}';
    }
}
