package it.antessio.xpsocialnetwork.dao;

import it.antessio.xpsocialnetwork.dao.db.DbConnectionConfigFactory;
import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.model.UserPost;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserPostDAO {

    private final String TABLE_NAME = "user_post";
    private DbConnectionConfigFactory connectionConfigFactory;

    public UserPostDAO(){
        connectionConfigFactory = new DbConnectionConfigFactory();
    }
    public void insertPost(UserPost userPost){
        try(Connection connection = DriverManager.getConnection(connectionConfigFactory.createConfig())){
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
    public List<UserPost> findPostsByUser(String username){
        List<UserPost> userPostList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(connectionConfigFactory.createConfig())){
            PreparedStatement ps = connection.prepareStatement("SELECT id,username, content, created " +
                    "FROM "+TABLE_NAME+" WHERE username = ? ORDER BY created");
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
}
