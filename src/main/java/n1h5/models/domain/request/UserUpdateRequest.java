package n1h5.models.domain.request;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @Size(max = 50, message = "Tên không vượt quá 50 ký tự")
    private String firstName;

    @Size(max = 50, message = "Họ không vượt quá 50 ký tự")
    private String lastName;

    @Pattern(regexp = "^(0|\\+84)[3|5|7|8|9][0-9]{8}$", message = "Số điện thoại không đúng định dạng Việt Nam")
    private String phone;
}