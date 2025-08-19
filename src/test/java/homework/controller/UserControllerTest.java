package homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import homework.dto.request.UserRequestTo;
import homework.dto.response.UserResponseTo;
import homework.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static homework.util.DataUtil.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserService userService;

    @Test
    public void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(getUserResponseToList());

        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(COUNT))
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(NAME))
                .andExpect(jsonPath("$[0].age").value(AGE))
                .andExpect(jsonPath("$[0].email").value(EMAIL));
    }

    @Test
    public void testGetAllUsersEmpty() throws Exception {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        mvc.perform(get("/api/users"))
                .andExpect(status().isNoContent());
    }


    @Test
    public void testGetUserById() throws Exception {
        when(userService.getUserById(ID)).thenReturn(getUserResponseTo());

        mvc.perform(get("/api/users/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.age").value(AGE))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        when(userService.getUserById(ID)).thenReturn(null);

        mvc.perform(get("/api/users/{id}", ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserByNegativeId() throws Exception {
        mvc.perform(get("/api/users/{id}", -ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ID должен быть положительным числом"));
    }

    @Test
    public void testSaveUser() throws Exception {
        UserRequestTo userRequestTo = getUserRequestTo();
        UserResponseTo userResponseTo = getUserResponseTo();

        when(userService.addUser(any(UserRequestTo.class))).thenReturn(userResponseTo);

        ObjectMapper objectMapper = new ObjectMapper();
        String userRequestToJson = objectMapper.writeValueAsString(userRequestTo);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestToJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.age").value(AGE))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    public void testSaveUserNameIsEmpty() throws Exception {
        UserRequestTo userRequestTo = getUserRequestTo();
        userRequestTo.setName("");

        ObjectMapper objectMapper = new ObjectMapper();
        String userRequestToJson = objectMapper.writeValueAsString(userRequestTo);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestToJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Имя пользователя не может быть пустым"));
    }

    @Test
    public void testSaveUserEmailIsNull() throws Exception {
        UserRequestTo userRequestTo = getUserRequestTo();
        userRequestTo.setEmail(null);

        ObjectMapper objectMapper = new ObjectMapper();
        String userRequestToJson = objectMapper.writeValueAsString(userRequestTo);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestToJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email не может быть null"));
    }

    @Test
    public void testSaveUserEmailIsInvalid() throws Exception {
        UserRequestTo userRequestTo = getUserRequestTo();
        userRequestTo.setEmail("invalid");

        ObjectMapper objectMapper = new ObjectMapper();
        String userRequestToJson = objectMapper.writeValueAsString(userRequestTo);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestToJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Некорректный email"));
    }

    @Test
    public void testSaveUserAgeIsNull() throws Exception {
        UserRequestTo userRequestTo = getUserRequestTo();
        userRequestTo.setAge(null);

        ObjectMapper objectMapper = new ObjectMapper();
        String userRequestToJson = objectMapper.writeValueAsString(userRequestTo);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestToJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Возраст не может быть null"));
    }

    @Test
    public void testSaveUserAgeIsOutOfRange() throws Exception {
        UserRequestTo userRequestTo = getUserRequestTo();
        userRequestTo.setAge(150);

        ObjectMapper objectMapper = new ObjectMapper();
        String userRequestToJson = objectMapper.writeValueAsString(userRequestTo);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestToJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Люди пока столько не живут"));
    }

    @Test
    public void testUpdateUser() throws Exception {

        UserRequestTo userRequestTo = getUserRequestTo();
        UserResponseTo userResponseTo = getUserResponseTo();

        when(userService.updateUser(any(Long.class), any(UserRequestTo.class)))
                .thenReturn(userResponseTo);

        ObjectMapper objectMapper = new ObjectMapper();
        String userRequestToJson = objectMapper.writeValueAsString(userRequestTo);

        mvc.perform(put("/api/users/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestToJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.age").value(AGE))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    public void testUpdateUserByNegativeId() throws Exception {
        UserRequestTo userRequestTo = getUserRequestTo();

        ObjectMapper objectMapper = new ObjectMapper();
        String userRequestToJson = objectMapper.writeValueAsString(userRequestTo);

        mvc.perform(put("/api/users/{id}", -ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestToJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ID должен быть положительным числом"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        mvc.perform(delete("/api/users/{id}", ID))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(ID);
    }

    @Test
    public void testDeleteUserByNegativeId() throws Exception {
        mvc.perform(delete("/api/users/{id}", -ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ID должен быть положительным числом"));
    }
}