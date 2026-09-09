package n1h5.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import n1h5.models.domain.catalog.Brand;
import n1h5.models.domain.catalog.CarSeries;
import n1h5.models.domain.pagination.Meta;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.request.CarSeriesRequest;
import n1h5.models.domain.response.CarSeriesResponse;
import n1h5.models.repository.BrandRepository;
import n1h5.models.repository.CarSeriesRepository;
import n1h5.models.util.Exception.BusinessException;

@Service
public class CarSeriesService {

    private final CarSeriesRepository carSeriesRepository;
    private final BrandRepository brandRepository;

    public CarSeriesService(CarSeriesRepository carSeriesRepository, BrandRepository brandRepository) {
        this.carSeriesRepository = carSeriesRepository;
        this.brandRepository = brandRepository;
    }

    // Helper method map dữ liệu từ Entity -> Response DTO
    private CarSeriesResponse mapToResponse(CarSeries entity) {
        return CarSeriesResponse.builder()
                .seriesId(entity.getSeriesId())
                .brandId(entity.getBrand() != null ? entity.getBrand().getBrandId() : null)
                .seriesName(entity.getSeriesName())
                .description(entity.getDescription())
                .build();
    }

    // 1. CREATE
    public CarSeriesResponse handleCreateCarSeriesService(CarSeriesRequest carRequest) {
        Brand brand = null;
        if (carRequest.getBrandId() != null) {
            brand = brandRepository.findById(carRequest.getBrandId())
                    .orElseThrow(() -> new BusinessException("Brand not found with id: " + carRequest.getBrandId()));
        }

        CarSeries carSeries = CarSeries.builder()
                .brand(brand)
                .seriesName(carRequest.getSeriesName())
                .description(carRequest.getDescription())
                .build();

        CarSeries savedCarSeries = this.carSeriesRepository.save(carSeries);
        return mapToResponse(savedCarSeries);
    }

    // 2. READ ALL (Phân trang)
    public PageResponse getAll(Pageable pageable) {
        Page<CarSeries> pageCarSeries = this.carSeriesRepository.findAll(pageable);

        Meta mt = new Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageCarSeries.getTotalPages());
        mt.setTotal(pageCarSeries.getTotalElements());

        // Chuyển đổi danh sách Entity sang danh sách DTO bằng mapToResponse
        List<CarSeriesResponse> listResponses = pageCarSeries.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        PageResponse res = new PageResponse();
        res.setMeta(mt);
        res.setResult(listResponses);
        return res;
    }

    // 3. READ DETAIL
    public CarSeriesResponse getById(Long id) {
        CarSeries carSeries = carSeriesRepository.findById(id)
                .orElseThrow(() -> new BusinessException("CarSeries not found with id: " + id));
        return mapToResponse(carSeries);
    }

    // 4. UPDATE
    public CarSeriesResponse update(Long id, CarSeriesRequest request) {
        CarSeries carSeries = carSeriesRepository.findById(id)
                .orElseThrow(() -> new BusinessException("CarSeries not found with id: " + id));

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new BusinessException("Brand not found with id: " + request.getBrandId()));
            carSeries.setBrand(brand);
        }

        carSeries.setSeriesName(request.getSeriesName());
        carSeries.setDescription(request.getDescription());

        CarSeries updatedCarSeries = carSeriesRepository.save(carSeries);
        return mapToResponse(updatedCarSeries);
    }

    // 5. DELETE
    public void delete(Long id) {
        if (!carSeriesRepository.existsById(id)) {
            throw new BusinessException("CarSeries not found with id: " + id);
        }
        carSeriesRepository.deleteById(id);
    }
    
}