package it.antessio.xpsocialnetwork.service.handler;

import it.antessio.xpsocialnetwork.dao.UserDAO;
import it.antessio.xpsocialnetwork.dao.UserFollowerDAO;
import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.exception.ServiceException;
import it.antessio.xpsocialnetwork.model.User;
import it.antessio.xpsocialnetwork.model.UserPost;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ListPostsHandlerTest {

    private CommandHandler handler;

    private UserPostDAO userPostDAO = mock(UserPostDAO.class);
    private UserDAO userDAO = mock(UserDAO.class);
    private UserFollowerDAO userFollowerDAO = mock(UserFollowerDAO.class);
    private final LocalDateTime NOW = LocalDateTime.now();


    @Test
    public void userListPosts1Second() throws Exception{
        String username = "Charlie";
        String command = username;
        createListCommandHandlerWithMocks();
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(1))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(1l,username, "I'm in New York today! Anyone wants to have a coffee?", NOW
                                .minusSeconds(1))
                )
        );
        String output = handler.handleCommand(command);
        assertThat(output).isEqualTo("I'm in New York today! Anyone wants to have a coffee? (1 second ago)");
    }
    @Test
    public void userListPosts2Seconds() throws Exception{
        String username = "Charlie";
        String command = username;
        createListCommandHandlerWithMocks();
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(1))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(1l,username, "I'm in New York today! Anyone wants to have a coffee?", NOW
                                .minusSeconds(2))
                )
        );
        String output = handler.handleCommand(command);
        assertThat(output).isEqualTo("I'm in New York today! Anyone wants to have a coffee? (2 seconds ago)");
    }
    @Test
    public void userListPosts61Seconds() throws Exception{
        String username = "Charlie";
        String command = username;
        createListCommandHandlerWithMocks();
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(1))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(1l,username, "I'm in New York today! Anyone wants to have a coffee?", NOW
                                .minusSeconds(61))
                )
        );
        String output = handler.handleCommand(command);
        assertThat(output).isEqualTo("I'm in New York today! Anyone wants to have a coffee? (1 minute ago)");
    }

    @Test
    public void userListPosts1Minute() throws Exception{
        String username = "Bob";
        String command = username;
        createListCommandHandlerWithMocks();
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(1))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(1l,username, "Good game though.", NOW.minusMinutes(1))
                )
        );
        String output = handler.handleCommand(command);
        assertThat(output).isEqualTo("Good game though. (1 minute ago)");
    }

    @Test
    public void userListPosts5Minutes() throws Exception{
        String username = "Alice";
        String command = username;
        createListCommandHandlerWithMocks();
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(5))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(1l,username, "I love the weather today", NOW.minusMinutes(5))
                )
        );
        String output = handler.handleCommand(command);


        assertThat(output).isEqualTo("I love the weather today (5 minutes ago)");
    }
    @Test
    public void userListPosts61Minutes() throws Exception{
        String username = "Alice";
        String command = username;
        createListCommandHandlerWithMocks();
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(5))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(1l,username, "I love the weather today", NOW.minusMinutes(61))
                )
        );
        String output = handler.handleCommand(command);


        assertThat(output).isEqualTo("I love the weather today (1 hour ago)");
    }

    @Test
    public void userListPosts1Hour() throws Exception{
        String username = "Alice";
        String command = username;
        createListCommandHandlerWithMocks();
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(5))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(1l,username, "I love the weather today", NOW.minusHours(1))
                )
        );
        String output = handler.handleCommand(command);


        assertThat(output).isEqualTo("I love the weather today (1 hour ago)");
    }

    @Test
    public void userListPosts2Hour() throws Exception{
        String username = "Alice";
        String command = username;
        createListCommandHandlerWithMocks();
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(5))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(1l,username, "I love the weather today", NOW.minusHours(2))
                )
        );
        String output = handler.handleCommand(command);


        assertThat(output).isEqualTo("I love the weather today (2 hours ago)");
    }
    @Test
    public void userListPosts25Hour() throws Exception{
        String username = "Alice";
        String command = username;
        createListCommandHandlerWithMocks();
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(5))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(1l,username, "I love the weather today", NOW.minusHours(25))
                )
        );
        String output = handler.handleCommand(command);


        assertThat(output).isEqualTo("I love the weather today (1 day ago)");
    }


    @Test
    public void userListPosts1Day() throws Exception{
        String username = "Charlie";
        String command = username;
        createListCommandHandlerWithMocks();
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(1))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(1l,username, "I'm in New York today! Anyone wants to have a coffee?", NOW
                                .minusDays(1))
                )
        );
        String output = handler.handleCommand(command);
        assertThat(output).isEqualTo("I'm in New York today! Anyone wants to have a coffee? (1 day ago)");
    }
    @Test
    public void userListPosts2Days() throws Exception{
        String username = "Charlie";
        String command = username;
        createListCommandHandlerWithMocks();
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(1))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(1l,username, "I'm in New York today! Anyone wants to have a coffee?", NOW
                                .minusDays(2))
                )
        );
        String output = handler.handleCommand(command);
        assertThat(output).isEqualTo("I'm in New York today! Anyone wants to have a coffee? (2 days ago)");
    }
    @Test
    public void userListPostsMulti() throws Exception{
        String username = "Bob";
        String command = username;
        createListCommandHandlerWithMocks();
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(2))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(1l,username, "Good game though.", NOW.minusMinutes(1)),
                        new UserPost(2l,username, "Damn! We lost!", NOW.minusMinutes(2))
                )
        );
        String output = handler.handleCommand(command);
        assertThat(output).isEqualTo("Good game though. (1 minute ago)\nDamn! We lost! (2 minutes ago)");
    }
    @Test
    public void userListPosts_notExistingUser()throws Exception{
        String username = "Nicola";
        String command = username;
        createListCommandHandlerWithMocks();
        when(userDAO.find(username)).thenReturn(Optional.empty());
        assertThatThrownBy(()->
                handler.handleCommand(command)
        ).isInstanceOf(ServiceException.class)
                .matches(e->e.getMessage().equals(username+" not found"));

    }

    private void createListCommandHandlerWithMocks(){
        handler = new ListPostsHandler(userDAO,userPostDAO,userFollowerDAO,AbstractHandler.LIST_POST_PATTERN){
            @Override
            protected LocalDateTime getNow() {
                return NOW;
            }
        };
    }

}
