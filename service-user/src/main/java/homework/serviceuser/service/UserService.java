package homework.serviceuser.service;

import homework.serviceuser.dto.request.UserRequestTo;
import homework.serviceuser.dto.response.UserResponseTo;

import java.util.List;

public interface UserService {
    List<UserResponseTo> getAllUsers();

    UserResponseTo getUserById(Long id);

    UserResponseTo addUser(UserRequestTo userRequestTo);

    UserResponseTo updateUser(Long id, UserRequestTo userRequestTo);

    void deleteUser(Long id);
}
