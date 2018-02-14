package xpsocialnetwork.dao;

import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.model.UserPost;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class UserPostTest extends AbstractDAOTest{

    private UserPostDAO dao;

    @Before
    public void setUp(){
        dao = new UserPostDAO();
        createTestDB();
    }

    @After
    public void tearDown(){
        dropTestDB();
    }
    @Test
    public void selectUsersPost()throws Exception{
        List<UserPost> postList = dao.findPostsByUser("Alice");
        assertThat(postList).hasSize(1);
        UserPost userPost = postList.get(0);
        assertThat(userPost.getUsername()).isEqualTo("Alice");
        assertThat(userPost.getContent()).isEqualTo("I feel good");
    }
    @Test
    public void insertPost()throws Exception{
        String expectedContent = "I'm Alice";
        UserPost userPost = new UserPost("Alice", expectedContent, LocalDateTime.now());
        dao.insertPost(userPost);
        List<Map<String,Object>> userPosts = queryAll("user_post");
        assertThat(userPosts.stream().filter(
                map-> map.get("content").equals(expectedContent) && map.get("username").equals("Alice")
        ).findFirst().isPresent()).isTrue();

    }


}
