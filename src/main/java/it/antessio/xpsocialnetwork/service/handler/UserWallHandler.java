package it.antessio.xpsocialnetwork.service.handler;

import it.antessio.xpsocialnetwork.dao.UserDAO;
import it.antessio.xpsocialnetwork.dao.UserFollowerDAO;
import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.exception.ServiceException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserWallHandler extends AbstractHandler{

    private String username;

    public UserWallHandler(UserDAO userDAO, UserPostDAO userPostDAO, UserFollowerDAO userFollowerDAO, Pattern pattern) {
        super(userDAO, userPostDAO, userFollowerDAO, pattern);
    }

    public UserWallHandler(Pattern pattern) {
        super(pattern);
    }


    @Override
    public String handleCommand(String command) throws ServiceException {
        Matcher matcher = pattern.matcher(command);
        if(matcher.matches()) {
            username = matcher.group(1);
            logger.info("Get wall for user " + username);
            try {
                userDAO.find(username).orElseThrow(() -> new ServiceException(username + " not found"));
                return collectUserPosts(userPostDAO.getWall(username), true);
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
        }else{
            return null;
        }
    }

    public String getUsername() {
        return username;
    }
}
