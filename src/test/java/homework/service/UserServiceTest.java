package homework.service;

import homework.dao.UserDao;
import homework.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDaoMock;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetAllUsers() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setName("Ravil");
        user.setAge(23);
        user.setEmail("@eagle");
        user.setCreatedAt(created);
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userDaoMock.getUsers()).thenReturn(users);
        List<User> allUsers = userService.getAllUsers();

        assertNotNull(allUsers);
        assertEquals(allUsers.size(), users.size());
    }

    @Test
    public void testGetUserById() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setId(1L);
        user.setName("Ravil");
        user.setAge(23);
        user.setEmail("@eagle");
        user.setCreatedAt(created);

        when(userDaoMock.getUserById(1L)).thenReturn(Optional.of(user));
        User userById = userService.getUserById(1L);

        assertNotNull(userById);

        assertEquals(userById.getName(), user.getName());
        assertEquals(userById.getAge(), user.getAge());
        assertEquals(userById.getEmail(), user.getEmail());
        assertEquals(userById.getCreatedAt(), user.getCreatedAt());
    }

    @Test
    public void testGetUserByIdThrowsException() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUserById(-1L),
                "Ожидалось исключение IllegalArgumentException при попытке передать отрицательный id");
        assertTrue(thrown.getMessage().contains("ID должен быть положительным числом"));
    }

    @Test
    public void testAddUser() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setId(1L);
        user.setName("Ravil");
        user.setAge(23);
        user.setEmail("@eagle");
        user.setCreatedAt(created);

        userService.addUser(user);

        verify(userDaoMock).saveUser(user);
    }

    @Test
    void testAddUserWithNullUser() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.addUser(null));
        assertEquals("Пользователь не может быть null", thrown.getMessage());
    }

    @Test
    void testAddUserWithEmptyName() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setId(1L);
        user.setName(" ");
        user.setAge(23);
        user.setEmail("@eagle");
        user.setCreatedAt(created);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.addUser(user));
        assertEquals("Имя пользователя не может быть пустым", thrown.getMessage());
    }

    @Test
    void testAddUserWithInvalidEmail() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setId(1L);
        user.setName("Ravil");
        user.setAge(23);
        user.setEmail("eagle");
        user.setCreatedAt(created);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.addUser(user));
        assertEquals("Некорректный email", thrown.getMessage());
    }

    @Test
    void testAddUserWithInvalidAge() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setId(1L);
        user.setName("Ravil");
        user.setAge(-23);
        user.setEmail("@eagle");
        user.setCreatedAt(created);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.addUser(user));
        assertEquals("Возраст должен быть положительным числом", thrown.getMessage());
    }

    @Test
    public void testUpdateUser() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setId(1L);
        user.setName("Ravil");
        user.setAge(23);
        user.setEmail("@eagle");
        user.setCreatedAt(created);

        userService.updateUser(user);

        verify(userDaoMock).updateUser(user);
    }

    @Test
    public void testDeleteUser() {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setId(1L);
        user.setName("Ravil");
        user.setAge(23);
        user.setEmail("@eagle");
        user.setCreatedAt(created);

        when(userDaoMock.getUserById(user.getId())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());

        verify(userDaoMock).deleteUser(user);
    }

    @Test
    public void testDeleteUserThrowsException() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(-1L),
                "Ожидалось исключение IllegalArgumentException при попытке передать отрицательный id");
        assertTrue(thrown.getMessage().contains("ID должен быть положительным числом"));
    }

    @Test
    public void testDeleteUserNullThrowsException() {
        when(userDaoMock.getUserById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(1L),
                "Ожидалось исключение IllegalArgumentException при попытке передать id отсутствующего пользователя"
                );
        assertEquals("Пользователь с ID 1 не найден", thrown.getMessage());
    }
}