package it.antessio.xpsocialnetwork.service;

import it.antessio.xpsocialnetwork.exception.CommandNotFoundException;
import it.antessio.xpsocialnetwork.exception.ServiceException;


import it.antessio.xpsocialnetwork.service.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import static it.antessio.xpsocialnetwork.service.handler.AbstractHandler.*;


public class CommandService {


    private static final Logger logger = LoggerFactory.getLogger(CommandService.class);


    private List<CommandHandler> commandHandlers = new ArrayList<>();
    public CommandService(){
        commandHandlers.add(new SubmitPostHandler(INSERT_POST_PATTERN));
        commandHandlers.add(new ListPostsHandler(LIST_POST_PATTERN));
        commandHandlers.add(new FollowsUserHandler(FOLLOWS_PATTERN));
        commandHandlers.add(new UserWallHandler(WALL_PATTERN));
    }

    protected CommandService(List<CommandHandler> commandHandlers){
        this.commandHandlers = commandHandlers;
    }

    public String handle(String command) throws ServiceException, CommandNotFoundException {
        String output = null;
        int i=0;
        logger.info("Command "+command);
        while (output == null && i < commandHandlers.size()){
            output = commandHandlers.get(i).handleCommand(command);
            i++;
        }
        if(output!=null){
            return output;
        }else{
            throw new CommandNotFoundException("Command "+command+" not found");
        }
    }

}
