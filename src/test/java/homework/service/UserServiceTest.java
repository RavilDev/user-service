package homework.service;

import homework.dto.request.UserRequestTo;
import homework.dto.response.UserResponseTo;
import homework.entity.User;
import homework.mapper.UserMapper;
import homework.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static homework.util.DataUtil.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UserMapper userMapperMock;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetAllUsers() {
        User user = getUser();

        UserResponseTo userResponseTo = getUserResponseTo();

        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepositoryMock.findAll()).thenReturn(users);
        when(userMapperMock.toUserResponseTo(user)).thenReturn(userResponseTo);
        List<UserResponseTo> allUsers = userService.getAllUsers();

        assertNotNull(allUsers);
        assertEquals(allUsers.size(), users.size());
    }

    @Test
    public void testGetUserById() {
        User user = getUser();

        UserResponseTo userResponseTo = getUserResponseTo();

        when(userMapperMock.toUserResponseTo(user)).thenReturn(userResponseTo);
        when(userRepositoryMock.findById(ID)).thenReturn(Optional.of(user));
        UserResponseTo userById = userService.getUserById(ID);

        assertNotNull(userById);

        assertEquals(userById.getName(), userResponseTo.getName());
        assertEquals(userById.getAge(), userResponseTo.getAge());
        assertEquals(userById.getEmail(), userResponseTo.getEmail());
        assertEquals(userById.getCreatedAt(), userResponseTo.getCreatedAt());
    }

    @Test
    public void testGetUserByIdThrowsException() {
        when(userRepositoryMock.findById(ID)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUserById(ID),
                "Ожидалось исключение IllegalArgumentException при попытке передать id отсутствующего пользователя");
        assertTrue(thrown.getMessage().contains("Пользователь с ID 1 не найден"));
    }

    @Test
    public void testAddUser() {
        User user = getUser();

        UserRequestTo userRequestTo = getUserRequestTo();

        when(userMapperMock.toUser(userRequestTo)).thenReturn(user);

        userService.addUser(userRequestTo);

        verify(userRepositoryMock).save(user);
    }

    @Test
    public void testUpdateUser() {
        User user = getUser();

        UserRequestTo userRequestTo = getUserRequestTo();

        UserResponseTo userResponseTo = getUserResponseTo();

        when(userRepositoryMock.findById(ID)).thenReturn(Optional.of(user));
        when(userMapperMock.toUserResponseTo(user)).thenReturn(userResponseTo);

        userService.updateUser(ID, userRequestTo);

        verify(userRepositoryMock).save(user);
    }

    @Test
    public void testUpdateUserThrownException() {
        UserRequestTo userRequestTo = getUserRequestTo();

        when(userRepositoryMock.findById(ID)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(ID, userRequestTo),
                "Ожидалось исключение IllegalArgumentException при попытке передать id отсутствующего пользователя"
        );
        assertEquals("Пользователь с ID 1 не найден", thrown.getMessage());
    }

    @Test
    public void testDeleteUser() {
        User user = getUser();

        when(userRepositoryMock.findById(user.getId())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());

        verify(userRepositoryMock).delete(user);
    }

    @Test
    public void testDeleteUserThrowsException() {
        when(userRepositoryMock.findById(ID)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(ID),
                "Ожидалось исключение IllegalArgumentException при попытке передать id отсутствующего пользователя"
                );
        assertEquals("Пользователь с ID 1 не найден", thrown.getMessage());
    }
}