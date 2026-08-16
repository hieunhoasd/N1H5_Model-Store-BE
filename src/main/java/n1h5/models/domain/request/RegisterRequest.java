package n1h5.models.domain.request;

import java.util.HashSet;

import jakarta.validation.constraints.*;
import lombok.Setter;
import n1h5.models.domain.auth.UserStatus;
import lombok.Getter;
@Getter
@Setter

public class RegisterRequest {

    @NotBlank(message = "Username không được để trống")
    @Size(min = 4, max = 30, message = "Username phải từ 4 đến 30 ký tự")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, max = 64, message = "Mật khẩu phải từ 8 đến 64 ký tự")
    private String password;

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 50, message = "Tên không vượt quá 50 ký tự")
    private String firstName;

    @NotBlank(message = "Họ không được để trống")
    @Size(max = 50, message = "Họ không vượt quá 50 ký tự")
    private String lastName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)[3|5|7|8|9][0-9]{8}$", message = "Số điện thoại không đúng định dạng Việt Nam")
    private String phone;

}