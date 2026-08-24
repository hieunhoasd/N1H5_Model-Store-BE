package n1h5.models.domain.shipping;

import jakarta.persistence.*;
import lombok.*;
import n1h5.models.domain.sales.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments", schema = "shipping")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long shipmentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String carrier;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "shipping_fee")
    private BigDecimal shippingFee;

    private String status;

    @Column(name = "estimated_date")
    private LocalDateTime estimatedDate;

    @Column(name = "delivered_date")
    private LocalDateTime deliveredDate;
}