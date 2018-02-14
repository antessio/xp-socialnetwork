package it.antessio.xpsocialnetwork.dao;

import it.antessio.xpsocialnetwork.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

public class UserDAO {

    public void insert(String username){
//        Connection connection = DriverManager.getConnection()
    }

    public Optional<User> find(String username) {
        return Optional.empty();
    }
}
