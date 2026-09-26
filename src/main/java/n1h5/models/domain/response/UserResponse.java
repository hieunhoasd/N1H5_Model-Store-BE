package n1h5.models.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import n1h5.models.domain.auth.UserStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Integer userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String avatarUrl; // Bổ sung trường này
    private UserStatus status;
}
