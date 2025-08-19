package homework.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestTo {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String name;

    @NotNull(message = "Email не может быть null")
    @Email(message = "Некорректный email")
    private String email;

    @NotNull(message = "Возраст не может быть null")
    @Min(value = 1, message = "Возраст должен быть положительным числом")
    @Max(value = 130, message = "Люди пока столько не живут")
    private Integer age;
}
