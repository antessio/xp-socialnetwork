package it.antessio.xpsocialnetwork.service.handler;

import it.antessio.xpsocialnetwork.dao.UserDAO;
import it.antessio.xpsocialnetwork.dao.UserFollowerDAO;
import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.exception.ServiceException;
import it.antessio.xpsocialnetwork.model.User;
import it.antessio.xpsocialnetwork.model.UserFollower;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FollowsUserHandlerTest {

    private CommandHandler handler;

    private UserPostDAO userPostDAO = mock(UserPostDAO.class);
    private UserDAO userDAO = mock(UserDAO.class);
    private UserFollowerDAO userFollowerDAO = mock(UserFollowerDAO.class);

    private final LocalDateTime NOW = LocalDateTime.now();


    @Test
    public void followUser() throws Exception{
        String follower="Charlie";
        String username="Alice";
        String command = follower+" follows "+username;
        createFollowsUserHandlerMocks();
        when(userDAO.find(follower)).thenReturn(Optional.of(new User(follower, NOW.minusMinutes(3))));
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(5))));
        when(userFollowerDAO.findByUsernameAndFollower(username,follower))
                .thenReturn(Optional.empty());
        String output = handler.handleCommand(command);
        verify(userFollowerDAO).insert(new UserFollower(username,follower));
        assertThat(output).isEmpty();

    }
    @Test
    public void followUser_notExistingUser() throws Exception{
        String follower="Charlie";
        String username="Nicola";
        String command = follower+" follows "+username;
        createFollowsUserHandlerMocks();
        when(userDAO.find(follower)).thenReturn(Optional.of(new User(follower, NOW.minusMinutes(3))));
        when(userDAO.find(username)).thenReturn(Optional.empty());
        assertThatThrownBy(()->
                handler.handleCommand(command)
        ).isInstanceOf(ServiceException.class)
                .matches(e->e.getMessage().equals(username+" not found"));
    }
    @Test
    public void followUser_notExistingFollower() throws Exception{
        String follower="Nicola";
        String username="Alice";
        String command = follower+" follows "+username;
        createFollowsUserHandlerMocks();
        when(userDAO.find(follower)).thenReturn(Optional.empty());
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(5))));
        assertThatThrownBy(()->
                handler.handleCommand(command)
        ).isInstanceOf(ServiceException.class)
                .matches(e->e.getMessage().equals(follower+" not found"));
    }
    @Test
    public void followUser_alreadyPresent()throws Exception{
        String follower="Charlie";
        String username="Alice";
        String command = follower+" follows "+username;
        createFollowsUserHandlerMocks();
        when(userDAO.find(follower)).thenReturn(Optional.of(new User(follower, NOW.minusMinutes(3))));
        when(userDAO.find(username)).thenReturn(Optional.of(new User(username, NOW.minusMinutes(5))));
        when(userFollowerDAO.findByUsernameAndFollower(username,follower)).thenReturn(Optional.of(new UserFollower
                (username,follower)));
        String output = handler.handleCommand(command);
        assertThat(output).isEqualTo(follower+" is already following "+username);
    }
    @Test
    public void commandNotFound() throws ServiceException {
        createFollowsUserHandlerMocks();
        assertThat(handler.handleCommand("Nicola")).isNull();
    }
    
    private void createFollowsUserHandlerMocks(){
        handler = new FollowsUserHandler(userDAO,userPostDAO,userFollowerDAO,AbstractHandler.FOLLOWS_PATTERN){
            @Override
            protected LocalDateTime getNow() {
                return NOW;
            }
        };
    }

}
