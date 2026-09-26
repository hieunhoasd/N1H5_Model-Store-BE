package n1h5.models.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginRequest {

    @NotBlank(message = "Provider không được để trống (GOOGLE hoặc FACEBOOK)")
    private String provider; // "GOOGLE" hoặc "FACEBOOK"

    @NotBlank(message = "Token không được để trống")
    private String token; // Google ID Token hoặc Facebook Access Token
}