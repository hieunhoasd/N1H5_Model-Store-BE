package n1h5.models.domain.loyalty;


import jakarta.persistence.*;
import lombok.*;
import n1h5.models.domain.promotion.Coupon;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_redemption", schema = "loyalty")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CouponRedemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "redemption_id")
    private Long redemptionId;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(name = "point_used")
    private Integer pointUsed;

    @CreationTimestamp
    @Column(name = "redeemed_at", updatable = false)
    private LocalDateTime redeemedAt;
}