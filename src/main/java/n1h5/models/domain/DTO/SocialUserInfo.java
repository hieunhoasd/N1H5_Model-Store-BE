package n1h5.models.domain.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialUserInfo {
    private String providerUserId;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
}