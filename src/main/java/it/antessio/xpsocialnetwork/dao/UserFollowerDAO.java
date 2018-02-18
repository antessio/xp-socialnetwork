package it.antessio.xpsocialnetwork.dao;

import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.model.UserFollower;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.Optional;

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
            int affectedRows = ps.executeUpdate();
            logger.debug("Update follower affected rows: "+affectedRows);
        }catch(SQLException e){
            throw new DAOException(e);
        }
    }

    public Optional<UserFollower> findByUsernameAndFollower(String username, String follower) throws DAOException {
        if(username == null || follower == null){
            throw new IllegalArgumentException("Invalid parameter username or follower");
        }
        String sql = "SELECT id,follower,username FROM user_follower WHERE username=? AND follower=?";
        try(Connection connection = DriverManager.getConnection(connectionConfigFactory.getDatabaseUrl())){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,follower);
            ResultSet rs = ps.executeQuery();
            return rs.next()?Optional.of(fromResultSet(rs)):Optional.empty();
        }catch (SQLException e){
            throw new DAOException(e);
        }
    }

    private UserFollower fromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String username = rs.getString("username");
        String follower = rs.getString("follower");
        return new UserFollower(id,username,follower);
    }
}
