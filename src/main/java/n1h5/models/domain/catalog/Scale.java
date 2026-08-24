package n1h5.models.domain.catalog;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scales", schema = "catalog")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Scale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scale_id")
    private Long scaleId;

    @Column(name = "scale_name", nullable = false)
    private String scaleName;
}