package n1h5.models.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import n1h5.models.domain.auth.Users;
import n1h5.models.domain.request.LoginRequest;
import n1h5.models.domain.request.RefreshTokenRequest;
import n1h5.models.domain.request.RegisterRequest;
import n1h5.models.domain.request.SocialLoginRequest;
import n1h5.models.domain.response.LoginResponse;
import n1h5.models.domain.response.UserResponse;
import n1h5.models.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerAccount(@Valid @RequestBody RegisterRequest registerRequest) {
        UserResponse userResponse = this.authService.registerAccount(registerRequest);
        return ResponseEntity.ok(userResponse);
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginAccount(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(this.authService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(this.authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        this.authService.logoutAccount(request);
        return ResponseEntity.ok("Đăng xuất thành công");
    }

    @PostMapping("/social-login")
    public ResponseEntity<LoginResponse> socialLogin(@Valid @RequestBody SocialLoginRequest request) {
        return ResponseEntity.ok(authService.processSocialLogin(request));
    }

}
