package n1h5.models.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarVariantResponse {
    private Long variantId;
    private Long generationId;
    private String variantName;
    private String engine;
    private Integer horsepower;
}