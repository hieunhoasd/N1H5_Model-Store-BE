package n1h5.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import n1h5.models.domain.DTO.BrandCreateDTO;
import n1h5.models.domain.catalog.Brand;
import n1h5.models.domain.pagination.Meta;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.response.BrandResponse;
import n1h5.models.repository.BrandRepository;
import n1h5.models.util.Annotation.LogActivity;
import n1h5.models.util.Exception.BusinessException;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final CloudinaryService cloudinaryService;

    public BrandService(BrandRepository brandRepository, CloudinaryService cloudinaryService) {
        this.brandRepository = brandRepository;
        this.cloudinaryService = cloudinaryService;
    }

    // Mapper chuyển đổi Entity sang Response DTO
    private BrandResponse mapToResponse(Brand entity) {
        return BrandResponse.builder()
                .brandId(entity.getBrandId())
                .brandName(entity.getBrandName())
                .country(entity.getCountry())
                .foundedYear(entity.getFoundedYear())
                .logos(entity.getLogos())
                .description(entity.getDescription())
                .build();
    }

    @LogActivity(action = "CREATE", entityName = "Brand")
    public BrandResponse handleCreateBrand(BrandCreateDTO dto) {
        if (this.brandRepository.existsByBrandName(dto.getBrandName())) {
            throw new BusinessException("Brand name đã tồn tại");
        }

        Brand newBr = Brand.builder()
                .brandName(dto.getBrandName())
                .country(dto.getCountry())
                .foundedYear(dto.getFoundedYear())
                .description(dto.getDescription())
                .build();

        // Xử lý file ảnh logo
        MultipartFile file = dto.getLogoFile();
        if (file != null && !file.isEmpty()) {
            String realImageUrl = cloudinaryService.uploadImage(file, "brands");
            newBr.setLogos(realImageUrl);
        }

        Brand savedBrand = this.brandRepository.save(newBr);
        return mapToResponse(savedBrand);
    }

    public BrandResponse handleGetBrandById(Long id) {
        Brand brand = this.brandRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Brand không tồn tại với ID: " + id));
        return mapToResponse(brand);
    }

    public PageResponse handleGetAllBrand(Pageable pageable) {
        Page<Brand> pageBrand = this.brandRepository.findAll(pageable);

        Meta mt = new Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageBrand.getTotalPages());
        mt.setTotal(pageBrand.getTotalElements());

        List<BrandResponse> listResponses = pageBrand.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        PageResponse res = new PageResponse();
        res.setMeta(mt);
        res.setResult(listResponses);

        return res;
    }

    @LogActivity(action = "DELETE", entityName = "Brand")
    public void handleDeleteBrandById(Long id) {
        if (!this.brandRepository.existsById(id)) {
            throw new BusinessException("Brand không tồn tại với ID: " + id);
        }
        this.brandRepository.deleteById(id);
    }

    @LogActivity(action = "UPDATE", entityName = "Brand")
    public BrandResponse handleUpdateBrand(Long id, BrandCreateDTO dto) {
        Brand existingBrand = this.brandRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Brand không tồn tại với ID: " + id));

        existingBrand.setBrandName(dto.getBrandName());
        existingBrand.setCountry(dto.getCountry());
        existingBrand.setFoundedYear(dto.getFoundedYear());
        existingBrand.setDescription(dto.getDescription());

        // Nếu client có tải lên file logo mới thì upload lại
        MultipartFile file = dto.getLogoFile();
        if (file != null && !file.isEmpty()) {
            String realImageUrl = cloudinaryService.uploadImage(file, "brands");
            existingBrand.setLogos(realImageUrl);
        }

        Brand updatedBrand = this.brandRepository.save(existingBrand);
        return mapToResponse(updatedBrand);
    }
}