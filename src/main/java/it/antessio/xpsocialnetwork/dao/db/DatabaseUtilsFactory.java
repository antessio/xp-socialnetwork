package it.antessio.xpsocialnetwork.dao.db;

public class DatabaseUtilsFactory {

    private DatabaseUtilsFactory(){

    }

    public static DatabaseUtils getInstance(){
        return new DatabaseUtils();
    }
}
