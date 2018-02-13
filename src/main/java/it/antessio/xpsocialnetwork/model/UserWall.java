package it.antessio.xpsocialnetwork.model;

import java.util.List;
import java.util.Objects;

public class UserWall {
    private String username;

    private List<UserPost> posts;

    public UserWall(String username, List<UserPost> posts) {
        this.username = username;
        this.posts = posts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UserPost> getPosts() {
        return posts;
    }

    public void setPosts(List<UserPost> posts) {
        this.posts = posts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserWall userWall = (UserWall) o;
        return Objects.equals(username, userWall.username) &&
                Objects.equals(posts, userWall.posts);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, posts);
    }

    @Override
    public String toString() {
        return "UserWall{" +
                "username='" + username + '\'' +
                ", posts=" + posts +
                '}';
    }
}
