package n1h5.models.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorRequest {
    private String colorName;
    private String hex;
}