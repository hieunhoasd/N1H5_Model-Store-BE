package n1h5.models.domain.catalog;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "categories", schema = "catalog")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    private String description;
}