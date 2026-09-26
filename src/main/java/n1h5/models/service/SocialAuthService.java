package n1h5.models.service;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import n1h5.models.domain.DTO.SocialUserInfo;

import java.util.Map;

@Service
public class SocialAuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    public SocialUserInfo verifyToken(String provider, String token) {
        if ("GOOGLE".equalsIgnoreCase(provider)) {
            return verifyGoogleToken(token);
        } else if ("FACEBOOK".equalsIgnoreCase(provider)) {
            return verifyFacebookToken(token);
        }
        throw new IllegalArgumentException("Nhà cung cấp không được hỗ trợ: " + provider);
    }

    private SocialUserInfo verifyGoogleToken(String idToken) {
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || response.containsKey("error_description")) {
            throw new IllegalArgumentException("Google ID Token không hợp lệ!");
        }

        String email = (String) response.get("email");
        String sub = (String) response.get("sub"); // Provider User ID
        String name = (String) response.getOrDefault("name", "");
        String picture = (String) response.getOrDefault("picture", "");

        String[] nameParts = name.split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        return SocialUserInfo.builder()
                .providerUserId(sub)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .avatarUrl(picture)
                .build();
    }

    private SocialUserInfo verifyFacebookToken(String accessToken) {
        String url = "https://graph.facebook.com/me?fields=id,first_name,last_name,email,picture.type(large)&access_token=" + accessToken;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || response.containsKey("error")) {
            throw new IllegalArgumentException("Facebook Access Token không hợp lệ!");
        }

        String id = (String) response.get("id");
        String email = (String) response.get("email");
        String firstName = (String) response.getOrDefault("first_name", "");
        String lastName = (String) response.getOrDefault("last_name", "");

        String avatarUrl = "";
        if (response.get("picture") instanceof Map pictureObj) {
            Map data = (Map) pictureObj.get("data");
            if (data != null) avatarUrl = (String) data.getOrDefault("url", "");
        }

        return SocialUserInfo.builder()
                .providerUserId(id)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .avatarUrl(avatarUrl)
                .build();
    }
    
}