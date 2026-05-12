package homework.serviceuser.service;

import homework.common.dto.UserMessageTo;
import homework.serviceuser.dto.request.UserRequestTo;
import homework.serviceuser.dto.response.UserResponseTo;
import homework.serviceuser.entity.User;
import homework.serviceuser.exception.UserNotFoundException;
import homework.serviceuser.mapper.UserMapper;
import homework.serviceuser.outbox.UserEventOutboxWriter;
import homework.serviceuser.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserEventOutboxWriter userEventOutboxWriter;
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
    @Transactional
    public UserResponseTo addUser(UserRequestTo userRequestTo) {
        Timestamp created = new Timestamp(System.currentTimeMillis());
        User createdUser = userMapper.toUser(userRequestTo);
        createdUser.setCreatedAt(created);
        createdUser = userRepository.save(createdUser);
        enqueueUserEvent("CREATE", createdUser.getEmail());
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
    @Transactional
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        User userElseThrow = user.orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));

        enqueueUserEvent("DELETE", userElseThrow.getEmail());
        userRepository.delete(userElseThrow);
    }

    private void enqueueUserEvent(String operation, String email) {
        UserMessageTo message = UserMessageTo.builder()
                .operation(operation)
                .email(email)
                .build();
        userEventOutboxWriter.enqueue(message);
    }

}
