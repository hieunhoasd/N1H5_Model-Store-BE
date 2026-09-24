package n1h5.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// import n1h5.models.annotation.LogActivity;
import n1h5.models.domain.catalog.CarGeneration;
import n1h5.models.domain.catalog.CarSeries;
import n1h5.models.domain.pagination.Meta;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.request.CarGenerationRequest;
import n1h5.models.domain.response.CarGenerationResponse;
import n1h5.models.repository.CarGenerationRepository;
import n1h5.models.repository.CarSeriesRepository;
import n1h5.models.util.Annotation.LogActivity;
import n1h5.models.util.Exception.BusinessException;

@Service
public class CarGenerationService {

    private final CarGenerationRepository carGenerationRepository;
    private final CarSeriesRepository carSeriesRepository;

    public CarGenerationService(CarGenerationRepository carGenerationRepository,
                                CarSeriesRepository carSeriesRepository) {
        this.carGenerationRepository = carGenerationRepository;
        this.carSeriesRepository = carSeriesRepository;
    }

    private CarGenerationResponse mapToResponse(CarGeneration entity) {
        return CarGenerationResponse.builder()
                .generationId(entity.getGenerationId())
                .seriesId(entity.getSeries() != null ? entity.getSeries().getSeriesId() : null)
                .generationName(entity.getGenerationName())
                .startYear(entity.getStartYear())
                .endYear(entity.getEndYear())
                .build();
    }

    @LogActivity(action = "CREATE", entityName = "CarGeneration")
    public CarGenerationResponse create(CarGenerationRequest request) {
        CarSeries series = null;
        if (request.getSeriesId() != null) {
            series = carSeriesRepository.findById(request.getSeriesId())
                    .orElseThrow(() -> new BusinessException("CarSeries not found with id: " + request.getSeriesId()));
        }

        CarGeneration generation = CarGeneration.builder()
                .series(series)
                .generationName(request.getGenerationName())
                .startYear(request.getStartYear())
                .endYear(request.getEndYear())
                .build();

        return mapToResponse(carGenerationRepository.save(generation));
    }

    public PageResponse getAll(Pageable pageable) {
        Page<CarGeneration> pageGenerations = carGenerationRepository.findAll(pageable);

        Meta mt = new Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageGenerations.getTotalPages());
        mt.setTotal(pageGenerations.getTotalElements());

        List<CarGenerationResponse> listResponses = pageGenerations.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        PageResponse res = new PageResponse();
        res.setMeta(mt);
        res.setResult(listResponses);
        return res;
    }

    public CarGenerationResponse getById(Long id) {
        CarGeneration generation = carGenerationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("CarGeneration not found with id: " + id));
        return mapToResponse(generation);
    }

    @LogActivity(action = "UPDATE", entityName = "CarGeneration")
    public CarGenerationResponse update(Long id, CarGenerationRequest request) {
        CarGeneration generation = carGenerationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("CarGeneration not found with id: " + id));

        if (request.getSeriesId() != null) {
            CarSeries series = carSeriesRepository.findById(request.getSeriesId())
                    .orElseThrow(() -> new BusinessException("CarSeries not found with id: " + request.getSeriesId()));
            generation.setSeries(series);
        }

        generation.setGenerationName(request.getGenerationName());
        generation.setStartYear(request.getStartYear());
        generation.setEndYear(request.getEndYear());

        return mapToResponse(carGenerationRepository.save(generation));
    }

    @LogActivity(action = "DELETE", entityName = "CarGeneration")
    public void delete(Long id) {
        if (!carGenerationRepository.existsById(id)) {
            throw new BusinessException("CarGeneration not found with id: " + id);
        }
        carGenerationRepository.deleteById(id);
    }
}