package n1h5.models.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarSeriesRequest {
    private Long brandId;
    private String seriesName;
    private String description;
}