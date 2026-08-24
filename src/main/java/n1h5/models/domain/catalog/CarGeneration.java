package n1h5.models.domain.catalog;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "car_generations", schema = "catalog")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CarGeneration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "generation_id")
    private Long generationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private CarSeries series;

    @Column(name = "generation_name", nullable = false)
    private String generationName;

    @Column(name = "start_year")
    private Integer startYear;

    @Column(name = "end_year")
    private Integer endYear;

    @OneToMany(mappedBy = "generation")
    private List<CarVariant> variants;
}