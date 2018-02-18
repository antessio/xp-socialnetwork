package it.antessio.xpsocialnetwork.service.handler;

import it.antessio.xpsocialnetwork.dao.UserDAO;
import it.antessio.xpsocialnetwork.dao.UserFollowerDAO;
import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.exception.ServiceException;
import it.antessio.xpsocialnetwork.model.User;
import it.antessio.xpsocialnetwork.model.UserPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListPostsHandler extends AbstractHandler {

    private String username;

    public ListPostsHandler(UserDAO userDAO, UserPostDAO userPostDAO, UserFollowerDAO userFollowerDAO, Pattern
            pattern) {
        super(userDAO, userPostDAO, userFollowerDAO, pattern);
    }

    public ListPostsHandler(Pattern pattern) {
        super(pattern);
    }


    @Override
    public String handleCommand(String command) throws ServiceException {
        Matcher matcher = pattern.matcher(command);
        if(matcher.matches()) {
            username = matcher.group(1);
            logger.info(username + " list posts ");
            try {
                userDAO.find(username).orElseThrow(() -> new ServiceException(username + " not found"));
                List<UserPost> userPostList = userPostDAO.findPostsByUser(username);
                return collectUserPosts(userPostList, false);
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


    protected LocalDateTime getNow(){
        return LocalDateTime.now();
    }
}
