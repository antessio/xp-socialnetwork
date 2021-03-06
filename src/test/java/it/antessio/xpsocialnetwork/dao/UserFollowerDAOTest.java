package it.antessio.xpsocialnetwork.dao;


import it.antessio.xpsocialnetwork.exception.DAOException;
import it.antessio.xpsocialnetwork.model.UserFollower;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Test
    public void testFindByUsernameAndFollower()throws Exception{
        String username = "Alice";
        String follower = "Bob";
        Optional<UserFollower> userFollowerOptional = dao.findByUsernameAndFollower(username,follower);
        assertThat(userFollowerOptional.isPresent()).isTrue();
        UserFollower userFollower = userFollowerOptional.get();
        assertThat(userFollower.getFollower()).isEqualTo(follower);
        assertThat(userFollower.getUsername()).isEqualTo(username);
    }
    @Test
    public void testFindByUsernameAndFollower_notFound()throws Exception{
        String username = "Alice";
        String follower = "Nicola";
        Optional<UserFollower> userFollowerOptional = dao.findByUsernameAndFollower(username,follower);
        assertThat(userFollowerOptional.isPresent()).isFalse();
        username = "Nicola";
        follower = "Alice";
        userFollowerOptional = dao.findByUsernameAndFollower(username,follower);
        assertThat(userFollowerOptional.isPresent()).isFalse();
    }




}
