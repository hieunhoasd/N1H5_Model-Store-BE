package n1h5.models.domain.response;

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
public class ProductImageResponse {

    private Long imageId;
    private Long productId;
    private String imageUrl;
    private String altText;
    private Integer displayOrder;
    private Boolean isMain;
}