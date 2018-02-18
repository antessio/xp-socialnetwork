package it.antessio.xpsocialnetwork.dao.db;


import it.antessio.xpsocialnetwork.exception.DAOException;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class DatabaseUtilsTest {

    private DatabaseUtils factory;

    @Before
    public void setUp(){
        factory = DatabaseUtilsFactory.getInstance();
    }
    @Test
    public void createDBConfig() throws DAOException {
        String url = factory.getDatabaseUrl();
        assertThat(url).isEqualTo("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    }
}
