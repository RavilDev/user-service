package homework.serviceuser.controller;

import homework.serviceuser.dto.request.UserRequestTo;
import homework.serviceuser.dto.response.UserResponseTo;
import homework.serviceuser.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    public static final String URL = "http://localhost:8080/api/users/%s";
    private final UserService userService;

    @Tag(name = "get", description = "GET-методы User API")
    @ApiResponse(responseCode = "200", description = "Пользователи получены")
    @Operation(summary = "Получить пользователей",
            description = "В ответе возвращается List объектов UserResponseTo с полями id, name, email, age, createdAt")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserResponseTo>>> getAllUsers() {
        List<UserResponseTo> users = userService.getAllUsers();
        List<EntityModel<UserResponseTo>> userModels = users.stream()
                .map(this::saturateModel)
                .toList();
        CollectionModel<EntityModel<UserResponseTo>> resource = CollectionModel.of(userModels);
        return ResponseEntity
                .ok(resource);
    }


    @Tag(name = "get", description = "GET-методы User API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь получен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @Operation(summary = "Получить пользователя",
            description = "В ответе возвращается объект UserResponseTo с полями id, name, email, age, createdAt")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseTo>> getUserById(
            @Parameter(description = "ID пользователя, которого нужно получить", required = true)
            @PathVariable @Positive(message = "ID должен быть положительным числом")
            Long id
    ) {
        UserResponseTo userById = userService.getUserById(id);
        EntityModel<UserResponseTo> resource = saturateModel(userById);
        return ResponseEntity
                .ok(resource);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные пользователя")
    })
    @Operation(summary = "Создать нового пользователя",
            description = "В ответе возвращается объект UserResponseTo с полями id, name, email, age, createdAt")
    @PostMapping
    public ResponseEntity<EntityModel<UserResponseTo>> saveUser(
            @RequestBody @Valid @NotNull UserRequestTo userRequestTo
    ) {
        UserResponseTo userResponseTo = userService.addUser(userRequestTo);
        EntityModel<UserResponseTo> resource = saturateModel(userResponseTo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resource);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь обновлен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @Operation(summary = "Обновить данные о пользователе",
            description = "В ответе возвращается объект UserResponseTo с полями id, name, email, age, createdAt")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseTo>> updateUser(
            @Parameter(description = "ID пользователя, которого нужно обновить", required = true)
            @PathVariable @Positive(message = "ID должен быть положительным числом")
            Long id,
            @Valid @NotNull @RequestBody UserRequestTo user
    ) {
        UserResponseTo userResponseTo = userService.updateUser(id, user);
        EntityModel<UserResponseTo> resource = saturateModel(userResponseTo);
        return ResponseEntity
                .ok(resource);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @Operation(summary = "Удалить пользователя",
            description = "В ответе возвращается пустое тело запроса")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя, которого нужно удалить", required = true)
            @PathVariable @Positive(message = "ID должен быть положительным числом")
            Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    private EntityModel<UserResponseTo> saturateModel(UserResponseTo userResponseTo) {
        EntityModel<UserResponseTo> resource = EntityModel.of(userResponseTo);
        resource.add(Link.of(URL.formatted(userResponseTo.getId())));

        Link updateLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class).updateUser(userResponseTo.getId(), null))
                .withRel("update");
        resource.add(updateLink);

        Link deleteLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deleteUser(userResponseTo.getId()))
                .withRel("delete");
        resource.add(deleteLink);

        Link allLink = WebMvcLinkBuilder.
                linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers())
                .withRel("get all");
        resource.add(allLink);

        return resource;
    }
}
