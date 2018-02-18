package it.antessio.xpsocialnetwork.service.handler;

import it.antessio.xpsocialnetwork.dao.UserDAO;
import it.antessio.xpsocialnetwork.dao.UserFollowerDAO;
import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.exception.ServiceException;
import it.antessio.xpsocialnetwork.model.UserFollower;
import it.antessio.xpsocialnetwork.model.UserPost;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FollowsUserHandler extends AbstractHandler {


    private String username;
    private String follower;

    public FollowsUserHandler(UserDAO userDAO, UserPostDAO userPostDAO, UserFollowerDAO userFollowerDAO, Pattern pattern) {
        super(userDAO, userPostDAO, userFollowerDAO, pattern);

    }

    public FollowsUserHandler(Pattern pattern) {
        super(pattern);
    }


    @Override
    public String handleCommand(String command) throws ServiceException {
        Matcher matcher = pattern.matcher(command);
        if(matcher.matches()) {
            this.follower = matcher.group(1);
            this.username = matcher.group(2);
            logger.info(username + " has a new follower " + follower);
            try {
                userDAO.find(username).orElseThrow(() -> new ServiceException(username + " not found"));
                userDAO.find(follower).orElseThrow(() -> new ServiceException(follower + " not found"));
                Optional<UserFollower> userFollowerOptional =
                        userFollowerDAO.findByUsernameAndFollower(username,follower);
                if(userFollowerOptional.isPresent()){
                    return follower+" is already following "+username;
                }else {
                    userFollowerDAO.insert(new UserFollower(username, follower));
                }
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

    public String getFollower() {
        return follower;
    }

    protected LocalDateTime getNow(){
        return LocalDateTime.now();
    }
}
