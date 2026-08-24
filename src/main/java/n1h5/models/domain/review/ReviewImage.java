package n1h5.models.domain.review;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review_images", schema = "review")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}