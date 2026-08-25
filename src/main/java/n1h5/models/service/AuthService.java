package n1h5.models.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import n1h5.models.config.CustomUserDetails;
import n1h5.models.domain.auth.Role;
import n1h5.models.domain.auth.UserStatus;
import n1h5.models.domain.auth.Users;
import n1h5.models.domain.request.LoginRequest;
import n1h5.models.domain.request.RegisterRequest;
import n1h5.models.domain.response.LoginResponse;
import n1h5.models.domain.response.UserResponse;
import n1h5.models.repository.AuthRepository;
import n1h5.models.repository.RoleRepository;
import n1h5.models.repository.UsersRepository;
import n1h5.models.util.Exception.BusinessException;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEndcoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEndcoder
        ,UsersRepository usersRepository,RoleRepository roleRepository,JwtService jwtService
        ,AuthenticationManager authenticationManager,UserDetailsService userDetailsService){

        this.authRepository=authRepository;
        this.passwordEndcoder=passwordEndcoder;
        this.usersRepository=usersRepository;
        this.roleRepository=roleRepository;
        this.jwtService=jwtService;
        this.authenticationManager=authenticationManager;
        this.userDetailsService=userDetailsService;
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
            Role defaultRole = this.roleRepository.findByRoleName("ROLE_USER").orElseThrow(()-> new BusinessException("khong tin thay role"));
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
    
    private Map<String, Object> buildExtraClaims(Users user, CustomUserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", user.getUserId());
    claims.put("roles", userDetails.getAuthorities());
    claims.put("first_Name",user.getFirstName());
    claims.put("last_Name",user.getLastName());
    claims.put("username",user.getUsername());
    claims.put("phone",user.getPhone());
    claims.put("email",user.getEmail());
    return claims;
    }

    public LoginResponse login(LoginRequest request) {
        // Đúng Username/Password thì đi tiếp, sai sẽ tự động ném ra BadCredentialsException
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Users user = this.usersRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        Map<String, Object> extraClaims = buildExtraClaims(user, userDetails);
        String accessToken = jwtService.generateToken(extraClaims,userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    // 2. Logic Cấp lại Access Token từ Refresh Token
    public LoginResponse refreshToken(String refreshToken) {
        String userEmail = jwtService.extractUsername(refreshToken);
        
        if (userEmail != null) {
            var userDetails = userDetailsService.loadUserByUsername(userEmail);
            
            // Kiểm tra Refresh Token có hợp lệ & chưa hết hạn không
            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                Users user = usersRepository.findByEmail(userEmail).orElseThrow();
                Map<String, Object> extraClaims = buildExtraClaims(user, (CustomUserDetails) userDetails);
                String newAccessToken = jwtService.generateToken(extraClaims,userDetails);
                
                return LoginResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken) // Giữ nguyên Refresh Token cũ
                        .build();
            }
        }
        throw new RuntimeException("Refresh Token không hợp lệ hoặc đã hết hạn!");
    }

    

}
