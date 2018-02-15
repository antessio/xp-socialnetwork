package xpsocialnetwork.dao.db;


import it.antessio.xpsocialnetwork.dao.db.DatabseUtils;
import it.antessio.xpsocialnetwork.exception.DAOException;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class DatabaseUtilsTest {

    private DatabseUtils factory;

    @Before
    public void setUp(){
        factory = new DatabseUtils();
    }
    @Test
    public void createDBConfig() throws DAOException {
        String url = factory.getDatabaseUrl();
        assertThat(url).isEqualTo("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    }
    @Test
    public void unexpectedMissingProperties(){
        factory=new DatabseUtils("unexistingFile.properties");
        assertThatThrownBy(
                ()-> factory.getDatabaseUrl()
        ).isInstanceOf(DAOException.class)
        .matches(e->e.getMessage().equals("Unable to load the database configuration"));

    }
}
