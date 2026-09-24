package n1h5.models.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageRequest {

    @NotNull(message = "Product ID không được để trống")
    private Long productId;

    @NotBlank(message = "Đường dẫn ảnh không được để trống")
    private String imageUrl;

    private String altText;

    private Integer displayOrder;

    private Boolean isMain;
}