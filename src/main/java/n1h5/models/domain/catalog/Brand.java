package n1h5.models.domain.catalog;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "brands", schema = "catalog")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "brand_name", nullable = false)
    private String brandName;

    private String country;

    @Column(name = "founded_year")
    private Integer foundedYear;

    private String logos;

    private String description;

    @OneToMany(mappedBy = "brand")
    private List<CarSeries> seriesList;
}