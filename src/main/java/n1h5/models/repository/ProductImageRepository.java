package n1h5.models.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import n1h5.models.domain.catalog.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductProductIdOrderByDisplayOrderAsc(Long productId);

    @Modifying
    @Query("UPDATE ProductImage pi SET pi.isMain = false WHERE pi.product.productId = :productId")
    void resetMainImageByProductId(@Param("productId") Long productId);
    
}