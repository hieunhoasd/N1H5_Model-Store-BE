package n1h5.models.domain.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
public class ProductResponse {

    private Long productId;
    private String sku;
    private String productName;

    // Thông tin các FK (ID + Tên để hiển thị tiện lợi ở Frontend)
    private Long brandId;
    private String brandName;

    private Long categoryId;
    private String categoryName;

    private Long seriesId;
    private String seriesName;

    private Long generationId;
    private String generationName;

    private Long variantId;
    private String variantName;

    private Long manufacturerId;
    private String manufacturerName;

    private Long scaleId;
    private String scaleName;

    private Long colorId;
    private String colorName;

    private BigDecimal price;
    private Integer stock;
    private String description;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ProductImageResponse> images;
    private Set<TagResponse> tags;
}