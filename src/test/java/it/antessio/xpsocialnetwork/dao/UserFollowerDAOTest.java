package it.antessio.xpsocialnetwork.dao;


import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.model.UserFollower;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class UserFollowerDAOTest extends AbstractDAOTest{

    private UserFollowerDAO dao;

    @Before
    public void setUp(){
        super.setUp();
        dao = new UserFollowerDAO();
    }

    @Test
    public void testInsertExistingUsers() throws DAOException {
        dao.insert(new UserFollower("Bob","Alice"));
        List<Map<String,Object>> userFollowers = queryAll("user_follower");
        assertThat(userFollowers.stream().filter(
                e->e.get("username").equals("Bob")&&e.get("follower").equals("Alice")
        ).findFirst().isPresent()).isTrue();
    }

    @Test
    public void testInsertNotExistingFollower()throws Exception{
        assertThatThrownBy(
                ()->dao.insert(new UserFollower("Bob","Antonio"))
        ).isInstanceOf(DAOException.class);
    }
    @Test
    public void testInsertNotExistingUsername()throws Exception{
        assertThatThrownBy(
                ()->dao.insert(new UserFollower("Antonio","Bob"))
        ).isInstanceOf(DAOException.class);
    }

    @Test
    public void testInsertDuplicateFollower()throws Exception{
        assertThatThrownBy(
                ()->dao.insert(new UserFollower("Alice","Bob"))
        ).isInstanceOf(DAOException.class);
    }




}
