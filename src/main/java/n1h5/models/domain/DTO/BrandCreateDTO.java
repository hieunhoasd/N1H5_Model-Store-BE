package n1h5.models.domain.DTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
public class BrandCreateDTO {
    private String brandName;
    private String country;
    private Integer foundedYear;
    private String description;
    
    // Thuộc tính này hứng file ảnh từ Client gửi lên
    private MultipartFile logoFile; 
}
