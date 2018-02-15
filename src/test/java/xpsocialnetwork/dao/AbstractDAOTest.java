package xpsocialnetwork.dao;


import it.antessio.xpsocialnetwork.dao.db.DatabseUtils;
import it.antessio.xpsocialnetwork.exception.DAOException;
import org.junit.After;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.assertj.core.api.Java6Assertions.fail;

public abstract class AbstractDAOTest {

    protected final String URL;
    public AbstractDAOTest(){
        try {
            URL = new DatabseUtils().getDatabaseUrl();
        }catch(DAOException e){
            throw new RuntimeException("Unable to load the database configuration", e);
        }
    }

    @Before
    public void setUp(){
        createTestDB();
    }

    @After
    public void tearDown(){
        dropTestDB();
    }


    private List<String> createQueries(URL scriptURL) {
        String queryLine = "";
        StringBuffer sBuffer = new StringBuffer();
        List<String> sql  = new ArrayList<>();
//        StringBuilder sql = new StringBuilder();

        try {
            FileReader fr = new FileReader(new File(scriptURL.toURI().getPath()));
            BufferedReader br = new BufferedReader(fr);

            //read the SQL file line by line
            while ((queryLine = br.readLine()) != null) {
                // ignore comments beginning with #
                int indexOfCommentSign = queryLine.indexOf('#');
                if (indexOfCommentSign != -1) {
                    if (queryLine.startsWith("#")) {
                        queryLine = "";
                    } else
                        queryLine = queryLine.substring(0, indexOfCommentSign - 1);
                }
                // ignore comments beginning with --
                indexOfCommentSign = queryLine.indexOf("--");
                if (indexOfCommentSign != -1) {
                    if (queryLine.startsWith("--")) {
                        queryLine = "";
                    } else
                        queryLine = queryLine.substring(0, indexOfCommentSign - 1);
                }
                // ignore comments surrounded by /* */
                indexOfCommentSign = queryLine.indexOf("/*");
                if (indexOfCommentSign != -1) {
                    if (queryLine.startsWith("#")) {
                        queryLine = "";
                    } else
                        queryLine = queryLine.substring(0, indexOfCommentSign - 1);

                    sBuffer.append(queryLine + " ");
                    // ignore all characters within the comment
                    do {
                        queryLine = br.readLine();
                    }
                    while (queryLine != null && !queryLine.contains("*/"));
                    indexOfCommentSign = queryLine.indexOf("*/");
                    if (indexOfCommentSign != -1) {
                        if (queryLine.endsWith("*/")) {
                            queryLine = "";
                        } else
                            queryLine = queryLine.substring(indexOfCommentSign + 2, queryLine.length() - 1);
                    }
                }

                //  the + " " is necessary, because otherwise the content before and after a line break are concatenated
                // like e.g. a.xyz FROM becomes a.xyzFROM otherwise and can not be executed
                if (queryLine != null)
                    sBuffer.append(queryLine + " ");
            }
            br.close();

            // here is our splitter ! We use ";" as a delimiter for each request
            String[] splittedQueries = sBuffer.toString().split(";");

            // filter out empty statements
            for (int i = 0; i < splittedQueries.length; i++) {
                if (!splittedQueries[i].trim().equals("") && !splittedQueries[i].trim().equals("\t")) {
                    sql.add(splittedQueries[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return sql.toString();
        return sql;
    }
    protected void dropTestDB() {
        try (Connection connection = DriverManager.getConnection(URL)) {

            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE user_follower");
            statement.execute("DROP TABLE  user_post");
            statement.execute("DROP TABLE user");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    protected void createTestDB() {
        URL initSqlURL = AbstractDAOTest.class.getClassLoader().getResource("init_db.sql");
        initDatabase(initSqlURL);
    }

    public void initDatabase(URL initSqlURL) {
        String sql = new DatabseUtils().readSQLContent(initSqlURL);
        try (Connection connection = DriverManager.getConnection(URL)) {
            Statement statement = connection.createStatement();
            statement.addBatch(sql);
            statement.executeBatch();
        } catch (SQLException e) {
            fail(e.getMessage(),e);
        }
    }


    protected List<Map<String,Object>> queryAll(String tableName) {
        List<Map<String,Object>> results = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tableName);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            while(rs.next()){
                HashMap<String,Object> row = new HashMap<>();
                for(int i=1; i<=columns; ++i) {
                    row.put(md.getColumnLabel(i).toLowerCase(),rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            fail(e.getMessage(), e);
        }
        return results;
    }
}
