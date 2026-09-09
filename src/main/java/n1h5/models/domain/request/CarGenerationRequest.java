package n1h5.models.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarGenerationRequest {
    private Long seriesId;
    private String generationName;
    private Integer startYear;
    private Integer endYear;
}