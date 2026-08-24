package n1h5.models.domain.catalog;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "car_series", schema = "catalog")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CarSeries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "series_id")
    private Long seriesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name = "series_name", nullable = false)
    private String seriesName;

    private String description;

    @OneToMany(mappedBy = "series")
    private List<CarGeneration> generations;
}
