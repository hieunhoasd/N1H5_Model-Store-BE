package n1h5.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import n1h5.models.domain.DTO.ManufacturerCreateDTO;
import n1h5.models.domain.catalog.Manufacturer;
import n1h5.models.domain.pagination.Meta;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.response.ManufacturerResponse;
import n1h5.models.repository.ManufacturerRepository;
import n1h5.models.util.Annotation.LogActivity;
import n1h5.models.util.Exception.BusinessException;

@Service
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    private final CloudinaryService cloudinaryService;

    public ManufacturerService(ManufacturerRepository manufacturerRepository, CloudinaryService cloudinaryService) {
        this.manufacturerRepository = manufacturerRepository;
        this.cloudinaryService = cloudinaryService;
    }

    private ManufacturerResponse mapToResponse(Manufacturer entity) {
        return ManufacturerResponse.builder()
                .manufacturerId(entity.getManufacturerId())
                .manufacturerName(entity.getManufacturerName())
                .country(entity.getCountry())
                .logoUrl(entity.getLogo())
                .build();
    }

    @LogActivity(action = "CREATE", entityName = "Manufacturer")
    public ManufacturerResponse create(ManufacturerCreateDTO dto) {
        String logoUrl = null;
        
        // Cập nhật: Truyền thêm tham số folderName là "manufacturers"
        if (dto.getLogoFile() != null && !dto.getLogoFile().isEmpty()) {
            logoUrl = cloudinaryService.uploadImage(dto.getLogoFile(), "manufacturers");
        }

        Manufacturer manufacturer = Manufacturer.builder()
                .manufacturerName(dto.getManufacturerName())
                .country(dto.getCountry())
                .logo(logoUrl)
                .build();

        return mapToResponse(manufacturerRepository.save(manufacturer));
    }

    public PageResponse getAll(Pageable pageable) {
        Page<Manufacturer> pageManufacturers = manufacturerRepository.findAll(pageable);

        Meta mt = new Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageManufacturers.getTotalPages());
        mt.setTotal(pageManufacturers.getTotalElements());

        List<ManufacturerResponse> listResponses = pageManufacturers.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        PageResponse res = new PageResponse();
        res.setMeta(mt);
        res.setResult(listResponses);
        return res;
    }

    public ManufacturerResponse getById(Long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Manufacturer not found with id: " + id));
        return mapToResponse(manufacturer);
    }

    @LogActivity(action = "UPDATE", entityName = "Manufacturer")
    public ManufacturerResponse update(Long id, ManufacturerCreateDTO dto) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Manufacturer not found with id: " + id));

        manufacturer.setManufacturerName(dto.getManufacturerName());
        manufacturer.setCountry(dto.getCountry());

        // Cập nhật: Truyền thêm tham số folderName là "manufacturers"
        if (dto.getLogoFile() != null && !dto.getLogoFile().isEmpty()) {
            String logoUrl = cloudinaryService.uploadImage(dto.getLogoFile(), "manufacturers");
            manufacturer.setLogo(logoUrl);
        }

        return mapToResponse(manufacturerRepository.save(manufacturer));
    }

    
    @LogActivity(action = "DELETE", entityName = "Manufacturer")
    public void delete(Long id) {
        if (!manufacturerRepository.existsById(id)) {
            throw new BusinessException("Manufacturer not found with id: " + id);
        }
        manufacturerRepository.deleteById(id);
    }
}