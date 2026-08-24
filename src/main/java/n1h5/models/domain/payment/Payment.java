package n1h5.models.domain.payment;

import jakarta.persistence.*;
import lombok.*;
import n1h5.models.domain.sales.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments", schema = "payment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "method_id")
    private PaymentMethod paymentMethod;

    @Column(name = "transaction_id")
    private String transactionId;

    private BigDecimal amount;

    private String status;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;
}