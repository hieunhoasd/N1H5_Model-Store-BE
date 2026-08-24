package n1h5.models.domain.payment;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment_methods", schema = "payment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "method_id")
    private Long methodId;

    @Column(name = "method_name", nullable = false)
    private String methodName;

    @OneToMany(mappedBy = "paymentMethod")
    private List<Payment> payments = new ArrayList<>();
}