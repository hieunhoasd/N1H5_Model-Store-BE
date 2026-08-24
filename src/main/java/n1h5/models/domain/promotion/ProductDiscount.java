package n1h5.models.domain.promotion;

import jakarta.persistence.*;
import lombok.*;
import n1h5.models.domain.catalog.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_discounts", schema = "promotion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long discountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "discount_percent")
    private BigDecimal discountPercent;

    private String status;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;
}