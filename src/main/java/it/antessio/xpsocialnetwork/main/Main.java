package it.antessio.xpsocialnetwork.main;


import it.antessio.xpsocialnetwork.dao.db.DatabseUtils;
import it.antessio.xpsocialnetwork.exception.ServiceException;
import it.antessio.xpsocialnetwork.service.CommandService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String [] args){

        Scanner in;
        in = new Scanner(System.in);
        CommandService commandService = new CommandService();
        String command="";

        System.out.print(">");
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
                        printOutput(output);
                    } catch (ServiceException e) {
                        logger.error("Service error: "+e.getMessage(),e);
                        printOutput(e.getMessage());
                    }
                }
            }


    }

    private static void printOutput(String output){
        if(StringUtils.isNotBlank(output)){
            System.out.println(output);
        }
        System.out.print(">");
    }

    private static void printHelp() {
        printOutput(
                "- posting: <user name> -> <message>\n" +
                "- reading: <user name>\n" +
                "- following: <user name> follows <another user>\n" +
                "- wall: <user name> wall\n");
    }
}
