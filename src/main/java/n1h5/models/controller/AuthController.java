package n1h5.models.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import n1h5.models.domain.auth.Users;
import n1h5.models.domain.request.LoginRequest;
import n1h5.models.domain.request.RegisterRequest;
import n1h5.models.domain.response.LoginReponse;
import n1h5.models.domain.response.UserResponse;
import n1h5.models.service.AuthService;

@RestController
@RequestMapping("v1/api")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }
    @PostMapping("/register")
    public ResponseEntity <UserResponse> registerAccount(@Valid @RequestBody RegisterRequest registerRequest ){
        Users account=this.authService.registerAccount(registerRequest);
        UserResponse userdto = this.authService.userDTO(account);
        return ResponseEntity.ok().body(userdto);
    }

   @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginAccount(@Valid @RequestBody LoginRequest request) {
        // Sửa lỗi dòng 34 bị đỏ bằng cách truyền authService.login(request) vào đây
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        
        String refreshToken = authHeader.substring(7);
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
