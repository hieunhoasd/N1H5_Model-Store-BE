package n1h5.models.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import n1h5.models.domain.catalog.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // Kiểm tra SKU đã tồn tại chưa (khi tạo mới)
    boolean existsBySku(String sku);

    // Kiểm tra SKU đã tồn tại chưa (khi cập nhật, trừ chính nó)
    boolean existsBySkuAndProductIdNot(String sku, Long productId);

    // Tìm sản phẩm theo SKU
    Optional<Product> findBySku(String sku);

    // Lọc sản phẩm theo Category ID (có phân trang)
    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);

    // Lọc sản phẩm theo Brand ID (có phân trang)
    Page<Product> findByBrandBrandId(Long brandId, Pageable pageable);

    // Lọc sản phẩm theo Tag ID (có phân trang)
    Page<Product> findByTagsTagId(Long tagId, Pageable pageable);

    // Lọc sản phẩm theo Scale ID (có phân trang)
    Page<Product> findByScaleScaleId(Long scaleId, Pageable pageable);
}