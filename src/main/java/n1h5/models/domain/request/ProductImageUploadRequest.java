package n1h5.models.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageUploadRequest {

    @NotNull(message = "Product ID không được để trống")
    private Long productId;

    @NotNull(message = "File hình ảnh không được để trống")
    private MultipartFile file;

    private String altText;
    private Integer displayOrder;
    private Boolean isMain;
}