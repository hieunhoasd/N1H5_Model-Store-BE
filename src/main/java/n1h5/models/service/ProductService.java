package n1h5.models.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import n1h5.models.domain.catalog.Product;
import n1h5.models.domain.request.ProductRequest;
import n1h5.models.domain.response.ProductImageResponse;
import n1h5.models.domain.response.ProductResponse;
import n1h5.models.domain.response.TagResponse;
import n1h5.models.repository.BrandRepository;
import n1h5.models.repository.CarGenerationRepository;
import n1h5.models.repository.CarSeriesRepository;
import n1h5.models.repository.CarVariantRepository;
import n1h5.models.repository.CategoryRepository;
import n1h5.models.repository.ColorRepository;
import n1h5.models.repository.ManufacturerRepository;
import n1h5.models.repository.ProductRepository;
import n1h5.models.repository.ScaleRepository;
import n1h5.models.repository.TagRepository;
import n1h5.models.util.Annotation.LogActivity;
import n1h5.models.util.Exception.BusinessException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final CarSeriesRepository seriesRepository;
    private final CarGenerationRepository generationRepository;
    private final CarVariantRepository variantRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ScaleRepository scaleRepository;
    private final ColorRepository colorRepository;
    private final TagRepository tagRepository;

    public ProductService(ProductRepository productRepository,
                          BrandRepository brandRepository,
                          CategoryRepository categoryRepository,
                          CarSeriesRepository seriesRepository,
                          CarGenerationRepository generationRepository,
                          CarVariantRepository variantRepository,
                          ManufacturerRepository manufacturerRepository,
                          ScaleRepository scaleRepository,
                          ColorRepository colorRepository,
                          TagRepository tagRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.seriesRepository = seriesRepository;
        this.generationRepository = generationRepository;
        this.variantRepository = variantRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.scaleRepository = scaleRepository;
        this.colorRepository = colorRepository;
        this.tagRepository = tagRepository;
    }

    private ProductResponse mapToResponse(Product product) {
        List<ProductImageResponse> images = product.getImages() != null ? product.getImages().stream()
                .map(img -> ProductImageResponse.builder()
                        .imageId(img.getImageId())
                        .productId(product.getProductId())
                        .imageUrl(img.getImageUrl())
                        .altText(img.getAltText())
                        .displayOrder(img.getDisplayOrder())
                        .isMain(img.getIsMain())
                        .build())
                .toList() : List.of();

        Set<TagResponse> tags = product.getTags() != null ? product.getTags().stream()
                .map(tag -> TagResponse.builder()
                        .tagId(tag.getTagId())
                        .tagName(tag.getTagName())
                        .build())
                .collect(java.util.stream.Collectors.toSet()) : Set.of();

        return ProductResponse.builder()
                .productId(product.getProductId())
                .sku(product.getSku())
                .productName(product.getProductName())
                .brandId(product.getBrand() != null ? product.getBrand().getBrandId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getBrandName() : null)
                .categoryId(product.getCategory() != null ? product.getCategory().getCategoryId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getCategoryName() : null)
                .seriesId(product.getSeries() != null ? product.getSeries().getSeriesId() : null)
                .seriesName(product.getSeries() != null ? product.getSeries().getSeriesName() : null)
                .generationId(product.getGeneration() != null ? product.getGeneration().getGenerationId() : null)
                .generationName(product.getGeneration() != null ? product.getGeneration().getGenerationName() : null)
                .variantId(product.getVariant() != null ? product.getVariant().getVariantId() : null)
                .variantName(product.getVariant() != null ? product.getVariant().getVariantName() : null)
                .manufacturerId(product.getManufacturer() != null ? product.getManufacturer().getManufacturerId() : null)
                .manufacturerName(product.getManufacturer() != null ? product.getManufacturer().getManufacturerName() : null)
                .scaleId(product.getScale() != null ? product.getScale().getScaleId() : null)
                .scaleName(product.getScale() != null ? product.getScale().getScaleName() : null)
                .colorId(product.getColor() != null ? product.getColor().getColorId() : null)
                .colorName(product.getColor() != null ? product.getColor().getColorName() : null)
                .price(product.getPrice())
                .stock(product.getStock())
                .description(product.getDescription())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .images(images)
                .tags(tags)
                .build();
    }

    private void mapRelationships(Product product, ProductRequest request) {
        if (request.getBrandId() != null) {
            product.setBrand(this.brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new BusinessException("Không tìm thấy Brand với ID: " + request.getBrandId())));
        } else {
            product.setBrand(null);
        }

        if (request.getCategoryId() != null) {
            product.setCategory(this.categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException("Không tìm thấy Category với ID: " + request.getCategoryId())));
        } else {
            product.setCategory(null);
        }

        if (request.getSeriesId() != null) {
            product.setSeries(this.seriesRepository.findById(request.getSeriesId())
                    .orElseThrow(() -> new BusinessException("Không tìm thấy Series với ID: " + request.getSeriesId())));
        } else {
            product.setSeries(null);
        }

        if (request.getGenerationId() != null) {
            product.setGeneration(this.generationRepository.findById(request.getGenerationId())
                    .orElseThrow(() -> new BusinessException("Không tìm thấy Generation với ID: " + request.getGenerationId())));
        } else {
            product.setGeneration(null);
        }

        if (request.getVariantId() != null) {
            product.setVariant(this.variantRepository.findById(request.getVariantId())
                    .orElseThrow(() -> new BusinessException("Không tìm thấy Variant với ID: " + request.getVariantId())));
        } else {
            product.setVariant(null);
        }

        if (request.getManufacturerId() != null) {
            product.setManufacturer(this.manufacturerRepository.findById(request.getManufacturerId())
                    .orElseThrow(() -> new BusinessException("Không tìm thấy Manufacturer với ID: " + request.getManufacturerId())));
        } else {
            product.setManufacturer(null);
        }

        if (request.getScaleId() != null) {
            product.setScale(this.scaleRepository.findById(request.getScaleId())
                    .orElseThrow(() -> new BusinessException("Không tìm thấy Scale với ID: " + request.getScaleId())));
        } else {
            product.setScale(null);
        }

        if (request.getColorId() != null) {
            product.setColor(this.colorRepository.findById(request.getColorId())
                    .orElseThrow(() -> new BusinessException("Không tìm thấy Color với ID: " + request.getColorId())));
        } else {
            product.setColor(null);
        }

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            product.setTags(new HashSet<>(this.tagRepository.findAllById(request.getTagIds())));
        } else {
            product.setTags(new HashSet<>());
        }
    }

    @LogActivity(action = "CREATE", entityName = "Product")
    public ProductResponse handleCreateProduct(ProductRequest request) {
        if (this.productRepository.existsBySku(request.getSku())) {
            throw new BusinessException("Mã SKU '" + request.getSku() + "' đã tồn tại");
        }

        Product product = Product.builder()
                .sku(request.getSku())
                .productName(request.getProductName())
                .price(request.getPrice())
                .stock(request.getStock())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();

        mapRelationships(product, request);

        return mapToResponse(this.productRepository.save(product));
    }

    public ProductResponse handleGetProductById(Long id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy Sản phẩm với ID: " + id));
        return mapToResponse(product);
    }

    public Page<ProductResponse> handleGetAllProducts(Pageable pageable) {
        return this.productRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @LogActivity(action = "UPDATE", entityName = "Product")
    public ProductResponse handleUpdateProduct(Long id, ProductRequest request) {
        Product existingProduct = this.productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy Sản phẩm với ID: " + id));

        if (this.productRepository.existsBySkuAndProductIdNot(request.getSku(), id)) {
            throw new BusinessException("Mã SKU '" + request.getSku() + "' đã bị trùng với sản phẩm khác");
        }

        existingProduct.setSku(request.getSku());
        existingProduct.setProductName(request.getProductName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStock(request.getStock());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setStatus(request.getStatus());

        mapRelationships(existingProduct, request);

        return mapToResponse(this.productRepository.save(existingProduct));
    }

    @LogActivity(action = "DELETE", entityName = "Product")
    public void handleDeleteProductById(Long id) {
        if (!this.productRepository.existsById(id)) {
            throw new BusinessException("Không tìm thấy Sản phẩm với ID: " + id);
        }
        this.productRepository.deleteById(id);
    }
}