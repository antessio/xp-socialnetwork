package it.antessio.xpsocialnetwork.main;

import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.exception.ServiceException;
import it.antessio.xpsocialnetwork.service.CommandService;

import java.io.Console;

public class Main {

    public static void main(String [] args){
        Console c = System.console();
        CommandService commandService = new CommandService();
        String command = c.readLine(">");
        try {
            String output =  commandService.handle(command);
        } catch (ServiceException e) {
            e.printStackTrace();
        }


    }
}
