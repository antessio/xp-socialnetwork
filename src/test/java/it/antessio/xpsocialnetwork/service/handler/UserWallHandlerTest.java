package it.antessio.xpsocialnetwork.service.handler;

import it.antessio.xpsocialnetwork.dao.UserDAO;
import it.antessio.xpsocialnetwork.dao.UserFollowerDAO;
import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.model.User;
import it.antessio.xpsocialnetwork.model.UserPost;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class UserWallHandlerTest {

    private CommandHandler handler;

    private UserPostDAO userPostDAO = mock(UserPostDAO.class);
    private UserDAO userDAO = mock(UserDAO.class);
    private UserFollowerDAO userFollowerDAO = mock(UserFollowerDAO.class);

    private final LocalDateTime NOW = LocalDateTime.now();
    @Test
    public void wall()throws Exception{
        String username="Charlie";
        String command = username+" wall";
        createUserWallHandlerMocks(username);
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(3))));
        List<UserPost> expectedPosts = Arrays.asList(
                new UserPost(1l,username,"I'm in New York today! Anyone wants to have a coffee?", NOW.minusSeconds
                        (15)),
                new UserPost(2l,"Bob","Good game though.", NOW.minusMinutes(1)),
                new UserPost(3l,"Bob","Damn! We lost!", NOW.minusMinutes(2)),
                new UserPost(4l,"Alice","I love the weather today", NOW.minusMinutes(5))
        );

        when(userPostDAO.getWall(username)).thenReturn(expectedPosts);
        String output = handler.handleCommand(command);
        String expectedOutput="Charlie - I'm in New York today! Anyone wants to have a coffee? (15 seconds ago)\n" +
                "Bob - Good game though. (1 minute ago)\n" +
                "Bob - Damn! We lost! (2 minutes ago)\n" +
                "Alice - I love the weather today (5 minutes ago)";
        assertThat(output).isEqualTo(expectedOutput);
    }

    private void createUserWallHandlerMocks(String username){
        handler = new UserWallHandler(userDAO,userPostDAO,userFollowerDAO,AbstractHandler.WALL_PATTERN){
            @Override
            protected LocalDateTime getNow() {
                return NOW;
            }
        };
    }

}
