package n1h5.models.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import n1h5.models.domain.DTO.SocialUserInfo;
import n1h5.models.domain.auth.Role;
import n1h5.models.domain.auth.UserSocialAccount;
import n1h5.models.domain.auth.UserStatus;
import n1h5.models.domain.auth.Users;
import n1h5.models.domain.request.LoginRequest;
import n1h5.models.domain.request.RefreshTokenRequest;
import n1h5.models.domain.request.RegisterRequest;
import n1h5.models.domain.request.SocialLoginRequest;
import n1h5.models.domain.response.LoginResponse;
import n1h5.models.domain.response.UserResponse;
import n1h5.models.repository.AuthRepository;
import n1h5.models.repository.RoleRepository;
import n1h5.models.repository.UserSocialAccountRepository;
import n1h5.models.repository.UsersRepository;
import n1h5.models.util.Annotation.LogActivity;
import n1h5.models.util.Exception.BusinessException;
import n1h5.models.util.SecurityUtil.CustomUserDetails;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEndcoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager; // Bổ sung
    private final JwtService jwtService;
    private final StringRedisTemplate redisTemplate;
    private final SocialAuthService socialAuthService;
    private final UserSocialAccountRepository userSocialAccountRepository;

    public AuthService(AuthRepository authRepository,PasswordEncoder passwordEndcoder ,UsersRepository usersRepository,RoleRepository roleRepository,AuthenticationManager authenticationManager,JwtService jwtService,StringRedisTemplate redisTemplate,SocialAuthService socialAuthService,UserSocialAccountRepository userSocialAccountRepository){

        this.authRepository=authRepository;
        this.passwordEndcoder=passwordEndcoder;
        this.usersRepository=usersRepository;
        this.roleRepository=roleRepository;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
        this.redisTemplate=redisTemplate;
        this.socialAuthService=socialAuthService;
        this.userSocialAccountRepository=userSocialAccountRepository;
    }
       private UserResponse mapToUserResponse(Users account) {
        return UserResponse.builder()
                .userId(account.getUserId())
                .username(account.getUsername())
                .email(account.getEmail())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .phone(account.getPhone())
                .avatarUrl(account.getAvatarUrl())
                .status(account.getStatus())
                .build();
    }

    @LogActivity(action = "CREATE", entityName = "Users")
    public UserResponse registerAccount(RegisterRequest registerRequest) {
        // 1. Kiểm tra trùng lặp Email
        if (this.usersRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BusinessException("Email này đã được sử dụng. Vui lòng sử dụng email khác!");
        }

        // 2. Kiểm tra trùng lặp Username
        if (this.usersRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BusinessException("Tên đăng nhập đã tồn tại trong hệ thống!");
        }

        // 3. Kiểm tra trùng lặp Số điện thoại (nếu có nhập)
        if (registerRequest.getPhone() != null && !registerRequest.getPhone().isBlank()
                && this.usersRepository.existsByPhone(registerRequest.getPhone())) {
            throw new BusinessException("Số điện thoại này đã được đăng ký!");
        }

        // 4. Khởi tạo đối tượng User mới
        Users newAccount = new Users();
        newAccount.setEmail(registerRequest.getEmail());
        newAccount.setFirstName(registerRequest.getFirstName());
        newAccount.setLastName(registerRequest.getLastName());
        newAccount.setUsername(registerRequest.getUsername());
        newAccount.setPhone(registerRequest.getPhone());

        // 5. Mã hóa mật khẩu
        String rawPassword = registerRequest.getPassword();
        String hashPassword = this.passwordEndcoder.encode(rawPassword);
        newAccount.setPassword(hashPassword);

        // 6. Gán Role mặc định
        Role defaultRole = this.roleRepository.findByRoleName("ROLE_CUSTOMER")
                .orElseThrow(() -> new BusinessException("Không tìm thấy vai trò ROLE_CUSTOMER!"));

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        newAccount.setRoles(roles);

        // 7. Thiết lập trạng thái và Lưu dữ liệu
        newAccount.setStatus(UserStatus.ACTIVE);
        Users savedUser = this.usersRepository.save(newAccount);

        // 8. Trả về UserResponse DTO
        return mapToUserResponse(savedUser);
    }
        
    private Map<String, Object> buildExtraClaims(CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getUserId());
        extraClaims.put("firstName", user.getFirstName());
        extraClaims.put("lastName", user.getLastName());
        extraClaims.put("email", user.getEmail());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        extraClaims.put("roles", roles);

        return extraClaims;
    }

   public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> extraClaims = buildExtraClaims(userDetails);
        String accessToken = jwtService.generateToken(extraClaims, userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Lưu vào Redis (TTL 7 ngày)
        redisTemplate.opsForValue().set(
                "RT:" + userDetails.getUsername(),
                refreshToken,
                7,
                TimeUnit.DAYS
        );

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    //  2. REFRESH TOKEN: KIỂM TRA ĐỐI CHIẾU REDIS
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String userEmail = jwtService.extractUsername(refreshToken);

        // 1. Kiểm tra token có tồn tại trên Redis không
        String savedToken = redisTemplate.opsForValue().get("RT:" + userEmail);
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new BusinessException("Refresh token không hợp lệ hoặc đã bị vô hiệu hóa");
        }

        // 2. Lấy thông tin user
        Users user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException("Không tìm thấy người dùng"));

        CustomUserDetails userDetails = new CustomUserDetails(user);

        // 3. Check hạn dùng Jwt
        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new BusinessException("Refresh token đã hết hạn");
        }

        // 4. Cấp Access Token mới
        Map<String, Object> extraClaims = buildExtraClaims(userDetails);
        String newAccessToken = jwtService.generateToken(extraClaims, userDetails);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logoutAccount(HttpServletRequest request){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String redisKey = "RT:" + email;
        redisTemplate.delete(redisKey);
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            long expiration = jwtService.getExpirationTime(accessToken); // Lấy thời gian còn sống của token (tính bằng ms)
            // Lưu Access Token vào Redis với prefix "BL:" (Blacklist) và set TTL bằng thời gian còn lại
            redisTemplate.opsForValue().set("BL:" + accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
        }
    }

        public LoginResponse processSocialLogin(SocialLoginRequest request) {
        // 1. Verify token với Google/Facebook
        SocialUserInfo socialInfo = socialAuthService.verifyToken(request.getProvider(), request.getToken());

        // 2. Tra cứu trong bảng User_Social_Accounts
        String providerName = request.getProvider().toUpperCase();
        Users user = userSocialAccountRepository
                .findByProviderNameAndProviderUserId(providerName, socialInfo.getProviderUserId())
                .map(UserSocialAccount::getUser)
                .orElseGet(() -> createOrLinkUser(providerName, socialInfo));

        // 3. Tạo UserDetails & cấp JWT
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Map<String, Object> extraClaims = buildExtraClaims(userDetails);

        String accessToken = jwtService.generateToken(extraClaims, userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // 4. Lưu Refresh Token vào Redis (TTL 7 ngày)
        redisTemplate.opsForValue().set(
                "RT:" + userDetails.getUsername(),
                refreshToken,
                7,
                TimeUnit.DAYS
        );

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Users createOrLinkUser(String providerName, SocialUserInfo socialInfo) {
        // Tìm User theo Email nếu đã từng đăng ký tài khoản thường trước đó
        Users user = null;
        if (socialInfo.getEmail() != null && !socialInfo.getEmail().isBlank()) {
            user = usersRepository.findByEmail(socialInfo.getEmail()).orElse(null);
        }

        // Nếu chưa từng có tài khoản -> Tạo mới hoàn toàn trong auth.Users
        if (user == null) {
            user = Users.builder()
                    .email(socialInfo.getEmail())
                    .username(socialInfo.getEmail() != null ? socialInfo.getEmail() : providerName.toLowerCase() + "_" + socialInfo.getProviderUserId())
                    .password(null) // Đăng nhập mạng xã hội không cần password
                    .firstName(socialInfo.getFirstName())
                    .lastName(socialInfo.getLastName())
                    .avatarUrl(socialInfo.getAvatarUrl())
                    .status(UserStatus.ACTIVE)
                    .build();
            user = usersRepository.save(user);
        }

        // Liên kết tài khoản mạng xã hội vào auth.User_Social_Accounts
        UserSocialAccount socialAccount = UserSocialAccount.builder()
                .user(user)
                .providerName(providerName)
                .providerUserId(socialInfo.getProviderUserId())
                .email(socialInfo.getEmail())
                .build();

        userSocialAccountRepository.save(socialAccount);

        return user;
    }
}
