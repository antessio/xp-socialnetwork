package it.antessio.xpsocialnetwork.dao;

import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.model.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UserDAOTest extends AbstractDAOTest {

    private UserDAO dao;

    @Before
    public void setUp(){
        super.setUp();
        dao = new UserDAO();
    }

    @Test
    public void findUser() throws DAOException {
        Optional<User> userOptional = dao.find("Alice");
        assertThat(userOptional.isPresent()).isTrue();
        User user = userOptional.get();
        assertThat(user.getUsername()).isEqualTo("Alice");
        assertThat(user.getCreatedAt()).isEqualTo(LocalDateTime.of(2018,2,1,9,0,0));
    }

    @Test
    public void findNotExistingUser() throws DAOException {
        Optional<User> userOptional = dao.find("Antonio");
        assertThat(userOptional.isPresent()).isFalse();
    }

    @Test
    public void insertNotExistingUser() throws DAOException {
        LocalDateTime now = LocalDateTime.now();
        User user = new User("Antonio",now);
        dao.insert(user);
        List<Map<String,Object>> users = queryAll("user");
        assertThat(
                users.stream()
                        .filter(e-> e.get("username").equals("Antonio"))
                        .findFirst()
                        .isPresent()
        ).isTrue();
    }
}
