package n1h5.models.domain.catalog;


import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "colors", schema = "catalog")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "color_id")
    private Long colorId;

    @Column(name = "color_name", nullable = false)
    private String colorName;

    private String hex;
}