package it.antessio.xpsocialnetwork.service.handler;

import it.antessio.xpsocialnetwork.dao.UserDAO;
import it.antessio.xpsocialnetwork.dao.UserFollowerDAO;
import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.exception.ServiceException;
import it.antessio.xpsocialnetwork.model.User;
import it.antessio.xpsocialnetwork.model.UserPost;


import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubmitPostHandler extends AbstractHandler {


    private String username;
    private String content;

    public SubmitPostHandler(UserDAO userDAO, UserPostDAO userPostDAO, UserFollowerDAO userFollowerDAO, Pattern pattern) {
        super(userDAO, userPostDAO, userFollowerDAO, pattern);
    }

    public SubmitPostHandler(Pattern pattern) {
        super(pattern);
    }


    @Override
    public String handleCommand(String command) throws ServiceException {
        Matcher matcher = pattern.matcher(command);
        if(matcher.matches()) {
            username = matcher.group(1);
            content = matcher.group(2);
            LocalDateTime created = getNow();
            logger.info(username + " is publishing new post " + content + " on " + created);
            try {
                if (!userDAO.find(username).isPresent()) {
                    userDAO.insert(new User(username, getNow()));
                }
                userPostDAO.insertPost(new UserPost(username, content, created));
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
            return "";
        }else{
            return null;
        }
    }



    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    protected LocalDateTime getNow(){
        return LocalDateTime.now();
    }
}
