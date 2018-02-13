package it.antessio.xpsocialnetwork.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private String username;
    private LocalDateTime createdAt;

    public User(String username, LocalDateTime createdAt) {
        this.username = username;
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(createdAt, user.createdAt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, createdAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
