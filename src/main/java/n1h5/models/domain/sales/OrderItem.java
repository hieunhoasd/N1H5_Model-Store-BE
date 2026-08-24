package n1h5.models.domain.sales;

import jakarta.persistence.*;
import lombok.*;
import n1h5.models.domain.catalog.Product;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items", schema = "sales")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal subtotal;
}
