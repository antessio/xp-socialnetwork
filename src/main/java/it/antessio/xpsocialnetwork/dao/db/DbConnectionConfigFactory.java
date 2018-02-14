package it.antessio.xpsocialnetwork.dao.db;


import it.antessio.xpsocialnetwork.exception.DAOException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class DbConnectionConfigFactory {

    private static final String DEFAULT_PROPERTIES_FILE_NAME = "db.properties";
    private String propertiesFileName;

    public DbConnectionConfigFactory() {
        propertiesFileName = DEFAULT_PROPERTIES_FILE_NAME;
    }
    public DbConnectionConfigFactory(String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
    }

    public String createConfig() {
        Properties dbProperties = new Properties();
        URL propertiesUrl = DbConnectionConfigFactory.class.getClassLoader().getResource(propertiesFileName);
        if(propertiesUrl == null){
            throw new DAOException("Unable to load the database configuration");
        }
        try (InputStream stream=propertiesUrl.openStream()){
            dbProperties.load(stream);
        } catch (IOException e) {
            throw new DAOException("Unable to load the databse configuration");
        }
        return dbProperties.getProperty("connection");
    }
}
