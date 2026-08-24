package n1h5.models.domain.loyalty;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_points", schema = "loyalty")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long pointId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "current_point")
    private Integer currentPoint;

    @Column(name = "lifetime_point")
    private Integer lifetimePoint;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}