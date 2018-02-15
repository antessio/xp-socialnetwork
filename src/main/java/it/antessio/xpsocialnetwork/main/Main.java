package it.antessio.xpsocialnetwork.main;

import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.exception.ServiceException;
import it.antessio.xpsocialnetwork.service.CommandService;
import org.apache.commons.lang3.StringUtils;

import java.io.Console;
import java.util.Scanner;

public class Main {

    public static void main(String [] args){

        Scanner in;
        in = new Scanner(System.in);
        CommandService commandService = new CommandService();
        String command="";

        System.out.println("Hi, type a command");
        printHelp();
        boolean waitForCommands = true;
            while(waitForCommands) {

                command = in.nextLine();
                if("quit".equals(command)){
                    waitForCommands=false;
                }else if("help".equals(command)) {
                    printHelp();
                }else{
                    try{
                        String output = commandService.handle(command);
                        if(StringUtils.isNotBlank(output)) {
                            System.out.println(output);
                        }
                    } catch (ServiceException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }


    }

    private static void printHelp() {
        System.out.println(
                "- posting: <user name> -> <message>\n" +
                "- reading: <user name>\n" +
                "- following: <user name> follows <another user>\n" +
                "- wall: <user name> wall\n");
    }
}
