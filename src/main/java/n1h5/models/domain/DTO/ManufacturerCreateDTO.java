package n1h5.models.domain.DTO;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManufacturerCreateDTO {
    private String manufacturerName;
    private String country;
    
    // Hứng file ảnh trực tiếp từ Client gửi lên
    private MultipartFile logoFile;
}