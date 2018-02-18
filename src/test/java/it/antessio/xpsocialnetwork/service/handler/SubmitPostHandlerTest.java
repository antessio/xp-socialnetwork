package it.antessio.xpsocialnetwork.service.handler;

import it.antessio.xpsocialnetwork.dao.UserDAO;
import it.antessio.xpsocialnetwork.dao.UserFollowerDAO;
import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.model.User;
import it.antessio.xpsocialnetwork.model.UserPost;
import org.junit.Test;

import java.time.LocalDateTime;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubmitPostHandlerTest {

    private CommandHandler postHandler;

    private UserPostDAO userPostDAO = mock(UserPostDAO.class);
    private UserDAO userDAO = mock(UserDAO.class);
    private UserFollowerDAO userFollowerDAO = mock(UserFollowerDAO.class);

    private final LocalDateTime NOW = LocalDateTime.now();


    @Test
    public void existingUserSubmitPost()throws Exception{
        String username = "Alice";
        String content = "I love the weather today";
        String command = username+" -> "+content;
        createHandlerWithMocks(username,content);
        UserPost expectedPost = new UserPost(username,content, NOW);
        when(userDAO.find("Alice"))
                .thenReturn(Optional.of(new User("Alice",LocalDateTime.now().minusDays(3))));
        String output = postHandler.handleCommand(command);
        verify(userPostDAO).insertPost(expectedPost);
        assertThat(output).isEmpty();
    }
    @Test
    public void notExistingUserSubmitPost()throws Exception{
        String username = "Alice";
        String content = "I love the weather today";
        String command = username+" -> "+content;
        createHandlerWithMocks(username,content);
        UserPost expectedPost = new UserPost(username,content, NOW);
        when(userDAO.find("Alice"))
                .thenReturn(Optional.empty());
        String output = postHandler.handleCommand(command);
        verify(userPostDAO).insertPost(expectedPost);
        verify(userDAO).insert(new User(username, NOW));
        assertThat(output).isEmpty();
    }

    private void createHandlerWithMocks(String username, String content) {
        postHandler = new SubmitPostHandler(userDAO, userPostDAO,userFollowerDAO,AbstractHandler.INSERT_POST_PATTERN){
            @Override
            protected LocalDateTime getNow() {
                return NOW;
            }
        };
    }

    @Test
    public void userSubmitEmptyPost() throws Exception{
        String username = "Alice";
        String content = "";
        String command = username+" -> "+content;
        createHandlerWithMocks(username,content);
        UserPost expectedPost = new UserPost(username,content, NOW);
        when(userDAO.find("Alice")).thenReturn(Optional.empty());
        String output = postHandler.handleCommand(command);
        verify(userPostDAO).insertPost(expectedPost);
        verify(userDAO).insert(new User(username, NOW));
        assertThat(output).isEmpty();
    }


}
