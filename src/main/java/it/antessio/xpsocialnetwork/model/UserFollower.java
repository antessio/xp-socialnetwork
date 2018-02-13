package it.antessio.xpsocialnetwork.model;

import java.util.Objects;

public class UserFollower {
    private String username;
    private String follower;

    public UserFollower(String username, String follower) {
        this.username = username;
        this.follower = follower;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFollower that = (UserFollower) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(follower, that.follower);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, follower);
    }

    @Override
    public String toString() {
        return "UserFollower{" +
                "username='" + username + '\'' +
                ", follower='" + follower + '\'' +
                '}';
    }
}
