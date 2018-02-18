package it.antessio.xpsocialnetwork.service.handler;

import it.antessio.xpsocialnetwork.exception.ServiceException;

public interface CommandHandler {


    String handleCommand(String command) throws ServiceException;


}
