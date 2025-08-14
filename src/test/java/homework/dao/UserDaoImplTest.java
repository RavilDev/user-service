package homework.dao;

import homework.config.TestContainerConfig;
import homework.entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoImplTest {

    private static UserDao userDao;

    @BeforeAll
    static void setUpBeforeClass() {
        userDao = new UserDaoImpl(TestContainerConfig.getSessionFactory());
    }

    @AfterAll
    static void setUpAfterClass() {
        TestContainerConfig.stop();
    }

    @Test
    public void testSaveUser() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setName("Ravil1");
        user.setAge(23);
        user.setEmail("@eagle1");
        user.setCreatedAt(created);
        userDao.saveUser(user);

        User foundUser = userDao.getUserById(user.getId()).orElse(null);
        assertNotNull(foundUser);
        assertEquals(foundUser.getName(), user.getName());
        assertEquals(foundUser.getAge(), user.getAge());
        assertEquals(created.getTime(), foundUser.getCreatedAt().getTime());
    }

    @Test
    public void testSaveUserThrowException() {
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> userDao.saveUser(null),
                "Ожидалось исключение RuntimeException при попытке сохранить null объект User"
        );

        assertTrue(thrown.getMessage().contains("Ошибка при добавлении пользователя"));
        assertInstanceOf(IllegalArgumentException.class, thrown.getCause());
    }

    @Test
    public void testGetUsers() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setName("Ravil2");
        user.setAge(23);
        user.setEmail("@eagle2");
        user.setCreatedAt(created);

        userDao.saveUser(user);

        assertEquals(1, userDao.getUsers().size());
    }

    @Test
    public void testGetUserById() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setName("Ravil3");
        user.setAge(23);
        user.setEmail("@eagle3");
        user.setCreatedAt(created);

        userDao.saveUser(user);
        long savedId = user.getId();

        User foundUser = userDao.getUserById(savedId).orElse(null);

        assertNotNull(foundUser);
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getAge(), foundUser.getAge());
        assertEquals(created.getTime(), foundUser.getCreatedAt().getTime());
    }

    @Test
    public void testGetUserByIdThrowException() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setName("Ravil4");
        user.setAge(23);
        user.setEmail("@eagle4");
        user.setCreatedAt(created);

        userDao.saveUser(user);

        Optional<User> foundUser = userDao.getUserById(999L);
        assertFalse(foundUser.isPresent(), "Пользователь с несуществующим ID не должен быть найден");
    }

    @Test
    public void testUpdateUser() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setName("Ravil5");
        user.setAge(23);
        user.setEmail("@eagle5");
        user.setCreatedAt(created);
        userDao.saveUser(user);

        User updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setName("Ravil5");
        updatedUser.setAge(24);
        updatedUser.setEmail("@eagle5");
        updatedUser.setCreatedAt(created);

        userDao.updateUser(updatedUser);

        User foundUser = userDao.getUserById(updatedUser.getId()).orElse(null);
        assertNotNull(foundUser);
        assertEquals(updatedUser.getName(), foundUser.getName());
        assertEquals(updatedUser.getAge(), foundUser.getAge());
        assertEquals(created.getTime(), foundUser.getCreatedAt().getTime());
    }

    @Test
    public void updateUserThrowException() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User updatedUser = new User();
        updatedUser.setId(999L);
        updatedUser.setName("Ravil Updated");
        updatedUser.setAge(24);
        updatedUser.setEmail("@eagle6");
        updatedUser.setCreatedAt(created);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> userDao.updateUser(updatedUser),
                "Ожидалось исключение RuntimeException при попытке обновить несуществующего пользователя"
        );
        assertTrue(thrown.getMessage().contains("Ошибка при обновлении пользователя"));
    }

    @Test
    public void testDeleteUser() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setName("Ravil7");
        user.setAge(23);
        user.setEmail("@eagle7");
        user.setCreatedAt(created);
        long savedId = user.getId();

        userDao.deleteUser(user);

        Optional<User> foundUser = userDao.getUserById(savedId);
        assertFalse(foundUser.isPresent(), "Пользователь должен быть удалён из базы");
    }

    @Test
    public void testDeleteUserThrowException() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userDao.deleteUser(null),
                "Ожидалось исключение IllegalArgumentException при попытке удалить null пользователя"
        );
        assertEquals("Пользователь не может быть null", thrown.getMessage());
    }

}