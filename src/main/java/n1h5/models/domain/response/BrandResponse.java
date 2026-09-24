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
public class BrandResponse {

    private Long brandId;
    private String brandName;
    private String country;
    private Integer foundedYear;
    private String logos;
    private String description;
}