package n1h5.models.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import n1h5.models.domain.request.ProductImageUpdateRequest;
import n1h5.models.domain.request.ProductImageUploadRequest;
import n1h5.models.domain.response.ProductImageResponse;
import n1h5.models.service.ProductImageService;

@RestController
@RequestMapping("/api/v1/product-images")
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductImageResponse> uploadImage(@Valid @ModelAttribute ProductImageUploadRequest request) {
        ProductImageResponse response = this.productImageService.handleUploadProductImage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImageResponse> getImageById(@PathVariable("id") Long id) {
        ProductImageResponse response = this.productImageService.handleGetImageById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductImageResponse>> getImagesByProductId(@PathVariable("productId") Long productId) {
        List<ProductImageResponse> list = this.productImageService.handleGetImagesByProductId(productId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductImageResponse> updateImageInfo(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductImageUpdateRequest request) {
        ProductImageResponse response = this.productImageService.handleUpdateImageInfo(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImageById(@PathVariable("id") Long id) {
        this.productImageService.handleDeleteImageById(id);
        return ResponseEntity.ok("Delete success");
    }
}