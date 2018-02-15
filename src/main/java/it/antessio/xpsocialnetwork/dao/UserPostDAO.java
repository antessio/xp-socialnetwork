package it.antessio.xpsocialnetwork.dao;


import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.model.UserPost;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

public class UserPostDAO extends AbstractDAO{

    private final String TABLE_NAME = "user_post";


    public void insertPost(UserPost userPost) throws DAOException {
        if(userPost == null ||
                StringUtils.isBlank(userPost.getContent()) ||
                StringUtils.isBlank(userPost.getUsername()) ||
                userPost.getCreated()==null){
            throw new IllegalArgumentException("Invalid user post as parameter: "+userPost);
        }
        try(Connection connection = DriverManager.getConnection(connectionConfigFactory.getDatabaseUrl())){
            PreparedStatement ps = connection.prepareStatement("INSERT INTO user_post (username, content, created) " +
                    "VALUES (?,?,?) ");
            ps.setString(1,userPost.getUsername());
            ps.setString(2,userPost.getContent());
            ps.setTimestamp(3, Timestamp.valueOf(userPost.getCreated()));
            ps.executeUpdate();
        }catch(SQLException e){
            throw new DAOException("Unable to find user's post",e);
        }
    }
    public List<UserPost> findPostsByUser(String username) throws DAOException {
        if(StringUtils.isBlank(username)){
            throw new IllegalArgumentException("Invalid username as parmeter: "+username);
        }
        List<UserPost> userPostList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(connectionConfigFactory.getDatabaseUrl())){
            PreparedStatement ps = connection.prepareStatement("SELECT id,username, content, created " +
                    "FROM "+TABLE_NAME+" WHERE username = ? ORDER BY created DESC");
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                userPostList.add(fromRs(rs));
            }
        }catch(SQLException e){
            throw new DAOException("Unable to find user's post",e);
        }
        return userPostList;
    }
    private UserPost fromRs(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String username= rs.getString("username");
        String content = rs.getString("content");
        LocalDateTime createdAt = rs.getTimestamp("created").toLocalDateTime();
        return new UserPost(id,username,content,createdAt);
    }

    public List<UserPost> getWall(String username) throws DAOException {
        if(StringUtils.isBlank(username)){
            throw new IllegalArgumentException("Invalid username as parmeter: "+username);
        }
        List<UserPost> userPostList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(connectionConfigFactory.getDatabaseUrl())){
            PreparedStatement ps = connection.prepareStatement("SELECT up.id, up.username, up.content, up.created " +
                    "FROM "+TABLE_NAME+" up LEFT OUTER JOIN user_follower uf " +
                    "ON (uf.username=up.username) WHERE (up.username = ? OR uf.follower=?) " +
                    "ORDER BY created DESC");
            ps.setString(1,username);
            ps.setString(2,username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                userPostList.add(fromRs(rs));
            }
        }catch(SQLException e){
            throw new DAOException("Unable to find user's post",e);
        }
        return userPostList;
    }
}
