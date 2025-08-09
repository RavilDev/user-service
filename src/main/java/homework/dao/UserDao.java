package homework.dao;

import homework.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> getUsers();

    Optional<User> getUserById(long id);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(User user);
}
