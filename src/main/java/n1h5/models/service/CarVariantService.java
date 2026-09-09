package n1h5.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// import n1h5.models.annotation.LogActivity;
import n1h5.models.domain.catalog.CarGeneration;
import n1h5.models.domain.catalog.CarVariant;
import n1h5.models.domain.pagination.Meta;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.request.CarVariantRequest;
import n1h5.models.domain.response.CarVariantResponse;
import n1h5.models.repository.CarGenerationRepository;
import n1h5.models.repository.CarVariantRepository;
import n1h5.models.util.Exception.BusinessException;

@Service
public class CarVariantService {

    private final CarVariantRepository carVariantRepository;
    private final CarGenerationRepository carGenerationRepository;

    public CarVariantService(CarVariantRepository carVariantRepository,
                             CarGenerationRepository carGenerationRepository) {
        this.carVariantRepository = carVariantRepository;
        this.carGenerationRepository = carGenerationRepository;
    }

    private CarVariantResponse mapToResponse(CarVariant entity) {
        return CarVariantResponse.builder()
                .variantId(entity.getVariantId())
                .generationId(entity.getGeneration() != null ? entity.getGeneration().getGenerationId() : null)
                .variantName(entity.getVariantName())
                .engine(entity.getEngine())
                .horsepower(entity.getHorsepower())
                .build();
    }

    // @LogActivity(action = "CREATE", entityName = "CarVariant")
    public CarVariantResponse create(CarVariantRequest request) {
        CarGeneration generation = null;
        if (request.getGenerationId() != null) {
            generation = carGenerationRepository.findById(request.getGenerationId())
                    .orElseThrow(() -> new BusinessException("CarGeneration not found with id: " + request.getGenerationId()));
        }

        CarVariant variant = CarVariant.builder()
                .generation(generation)
                .variantName(request.getVariantName())
                .engine(request.getEngine())
                .horsepower(request.getHorsepower())
                .build();

        return mapToResponse(carVariantRepository.save(variant));
    }

    public PageResponse getAll(Pageable pageable) {
        Page<CarVariant> pageVariants = carVariantRepository.findAll(pageable);

        Meta mt = new Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageVariants.getTotalPages());
        mt.setTotal(pageVariants.getTotalElements());

        List<CarVariantResponse> listResponses = pageVariants.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        PageResponse res = new PageResponse();
        res.setMeta(mt);
        res.setResult(listResponses);
        return res;
    }

    public CarVariantResponse getById(Long id) {
        CarVariant variant = carVariantRepository.findById(id)
                .orElseThrow(() -> new BusinessException("CarVariant not found with id: " + id));
        return mapToResponse(variant);
    }

    // @LogActivity(action = "UPDATE", entityName = "CarVariant")
    public CarVariantResponse update(Long id, CarVariantRequest request) {
        CarVariant variant = carVariantRepository.findById(id)
                .orElseThrow(() -> new BusinessException("CarVariant not found with id: " + id));

        if (request.getGenerationId() != null) {
            CarGeneration generation = carGenerationRepository.findById(request.getGenerationId())
                    .orElseThrow(() -> new BusinessException("CarGeneration not found with id: " + request.getGenerationId()));
            variant.setGeneration(generation);
        }

        variant.setVariantName(request.getVariantName());
        variant.setEngine(request.getEngine());
        variant.setHorsepower(request.getHorsepower());

        return mapToResponse(carVariantRepository.save(variant));
    }

    // @LogActivity(action = "DELETE", entityName = "CarVariant")
    public void delete(Long id) {
        if (!carVariantRepository.existsById(id)) {
            throw new BusinessException("CarVariant not found with id: " + id);
        }
        carVariantRepository.deleteById(id);
    }
}