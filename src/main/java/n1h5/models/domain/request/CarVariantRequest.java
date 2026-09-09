package n1h5.models.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarVariantRequest {
    private Long generationId;
    private String variantName;
    private String engine;
    private Integer horsepower;
}