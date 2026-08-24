package n1h5.models.domain.shipping;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses", schema = "shipping")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "user_id")
    private Long userId;

    private String province;

    private String district;

    private String ward;

    private String street;
}