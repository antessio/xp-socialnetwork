package it.antessio.xpsocialnetwork.service;

import it.antessio.xpsocialnetwork.dao.UserDAO;
import it.antessio.xpsocialnetwork.dao.UserFollowerDAO;
import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.exception.ServiceException;
import it.antessio.xpsocialnetwork.model.User;
import it.antessio.xpsocialnetwork.model.UserFollower;
import it.antessio.xpsocialnetwork.model.UserPost;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandService {
    private final Pattern INSERT_POST_PATTERN = Pattern.compile("^([a-zA-Z0-9]+)\\s-\\>\\s(.*)$");
    private final Pattern LIST_POST_PATTERN = Pattern.compile("^([a-zA-Z0-9]+)$");
    private final Pattern FOLLOWS_PATTERN = Pattern.compile("^([a-zA-Z0-9]+)\\sfollows\\s([a-zA-Z0-9]+)$");
    private final Pattern WALL_PATTERN = Pattern.compile("^([a-zA-Z0-9]+)\\swall$");
    private final UserDAO userDAO;
    private final UserPostDAO userPostDAO;
    private final UserFollowerDAO userFollowerDAO;

    public CommandService(){
        this.userPostDAO = new UserPostDAO();
        this.userDAO = new UserDAO();
        this.userFollowerDAO = new UserFollowerDAO();
    }
    public CommandService(UserPostDAO userPostDAO, UserDAO userDAO,UserFollowerDAO userFollowerDAO) {
        this.userPostDAO = userPostDAO;
        this.userDAO = userDAO;
        this.userFollowerDAO = userFollowerDAO;
    }

    public String handle(String command) throws ServiceException{
        Matcher insertPostMatcher = INSERT_POST_PATTERN.matcher(command);
        Matcher listPostMatcher = LIST_POST_PATTERN.matcher(command);
        Matcher followsMatcher = FOLLOWS_PATTERN.matcher(command);
        Matcher wallPattern = WALL_PATTERN.matcher(command);
        //TODO apply a design pattern (ChainOfResponsability?)
        try {
            if (insertPostMatcher.matches()) {
                String username = insertPostMatcher.group(1);
                String content = insertPostMatcher.group(2);
                LocalDateTime created = getNow();
                if (!userDAO.find(username).isPresent()) {
                    userDAO.insert(new User(username, getNow()));
                }
                userPostDAO.insertPost(new UserPost(username, content, created));
            } else if (listPostMatcher.matches()) {
                String username = listPostMatcher.group();
                userDAO.find(username).orElseThrow(() -> new ServiceException(username + " not found"));
                List<UserPost> userPostList = userPostDAO.findPostsByUser(username);
                return collectUserPosts(userPostList, false);
            } else if (followsMatcher.matches()) {
                String follower = followsMatcher.group(1);
                String username = followsMatcher.group(2);
                userDAO.find(username).orElseThrow(() -> new ServiceException(username + " not found"));
                userDAO.find(follower).orElseThrow(() -> new ServiceException(follower + " not found"));
                userFollowerDAO.insert(new UserFollower(username, follower));
            } else if (wallPattern.matches()) {
                String username = wallPattern.group(1);
                userDAO.find(username).orElseThrow(() -> new ServiceException(username + " not found"));
                return collectUserPosts(userPostDAO.getWall(username), true);
            }
        }catch(DAOException e){
            throw new ServiceException(e);
        }
        return "";
    }
    private String collectUserPosts(List<UserPost> userPostList, boolean addUsername){
        return userPostList.stream().map(
                userPost -> {
                    long days = userPost.getCreated().until(getNow(), ChronoUnit.DAYS);
                    long hours = userPost.getCreated().until(getNow(), ChronoUnit.HOURS);
                    long minutes = userPost.getCreated().until(getNow(), ChronoUnit.MINUTES);
                    long seconds = userPost.getCreated().until(getNow(), ChronoUnit.SECONDS);

                    String when = seconds+" second"+(seconds>1?"s":"")+" ago";
                    if(days>0){
                        when = days+" day"+(days>1?"s":"")+" ago";
                    }else if (hours>0){
                        when = hours+" hour"+(hours>1?"s":"")+" ago";
                    }else if (minutes >0){
                        when = minutes+" minute"+(minutes>1?"s":"")+" ago";
                    }

                    return (addUsername ?userPost.getUsername()+" - ":"")+ userPost.getContent()+" ("+when+")";

                }
        ).collect(Collectors.joining("\n"));
    }
    protected LocalDateTime getNow(){
        return LocalDateTime.now();
    }
}
