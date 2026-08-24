package n1h5.models.domain.response;
import lombok.Getter;
import lombok.Setter;
import n1h5.models.domain.auth.UserStatus;
@Getter
@Setter
public class UserResponse {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private UserStatus status;
    
}
