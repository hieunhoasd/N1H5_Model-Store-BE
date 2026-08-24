package n1h5.models.domain.promotion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coupons", schema = "promotion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(nullable = false, unique = true)
    private String code;

    private BigDecimal discount;

    private Integer quantity;

    private String status;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "point_cost")
    private Integer pointCost;
}