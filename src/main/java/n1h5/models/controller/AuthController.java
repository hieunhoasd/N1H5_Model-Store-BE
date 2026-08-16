package n1h5.models.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import n1h5.models.domain.auth.Users;
import n1h5.models.domain.request.RegisterRequest;
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
}
