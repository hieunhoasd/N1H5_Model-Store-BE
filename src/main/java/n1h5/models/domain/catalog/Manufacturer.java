package n1h5.models.domain.catalog;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "manufacturers", schema = "catalog")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_id")
    private Long manufacturerId;

    @Column(name = "manufacturer_name", nullable = false)
    private String manufacturerName;

    private String country;

    private String logo;
}