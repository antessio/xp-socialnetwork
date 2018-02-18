package it.antessio.xpsocialnetwork.service.handler;

import it.antessio.xpsocialnetwork.dao.UserDAO;
import it.antessio.xpsocialnetwork.dao.UserFollowerDAO;
import it.antessio.xpsocialnetwork.dao.UserPostDAO;
import it.antessio.xpsocialnetwork.model.UserPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractHandler implements CommandHandler {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractHandler.class);

    protected final UserDAO userDAO;
    protected final UserPostDAO userPostDAO;
    protected final UserFollowerDAO userFollowerDAO;
    protected Pattern pattern;
    public static final Pattern INSERT_POST_PATTERN = Pattern.compile("^([a-zA-Z0-9]+)\\s-\\>\\s(.*)$");
    public static final Pattern LIST_POST_PATTERN = Pattern.compile("^([a-zA-Z0-9]+)$");
    public static  final Pattern FOLLOWS_PATTERN = Pattern.compile("^([a-zA-Z0-9]+)\\sfollows\\s([a-zA-Z0-9]+)$");
    public  static final Pattern WALL_PATTERN = Pattern.compile("^([a-zA-Z0-9]+)\\swall$");

    public AbstractHandler(UserDAO userDAO, UserPostDAO userPostDAO, UserFollowerDAO userFollowerDAO,Pattern pattern){
        this.userDAO = userDAO;
        this.userPostDAO = userPostDAO;
        this.userFollowerDAO = userFollowerDAO;
        this.pattern = pattern;
    }

    public AbstractHandler(Pattern pattern){
        this(new UserDAO(),new UserPostDAO(),new UserFollowerDAO(),pattern);
    }


    protected String collectUserPosts(List<UserPost> userPostList, boolean addUsername){
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
