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
import n1h5.models.domain.auth.Role;
import n1h5.models.domain.auth.UserStatus;
import n1h5.models.domain.auth.Users;
import n1h5.models.domain.request.LoginRequest;
import n1h5.models.domain.request.RefreshTokenRequest;
import n1h5.models.domain.request.RegisterRequest;
import n1h5.models.domain.response.LoginResponse;
import n1h5.models.domain.response.UserResponse;
import n1h5.models.repository.AuthRepository;
import n1h5.models.repository.RoleRepository;
import n1h5.models.repository.UsersRepository;
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
    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEndcoder ,UsersRepository usersRepository,RoleRepository roleRepository,AuthenticationManager authenticationManager,JwtService jwtService,StringRedisTemplate redisTemplate){

        this.authRepository=authRepository;
        this.passwordEndcoder=passwordEndcoder;
        this.usersRepository=usersRepository;
        this.roleRepository=roleRepository;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
        this.redisTemplate=redisTemplate;
    }
    public Users registerAccount(RegisterRequest registerRequest){
            Users newAccount = new Users();
            newAccount.setEmail(registerRequest.getEmail());
            newAccount.setFirstName(registerRequest.getFirstName());
            newAccount.setLastName(registerRequest.getLastName());
            newAccount.setUsername(registerRequest.getUsername());
            newAccount.setPhone(registerRequest.getPhone());
            String rawPassword=registerRequest.getPassword();
            String hashPassword =passwordEndcoder.encode(rawPassword);
            newAccount.setPassword(hashPassword);
            Role defaultRole = this.roleRepository.findByRoleName("ROLE_CUSTOMER").orElseThrow(()-> new BusinessException("khong tin thay role"));
            Set<Role> roles = new HashSet<>();
            roles.add(defaultRole);
            newAccount.setRoles(roles);
            newAccount.setStatus(UserStatus.ACTIVE);
            this.usersRepository.save(newAccount);
            return newAccount;
    }

    public UserResponse userDTO(Users account) {
        UserResponse usersDTO=new UserResponse();
        usersDTO.setEmail(account.getEmail());
        usersDTO.setUsername(account.getUsername());
        usersDTO.setFirstName(account.getFirstName());
        usersDTO.setLastName(account.getLastName());
        usersDTO.setPhone(account.getPhone());
        usersDTO.setStatus(account.getStatus());
        return usersDTO;
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
}
