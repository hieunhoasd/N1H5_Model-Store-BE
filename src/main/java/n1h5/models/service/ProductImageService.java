package n1h5.models.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import n1h5.models.domain.catalog.Product;
import n1h5.models.domain.catalog.ProductImage;
import n1h5.models.domain.request.ProductImageUpdateRequest;
import n1h5.models.domain.request.ProductImageUploadRequest;
import n1h5.models.domain.response.ProductImageResponse;
import n1h5.models.repository.ProductImageRepository;
import n1h5.models.repository.ProductRepository;
import n1h5.models.util.Annotation.LogActivity;
import n1h5.models.util.Exception.BusinessException;

@Service
public class ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    public ProductImageService(ProductImageRepository productImageRepository,
                               ProductRepository productRepository,
                               CloudinaryService cloudinaryService) {
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
        this.cloudinaryService = cloudinaryService;
    }

    private ProductImageResponse mapToResponse(ProductImage entity) {
        return ProductImageResponse.builder()
                .imageId(entity.getImageId())
                .productId(entity.getProduct() != null ? entity.getProduct().getProductId() : null)
                .imageUrl(entity.getImageUrl())
                .altText(entity.getAltText())
                .displayOrder(entity.getDisplayOrder())
                .isMain(entity.getIsMain())
                .build();
    }

    @Transactional
    @LogActivity(action = "CREATE", entityName = "ProductImage")
    public ProductImageResponse handleUploadProductImage(ProductImageUploadRequest request) {
        // 1. Kiểm tra sản phẩm có tồn tại không
        Product product = this.productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException("Không tìm thấy Sản phẩm với ID: " + request.getProductId()));

        if (request.getFile() == null || request.getFile().isEmpty()) {
            throw new BusinessException("File hình ảnh không được để trống");
        }

        // 2. Upload file lên Cloudinary thông qua CloudinaryService
        String imageUrl = this.cloudinaryService.uploadImage(request.getFile(), "products");

        boolean setMain = Boolean.TRUE.equals(request.getIsMain());

        // 3. Nếu đánh dấu là ảnh chính, reset các ảnh khác của sản phẩm về false
        if (setMain) {
            this.productImageRepository.resetMainImageByProductId(request.getProductId());
        }

        // 4. Tạo entity và lưu vào DB
        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl(imageUrl)
                .altText(request.getAltText())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .isMain(setMain)
                .build();

        return mapToResponse(this.productImageRepository.save(image));
    }

    public ProductImageResponse handleGetImageById(Long id) {
        ProductImage image = this.productImageRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy Hình ảnh với ID: " + id));
        return mapToResponse(image);
    }

    public List<ProductImageResponse> handleGetImagesByProductId(Long productId) {
        if (!this.productRepository.existsById(productId)) {
            throw new BusinessException("Không tìm thấy Sản phẩm với ID: " + productId);
        }

        return this.productImageRepository.findByProductProductIdOrderByDisplayOrderAsc(productId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    @LogActivity(action = "UPDATE", entityName = "ProductImage")
    public ProductImageResponse handleUpdateImageInfo(Long id, ProductImageUpdateRequest request) {
        ProductImage existingImage = this.productImageRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy Hình ảnh với ID: " + id));

        boolean setMain = Boolean.TRUE.equals(request.getIsMain());

        if (setMain) {
            this.productImageRepository.resetMainImageByProductId(existingImage.getProduct().getProductId());
        }

        if (request.getAltText() != null) {
            existingImage.setAltText(request.getAltText());
        }
        if (request.getDisplayOrder() != null) {
            existingImage.setDisplayOrder(request.getDisplayOrder());
        }
        existingImage.setIsMain(setMain);

        return mapToResponse(this.productImageRepository.save(existingImage));
    }

    @Transactional
    @LogActivity(action = "DELETE", entityName = "ProductImage")
    public void handleDeleteImageById(Long id) {
        if (!this.productImageRepository.existsById(id)) {
            throw new BusinessException("Không tìm thấy Hình ảnh với ID: " + id);
        }
        this.productImageRepository.deleteById(id);
    }
}