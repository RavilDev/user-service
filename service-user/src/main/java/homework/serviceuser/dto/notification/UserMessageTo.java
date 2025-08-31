package homework.serviceuser.dto.notification;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMessageTo {
    private String operation;
    private String email;
}
