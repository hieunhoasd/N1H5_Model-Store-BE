package n1h5.models.service;

import lombok.RequiredArgsConstructor;
import n1h5.models.domain.auth.UserStatus;
import n1h5.models.domain.auth.Users;
import n1h5.models.domain.catalog.Brand;
import n1h5.models.domain.pagination.Meta;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.request.UserUpdateRequest;
import n1h5.models.domain.response.BrandResponse;
import n1h5.models.domain.response.UserResponse;
import n1h5.models.repository.UsersRepository;
import n1h5.models.util.Exception.BusinessException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;
    private final CloudinaryService cloudinaryService; 

    
    // Mapper helper
    public UserResponse mapToUserResponse(Users account) {
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
    
    // 1. Lấy danh sách phân trang (Dành cho Admin)

    public PageResponse getAllUsers(Pageable pageable) {
    Page<Users> userPage = this.usersRepository.findAll(pageable);

    Meta mt = new Meta();
    mt.setPage(pageable.getPageNumber() + 1);
    mt.setPageSize(pageable.getPageSize());
    mt.setPages(userPage.getTotalPages());
    mt.setTotal(userPage.getTotalElements());

    List<UserResponse> listResponses = userPage.getContent()
            .stream()
            .map(this::mapToUserResponse)
            .toList();

    PageResponse res = new PageResponse();
    res.setMeta(mt);
    res.setResult(listResponses);

    return res;
    }

    // 2. Lấy thông tin chi tiết 1 User
    public UserResponse getUserById(Integer id) {
        Users user = this.usersRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy người dùng với ID: " + id));
        return mapToUserResponse(user);
    }

    // 3. Cập nhật thông tin cá nhân
    public UserResponse updateUser(Integer id, UserUpdateRequest request) {
        Users user = this.usersRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy người dùng với ID: " + id));

        // Kiểm tra số điện thoại nếu bị trùng với người khác
        if (request.getPhone() != null && !request.getPhone().isBlank()
                && !request.getPhone().equals(user.getPhone())
                && this.usersRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("Số điện thoại này đã được sử dụng bởi tài khoản khác!");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        return mapToUserResponse(this.usersRepository.save(user));
    }

    // 4. Upload / Cập nhật Avatar
    public UserResponse uploadAvatar(Integer id, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("File hình ảnh không được để trống!");
        }

        Users user = this.usersRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy người dùng với ID: " + id));

        // Upload lên Cloudinary vào thư mục "avatars"
        String avatarUrl = this.cloudinaryService.uploadImage(file, "avatars");
        user.setAvatarUrl(avatarUrl);

        return mapToUserResponse(this.usersRepository.save(user));
    }

    // 5. Khóa / Kích hoạt tài khoản
    
    public UserResponse changeUserStatus(Integer id, UserStatus status) {
        Users user = this.usersRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy người dùng với ID: " + id));

        user.setStatus(status);
        return mapToUserResponse(this.usersRepository.save(user));
    }

}