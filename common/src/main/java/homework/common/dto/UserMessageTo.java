package homework.common.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMessageTo {
    private String operation;
    @Email(message = "Некорректный email")
    private String email;
}
