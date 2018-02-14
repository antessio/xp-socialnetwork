package xpsocialnetwork.dao.db;


import it.antessio.xpsocialnetwork.dao.db.DbConnectionConfigFactory;
import it.antessio.xpsocialnetwork.exception.DAOException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class DbConnectionConfigFactoryTest {

    private DbConnectionConfigFactory factory;

    @Before
    public void setUp(){
        factory = new DbConnectionConfigFactory();
    }
    @Test
    public void createDBConfig() {
        String url = factory.createConfig();
        assertThat(url).isEqualTo("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    }
    @Test
    public void unexpectedMissingProperties(){
        factory=new DbConnectionConfigFactory("unexistingFile.properties");
        assertThatThrownBy(
                ()-> factory.createConfig()
        ).isInstanceOf(DAOException.class)
        .matches(e->e.getMessage().equals("Unable to load the database configuration"));

    }
}
