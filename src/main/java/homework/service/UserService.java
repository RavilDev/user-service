package homework.service;

import homework.dao.UserDao;
import homework.entity.User;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class UserService {
    private final UserDao userDao;

    public List<User> getAllUsers() {
        return userDao.getUsers();
    }

    public User getUserById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть положительным числом");
        }
        return userDao.getUserById(id).orElse(null);
    }

    public void addUser(User user) {
        validateUser(user);
        userDao.saveUser(user);
    }

    public void updateUser(User user) {
        validateUser(user);
        userDao.updateUser(user);
    }

    public void deleteUser(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть положительным числом");
        }
        User user = userDao.getUserById(id).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с ID " + id + " не найден");
        }
        userDao.deleteUser(user);
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не может быть null");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Некорректный email");
        }
        if (user.getAge() <= 0) {
            throw new IllegalArgumentException("Возраст должен быть положительным числом");
        }
    }
}
