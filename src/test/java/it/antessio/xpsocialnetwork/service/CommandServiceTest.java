package it.antessio.xpsocialnetwork.service;

import it.antessio.xpsocialnetwork.exception.CommandNotFoundException;
import it.antessio.xpsocialnetwork.exception.ServiceException;

import it.antessio.xpsocialnetwork.service.handler.*;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CommandServiceTest {

    private CommandService commandService;
    private List<CommandHandler> commandHandlers;

    private SubmitPostHandler submitPostHandler = mock(SubmitPostHandler.class);
    private ListPostsHandler listPostsHandler = mock(ListPostsHandler.class);
    private FollowsUserHandler followsUserHandler = mock(FollowsUserHandler.class);
    private UserWallHandler userWallHandler = mock(UserWallHandler.class);


    @Before
    public void setUp() throws Exception {
        commandHandlers = new ArrayList<>();
        commandHandlers.add(submitPostHandler);
        commandHandlers.add(listPostsHandler);
        commandHandlers.add(followsUserHandler);
        commandHandlers.add(userWallHandler);
        commandService = new CommandService(commandHandlers);
    }

    @Test
    public void testPostHandler() throws CommandNotFoundException, ServiceException {
        String username = "Alice";
        String content = "I love the weather today";
        String command = username+" -> "+content;
        String expectedOutput = "";
        when(submitPostHandler.handleCommand(command)).thenReturn(expectedOutput);
        String output = commandService.handle(command);
        verify(submitPostHandler).handleCommand(command);
        assertThat(output).isEqualTo(expectedOutput);
    }
    @Test
    public void testListPostsHandler() throws CommandNotFoundException, ServiceException {
        String username = "Alice";
        String command = username;
        String expectedOutput = username+" ciao (1 second ago)";
        when(submitPostHandler.handleCommand(command)).thenReturn(null);
        when(listPostsHandler.handleCommand(command)).thenReturn(expectedOutput);
        String output = commandService.handle(command);
        verify(submitPostHandler).handleCommand(command);
        verify(listPostsHandler).handleCommand(command);
        assertThat(output).isEqualTo(expectedOutput);
    }

    @Test
    public void testFollowsHandler() throws CommandNotFoundException, ServiceException {
        String username = "Alice";
        String follower = "Bob";
        String command = follower+" follows "+username;
        String expectedOutput = "";
        when(submitPostHandler.handleCommand(command)).thenReturn(null);
        when(listPostsHandler.handleCommand(command)).thenReturn(null);
        when(followsUserHandler.handleCommand(command)).thenReturn(expectedOutput);
        String output = commandService.handle(command);
        verify(submitPostHandler).handleCommand(command);
        verify(listPostsHandler).handleCommand(command);
        verify(followsUserHandler).handleCommand(command);
        assertThat(output).isEqualTo(expectedOutput);
    }

    @Test
    public void testWallHandler() throws CommandNotFoundException, ServiceException {
        String username = "Alice";
        String command = username+" wall";
        String expectedOutput = username+" ciao (1 second ago)";
        when(submitPostHandler.handleCommand(command)).thenReturn(null);
        when(listPostsHandler.handleCommand(command)).thenReturn(null);
        when(followsUserHandler.handleCommand(command)).thenReturn(null);
        when(userWallHandler.handleCommand(command)).thenReturn(expectedOutput);
        String output = commandService.handle(command);
        verify(submitPostHandler).handleCommand(command);
        verify(listPostsHandler).handleCommand(command);
        verify(followsUserHandler).handleCommand(command);
        verify(userWallHandler).handleCommand(command);
        assertThat(output).isEqualTo(expectedOutput);
    }
    @Test
    public void commandNotFound_throwsException() throws ServiceException {
        String username = "Alice";
        String command = "print "+username+" posts";

        when(submitPostHandler.handleCommand(command)).thenReturn(null);
        when(listPostsHandler.handleCommand(command)).thenReturn(null);
        when(followsUserHandler.handleCommand(command)).thenReturn(null);
        when(userWallHandler.handleCommand(command)).thenReturn(null);
        assertThatThrownBy(()->commandService.handle(command)).isInstanceOf(CommandNotFoundException.class);
        verify(submitPostHandler).handleCommand(command);
        verify(listPostsHandler).handleCommand(command);
        verify(followsUserHandler).handleCommand(command);
        verify(userWallHandler).handleCommand(command);

    }
}
