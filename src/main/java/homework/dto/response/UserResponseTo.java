package homework.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class UserResponseTo {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private Timestamp createdAt;
}
