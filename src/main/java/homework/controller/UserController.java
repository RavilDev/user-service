package homework.controller;

import homework.dto.request.UserRequestTo;
import homework.dto.response.UserResponseTo;
import homework.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseTo>> getAllUsers() {
        List<UserResponseTo> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity
                    .noContent()
                    .build();
        }
        return ResponseEntity
                .ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseTo> getUserById(
            @PathVariable @Positive(message = "ID должен быть положительным числом") Long id
    ) {
        UserResponseTo userById = userService.getUserById(id);
        if (userById == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity
                .ok(userById);
    }

    @PostMapping
    public ResponseEntity<UserResponseTo> saveUser(
            @RequestBody @Valid @NotNull UserRequestTo userRequestTo
    ) {
        UserResponseTo userResponseTo = userService.addUser(userRequestTo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResponseTo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseTo> updateUser(
            @PathVariable @Positive(message = "ID должен быть положительным числом") Long id,
            @Valid @NotNull @RequestBody UserRequestTo user
    ) {
        return ResponseEntity
                .ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(
            @PathVariable @Positive(message = "ID должен быть положительным числом") Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
