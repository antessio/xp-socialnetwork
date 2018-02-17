package it.antessio.xpsocialnetwork.dao.db;


import it.antessio.xpsocialnetwork.exception.ApplicationRuntimeException;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseUtils {

    private static final String DEFAULT_PROPERTIES_FILE_NAME = "db.properties";
    private String propertiesFileName;

    DatabaseUtils() {
        propertiesFileName = DEFAULT_PROPERTIES_FILE_NAME;
    }

    public void setUpDB(String sql) {
        try (Connection connection = DriverManager.getConnection(getDatabaseUrl())) {
            Statement statement = connection.createStatement();
            statement.addBatch(sql);
            statement.executeBatch();
        } catch (SQLException  e) {
            throw new ApplicationRuntimeException("Unable to load the database", e);
        }
    }

    public String readSQLContent(URL scriptURL) {
        StringBuilder sql = new StringBuilder();
        try {
            FileReader fr = new FileReader(new File(scriptURL.toURI().getPath()));
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            //read the SQL file line by line
            while ((line = br.readLine()) != null) {
                sql.append(line);
            }
        }catch(IOException | URISyntaxException e){
            throw new ApplicationRuntimeException("Unable to read the content of the script", e);
        }
        return sql.toString();
    }

    public String getDatabaseUrl() {
        Properties dbProperties = new Properties();
        URL propertiesUrl = DatabaseUtils.class.getClassLoader().getResource(propertiesFileName);
        if (propertiesUrl == null) {
            throw new ApplicationRuntimeException("Unable to load the database configuration");
        }
        try (InputStream stream = propertiesUrl.openStream()) {
            dbProperties.load(stream);
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Unable to load the databse configuration");
        }
        return dbProperties.getProperty("connection");
    }


}
