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
public class ManufacturerResponse {
    private Long manufacturerId;
    private String manufacturerName;
    private String country;
    private String logoUrl;
}
