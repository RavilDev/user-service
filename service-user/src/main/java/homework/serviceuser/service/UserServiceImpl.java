package homework.serviceuser.service;

import homework.serviceuser.dto.notification.UserMessageTo;
import homework.serviceuser.dto.request.UserRequestTo;
import homework.serviceuser.dto.response.UserResponseTo;
import homework.serviceuser.entity.User;
import homework.serviceuser.exception.UserNotFoundException;
import homework.serviceuser.kafka.KafkaProducerService;
import homework.serviceuser.mapper.UserMapper;
import homework.serviceuser.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseTo> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponseTo).toList();
    }

    @Override
    public UserResponseTo getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));
        return userMapper.toUserResponseTo(user);
    }

    @Override
    public UserResponseTo addUser(UserRequestTo userRequestTo) {
        sendMessage("CREATE", userRequestTo.getEmail());

        Timestamp created = new Timestamp(System.currentTimeMillis());
        User createdUser = userMapper.toUser(userRequestTo);
        createdUser.setCreatedAt(created);
        createdUser = userRepository.save(createdUser);
        return userMapper.toUserResponseTo(createdUser);
    }

    @Override
    public UserResponseTo updateUser(Long id, UserRequestTo userRequestTo) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));

        existingUser.setName(userRequestTo.getName());
        existingUser.setEmail(userRequestTo.getEmail());
        existingUser.setAge(userRequestTo.getAge());

        userRepository.save(existingUser);
        return userMapper.toUserResponseTo(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        User userElseThrow = user.orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));

        sendMessage("DELETE", userElseThrow.getEmail());

        userRepository.delete(userElseThrow);
    }

    private void sendMessage(String operation, String email) {
        UserMessageTo message = UserMessageTo.builder()
                .operation(operation)
                .email(email)
                .build();
        kafkaProducerService.sendMessage(message);
    }

}
