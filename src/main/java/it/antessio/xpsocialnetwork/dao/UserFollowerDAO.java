package it.antessio.xpsocialnetwork.dao;

import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.model.UserFollower;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserFollowerDAO extends AbstractDAO{


    public void insert(UserFollower userFollower) throws DAOException {
        if (userFollower == null ||
                StringUtils.isBlank(userFollower.getUsername()) ||
                StringUtils.isBlank(userFollower.getFollower())) {
            throw new IllegalArgumentException("Invalid parameter user follower: "+userFollower);
        }
        String sql = "INSERT INTO user_follower (username, follower) VALUES (?,?) ";
        try(Connection connection = DriverManager.getConnection(connectionConfigFactory.getDatabaseUrl())){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, userFollower.getUsername());
            ps.setString(2, userFollower.getFollower());
            ps.executeUpdate();
        }catch(SQLException e){
            throw new DAOException(e);
        }
    }
}
