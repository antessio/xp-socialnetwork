package xpsocialnetwork.dao;

import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.model.UserPost;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;


public class UserPostTest extends AbstractDAOTest{

    private UserPostDAO dao;

    @Before
    public void setUp(){
        super.setUp();
        dao = new UserPostDAO();
    }

    @Test
    public void selectUsersPost_onePost() throws DAOException {
        List<UserPost> postList = dao.findPostsByUser("Alice");
        assertThat(postList).hasSize(1);
        UserPost userPost = postList.get(0);
        assertThat(userPost.getUsername()).isEqualTo("Alice");
        assertThat(userPost.getContent()).isEqualTo("I feel good");
    }
    @Test
    public void selectUsersPost_twoPosts() throws DAOException {
        List<UserPost> postList = dao.findPostsByUser("Bob");
        assertThat(postList).hasSize(2);
        UserPost userPost = postList.get(0);
        assertThat(userPost.getUsername()).isEqualTo("Bob");
        assertThat(userPost.getContent()).isEqualTo("Thank God is Monday. What!?!");
        LocalDateTime firstPostCreated = userPost.getCreated();
        userPost = postList.get(1);
        assertThat(userPost.getUsername()).isEqualTo("Bob");
        assertThat(userPost.getContent()).isEqualTo("I feel sad");
        assertThat(userPost.getCreated()).isLessThan(firstPostCreated);

    }
    @Test
    public void insertPost() throws DAOException {
        String expectedContent = "I'm Alice";
        UserPost userPost = new UserPost("Alice", expectedContent, LocalDateTime.now());
        dao.insertPost(userPost);
        List<Map<String,Object>> userPosts = queryAll("user_post");
        assertThat(userPosts.stream().filter(
                map-> map.get("content").equals(expectedContent) && map.get("username").equals("Alice")
        ).findFirst().isPresent()).isTrue();
    }
    @Test
    public void insertPost_notExistingUser(){
        assertThatThrownBy(
                ()->dao.insertPost(new UserPost("Antonio","Ciao", LocalDateTime.now()))
        ).isInstanceOf(DAOException.class);
    }

    @Test
    public void getWallWithOneUser() throws DAOException {
        List<UserPost> userWall = dao.getWall("Charlie");
        assertThat(userWall).hasSize(3);

        UserPost userPost = userWall.get(0);
        assertThatUserPostEqual(userPost, "Bob", "Thank God is Monday. What!?!", LocalDateTime.of(2018,2,2,10,10,0));
        userPost = userWall.get(1);
        assertThatUserPostEqual(userPost, "Charlie", "Thank God is Monday.", LocalDateTime.of(2018, 2, 2, 10, 8, 0));
        userPost = userWall.get(2);
        assertThatUserPostEqual(userPost, "Bob", "I feel sad", LocalDateTime.of(2018,2,1,10,10,0));
    }
    @Test
    public void getWallWithTwoUsers() throws DAOException {
        List<UserPost> userWall = dao.getWall("Bob");
        assertThat(userWall).hasSize(4);
        UserPost userPost = userWall.get(0);
        assertThatUserPostEqual(userPost, "Bob", "Thank God is Monday. What!?!", LocalDateTime.of(2018,2,2,10,10,0));
        userPost = userWall.get(1);
        assertThatUserPostEqual(userPost, "Charlie", "Thank God is Monday.", LocalDateTime.of(2018, 2, 2, 10, 8, 0));
        userPost = userWall.get(2);
        assertThatUserPostEqual(userPost, "Bob", "I feel sad", LocalDateTime.of(2018,2,1,10,10,0));
        userPost = userWall.get(3);
        assertThatUserPostEqual(userPost, "Alice", "I feel good", LocalDateTime.of(2018,2,1,9,10,0));
    }

    private void assertThatUserPostEqual(UserPost userPost, String username, String content, LocalDateTime created) {
        assertThat(userPost.getUsername()).isEqualTo(username);
        assertThat(userPost.getContent()).isEqualTo(content);
        assertThat(userPost.getCreated())
                .isEqualTo(created);
    }


    @Test
    public void getWallNoPost() throws DAOException {
        List<UserPost> userWall = dao.getWall("Samuel");
        assertThat(userWall).hasSize(0);
    }

}
