package it.antessio.xpsocialnetwork.dao.db;


import it.antessio.xpsocialnetwork.exception.DAOException;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabseUtils {

    private static final String DEFAULT_PROPERTIES_FILE_NAME = "db.properties";
    private String propertiesFileName;

    public DatabseUtils() {
        propertiesFileName = DEFAULT_PROPERTIES_FILE_NAME;
    }
    public DatabseUtils(String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
    }
    public void setUpDB(String sql) throws DAOException {
        try (Connection connection = DriverManager.getConnection(getDatabaseUrl())) {
            Statement statement = connection.createStatement();
            statement.addBatch(sql);
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }
    public String readSQLContent(URL scriptURL){
        StringBuilder sql=new StringBuilder();
        try {
            FileReader fr = new FileReader(new File(scriptURL.toURI().getPath()));
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            //read the SQL file line by line
            while ((line = br.readLine()) != null) {
                sql.append(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return sql.toString();
    }

    public String getDatabaseUrl() throws DAOException {
        Properties dbProperties = new Properties();
        URL propertiesUrl = DatabseUtils.class.getClassLoader().getResource(propertiesFileName);
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
