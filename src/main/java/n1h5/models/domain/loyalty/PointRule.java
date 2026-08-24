package n1h5.models.domain.loyalty;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "point_rules", schema = "loyalty")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PointRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long ruleId;

    private String action;

    private Integer point;

    private String status;
}