package n1h5.models.domain.pagination;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meta {
    private int page;       // Trang hiện tại
    private int pageSize;   // Số lượng item trên 1 trang
    private int pages;      // Tổng số trang
    private long total;     // Tổng số bản ghi trong DB
}