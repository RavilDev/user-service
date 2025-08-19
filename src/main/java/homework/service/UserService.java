package homework.service;

import homework.dto.request.UserRequestTo;
import homework.dto.response.UserResponseTo;
import homework.entity.User;
import homework.mapper.UserMapper;
import homework.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<UserResponseTo> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponseTo).toList();
    }

    public UserResponseTo getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + id + " не найден"));
        return userMapper.toUserResponseTo(user);
    }

    public UserResponseTo addUser(UserRequestTo userRequestTo) {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User createdUser = userMapper.toUser(userRequestTo);
        createdUser.setCreatedAt(created);
        createdUser = userRepository.save(createdUser);
        return userMapper.toUserResponseTo(createdUser);
    }

    public UserResponseTo updateUser(Long id, UserRequestTo userRequestTo) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + id + " не найден"));

        existingUser.setName(userRequestTo.getName());
        existingUser.setEmail(userRequestTo.getEmail());
        existingUser.setAge(userRequestTo.getAge());

        userRepository.save(existingUser);
        return userMapper.toUserResponseTo(existingUser);
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        userRepository.delete(user
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + id + " не найден")));
    }

}
