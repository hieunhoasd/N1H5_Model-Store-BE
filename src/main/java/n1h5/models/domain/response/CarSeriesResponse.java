package n1h5.models.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor 
@NoArgsConstructor 
public class CarSeriesResponse {
    private Long seriesId;
    private Long brandId;
    private String seriesName;
    private String description;
}

