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
public class CarGenerationResponse {
    private Long generationId;
    private Long seriesId;
    private String generationName;
    private Integer startYear;
    private Integer endYear;
}