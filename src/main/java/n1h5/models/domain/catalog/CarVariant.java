package n1h5.models.domain.catalog;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "car_variants", schema = "catalog")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CarVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_id")
    private Long variantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generation_id")
    private CarGeneration generation;

    @Column(name = "variant_name", nullable = false)
    private String variantName;

    private String engine;

    private Integer horsepower;
}