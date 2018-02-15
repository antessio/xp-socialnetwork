package it.antessio.xpsocialnetwork.dao;

import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.model.User;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserDAO extends AbstractDAO {

    private String TABLE_NAME="user";


    public void insert(User user) throws DAOException {
        if(user==null || StringUtils.isBlank(user.getUsername()) || user.getCreatedAt()==null){
            throw new IllegalArgumentException("Invalid user as parameter: "+user);
        }
        String sql = "INSERT INTO "+TABLE_NAME+" (username, created_at) VALUES (?,?) ";
        try(Connection connection = DriverManager.getConnection(connectionConfigFactory.getDatabaseUrl())){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setTimestamp(2, Timestamp.valueOf(user.getCreatedAt()));
            ps.executeUpdate();
        }catch (SQLException e){
            throw new DAOException(e.getMessage(),e);
        }
    }

    public Optional<User> find(String username) throws DAOException {
        if(StringUtils.isBlank(username)){
            throw new IllegalArgumentException("Invalid username as parameter: "+username);
        }
        String sql = "SELECT username, created_at FROM "+TABLE_NAME+" WHERE username=?";
        try(Connection connection = DriverManager.getConnection(connectionConfigFactory.getDatabaseUrl())){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            return rs.next()?Optional.of(fromRs(rs)):Optional.empty();
        }catch(SQLException e){
            throw new DAOException(e.getMessage(),e);
        }
    }

    private User fromRs(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        return new User(username, createdAt);
    }
}
