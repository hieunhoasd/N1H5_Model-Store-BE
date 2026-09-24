package n1h5.models.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManufacturerRequest {
    private String manufacturerName;
    private String country;
    private String logoUrl;
}