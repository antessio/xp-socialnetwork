package xpsocialnetwork.service;

import it.antessio.xpsocialnetwork.dao.UserDAO;
import it.antessio.xpsocialnetwork.dao.UserFollowerDAO;
import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.dao.UserWallDAO;
import it.antessio.xpsocialnetwork.exception.ServiceException;
import it.antessio.xpsocialnetwork.model.User;
import it.antessio.xpsocialnetwork.model.UserPost;
import it.antessio.xpsocialnetwork.model.UserWall;
import it.antessio.xpsocialnetwork.service.CommandService;
import org.junit.Before;
import org.junit.Test;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CommandServiceTest {

    private CommandService commandService;

    private UserPostDAO userPostDAO = mock(UserPostDAO.class);
    private UserDAO userDAO = mock(UserDAO.class);
    private UserFollowerDAO userFollowerDAO = mock(UserFollowerDAO.class);
    private UserWallDAO userWallDAO = mock(UserWallDAO.class);

    private LocalDateTime now = LocalDateTime.now();

    @Before
    public void setUp() throws Exception {
        commandService = new CommandService(userPostDAO,userDAO,userFollowerDAO,userWallDAO){
            @Override
            protected LocalDateTime getNow() {
                return now;
            }
        };
    }
    @Test
    public void userSubmitPost()throws Exception{
        String username = "Alice";
        String content = "I love the weather today";
        String command = username + " -> " + content;
        UserPost expectedPost = new UserPost(username,content, now);
        String output = commandService.handle(command);
        verify(userPostDAO).insertPost(expectedPost);
        verify(userDAO).insert(username);
        assertThat(output).isEmpty();
    }
    @Test
    public void userSubmitEmptyPost() throws Exception{
        String username = "Alice";
        String content = "";
        String command = username + " -> " + content;
        UserPost expectedPost = new UserPost(username,content, now);
        String output = commandService.handle(command);
        verify(userPostDAO).insertPost(expectedPost);
        verify(userDAO).insert(username);
        assertThat(output).isEmpty();
    }
    @Test
    public void userListPosts1Second() throws Exception{
        String username = "Charlie";
        String command = username;
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(1))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(username, "I'm in New York today! Anyone wants to have a coffee?",now
                                .minusSeconds(1))
                )
        );
        String output = commandService.handle(command);
        assertThat(output).isEqualTo("I'm in New York today! Anyone wants to have a coffee? (1 second ago)");
    }
    @Test
    public void userListPosts2Seconds() throws Exception{
        String username = "Charlie";
        String command = username;
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(1))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(username, "I'm in New York today! Anyone wants to have a coffee?",now
                                .minusSeconds(2))
                )
        );
        String output = commandService.handle(command);
        assertThat(output).isEqualTo("I'm in New York today! Anyone wants to have a coffee? (2 seconds ago)");
    }
    @Test
    public void userListPosts61Seconds() throws Exception{
        String username = "Charlie";
        String command = username;
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(1))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(username, "I'm in New York today! Anyone wants to have a coffee?",now
                                .minusSeconds(61))
                )
        );
        String output = commandService.handle(command);
        assertThat(output).isEqualTo("I'm in New York today! Anyone wants to have a coffee? (1 minute ago)");
    }

    @Test
    public void userListPosts1Minute() throws Exception{
        String username = "Bob";
        String command = username;
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(1))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(username, "Good game though.",now.minusMinutes(1))
                )
        );
        String output = commandService.handle(command);
        assertThat(output).isEqualTo("Good game though. (1 minute ago)");
    }

    @Test
    public void userListPosts5Minutes() throws Exception{
        String username = "Alice";
        String command = username;
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(5))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(username, "I love the weather today",now.minusMinutes(5))
                )
        );
        String output = commandService.handle(command);


        assertThat(output).isEqualTo("I love the weather today (5 minutes ago)");
    }
    @Test
    public void userListPosts61Minutes() throws Exception{
        String username = "Alice";
        String command = username;
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(5))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(username, "I love the weather today",now.minusMinutes(61))
                )
        );
        String output = commandService.handle(command);


        assertThat(output).isEqualTo("I love the weather today (1 hour ago)");
    }

    @Test
    public void userListPosts1Hour() throws Exception{
        String username = "Alice";
        String command = username;
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(5))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(username, "I love the weather today",now.minusHours(1))
                )
        );
        String output = commandService.handle(command);


        assertThat(output).isEqualTo("I love the weather today (1 hour ago)");
    }

    @Test
    public void userListPosts2Hour() throws Exception{
        String username = "Alice";
        String command = username;
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(5))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(username, "I love the weather today",now.minusHours(2))
                )
        );
        String output = commandService.handle(command);


        assertThat(output).isEqualTo("I love the weather today (2 hours ago)");
    }
    @Test
    public void userListPosts25Hour() throws Exception{
        String username = "Alice";
        String command = username;
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(5))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(username, "I love the weather today",now.minusHours(25))
                )
        );
        String output = commandService.handle(command);


        assertThat(output).isEqualTo("I love the weather today (1 day ago)");
    }


    @Test
    public void userListPosts1Day() throws Exception{
        String username = "Charlie";
        String command = username;
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(1))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(username, "I'm in New York today! Anyone wants to have a coffee?",now
                                .minusDays(1))
                )
        );
        String output = commandService.handle(command);
        assertThat(output).isEqualTo("I'm in New York today! Anyone wants to have a coffee? (1 day ago)");
    }
    @Test
    public void userListPosts2Days() throws Exception{
        String username = "Charlie";
        String command = username;
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(1))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(username, "I'm in New York today! Anyone wants to have a coffee?",now
                                .minusDays(2))
                )
        );
        String output = commandService.handle(command);
        assertThat(output).isEqualTo("I'm in New York today! Anyone wants to have a coffee? (2 days ago)");
    }
    @Test
    public void userListPostsMulti() throws Exception{
        String username = "Bob";
        String command = username;
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(2))));
        when(userPostDAO.findPostsByUser(username)).thenReturn(
                Arrays.asList(
                        new UserPost(username, "Good game though.",now.minusMinutes(1)),
                        new UserPost(username, "Damn! We lost!",now.minusMinutes(2))
                )
        );
        String output = commandService.handle(command);
        assertThat(output).isEqualTo("Good game though. (1 minute ago)\nDamn! We lost! (2 minutes ago)");
    }
    @Test
    public void userListPosts_notExistingUser()throws Exception{
        String username = "Nicola";
        String command = username;
        when(userDAO.find(username)).thenReturn(Optional.empty());
        assertThatThrownBy(()->
            commandService.handle(command)
        ).isInstanceOf(ServiceException.class)
        .matches(e->e.getMessage().equals(username+" not found"));

    }

    @Test
    public void followUser() throws Exception{
        String follower="Charlie";
        String username="Alice";
        String command=follower+" follows "+username;
        when(userDAO.find(follower)).thenReturn(Optional.of(new User(follower,now.minusMinutes(3))));
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(5))));
        String output = commandService.handle(command);
        verify(userFollowerDAO).insert(username,follower);
        assertThat(output).isEmpty();

    }
    @Test
    public void followUser_notExistingUser() throws Exception{
        String follower="Charlie";
        String username="Nicola";
        String command=follower+" follows "+username;
        when(userDAO.find(follower)).thenReturn(Optional.of(new User(follower,now.minusMinutes(3))));
        when(userDAO.find(username)).thenReturn(Optional.empty());
        assertThatThrownBy(()->
                commandService.handle(command)
        ).isInstanceOf(ServiceException.class)
                .matches(e->e.getMessage().equals(username+" not found"));
    }
    @Test
    public void followUser_notExistingFollower() throws Exception{
        String follower="Nicola";
        String username="Alice";
        String command=follower+" follows "+username;
        when(userDAO.find(follower)).thenReturn(Optional.empty());
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(5))));
        assertThatThrownBy(()->
                commandService.handle(command)
        ).isInstanceOf(ServiceException.class)
                .matches(e->e.getMessage().equals(follower+" not found"));
    }

    @Test
    public void wall()throws Exception{
        String username="Charlie";
        String command = username+" wall";
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username,now.minusMinutes(3))));
        List<UserPost> expectedPosts = Arrays.asList(
                new UserPost(username,"I'm in New York today! Anyone wants to have a coffee?",now.minusSeconds(15)),
                new UserPost("Bob","Good game though.",now.minusMinutes(1)),
                new UserPost("Bob","Damn! We lost!",now.minusMinutes(2)),
                new UserPost("Alice","I love the weather today",now.minusMinutes(5))
        );
        UserWall expectedWall = new UserWall(username,expectedPosts);
        when(userWallDAO.get(username)).thenReturn(expectedWall);
        String output = commandService.handle(command);
        String expectedOutput="Charlie - I'm in New York today! Anyone wants to have a coffee? (15 seconds ago)\n" +
                "Bob - Good game though. (1 minute ago)\n" +
                "Bob - Damn! We lost! (2 minutes ago)\n" +
                "Alice - I love the weather today (5 minutes ago)";
        assertThat(output).isEqualTo(expectedOutput);
    }
}