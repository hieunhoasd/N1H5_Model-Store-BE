package n1h5.models.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import n1h5.models.domain.auth.UserStatus;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.request.UserUpdateRequest;
import n1h5.models.domain.response.UserResponse;
import n1h5.models.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /api/v1/users?page=0&size=10
    @GetMapping("/")
    public ResponseEntity<PageResponse> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(this.userService.getAllUsers(pageable));
    }

    // GET /api/v1/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    // PUT /api/v1/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable("id") Integer id,
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(this.userService.updateUser(id, request));
    }

    // POST /api/v1/users/{id}/avatar
    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> uploadAvatar(
            @PathVariable("id") Integer id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(this.userService.uploadAvatar(id, file));
    }

    // PATCH /api/v1/users/{id}/status?status=ACTIVE
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponse> changeStatus(
            @PathVariable("id") Integer id,
            @RequestParam("status") UserStatus status) {
        return ResponseEntity.ok(this.userService.changeUserStatus(id, status));
    }
}