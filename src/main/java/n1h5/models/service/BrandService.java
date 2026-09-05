package n1h5.models.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import n1h5.models.domain.DTO.BrandCreateDTO;
import n1h5.models.domain.catalog.Brand;
import n1h5.models.domain.pagination.Meta;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.repository.BrandRepository;
import n1h5.models.util.Exception.BusinessException;

@Service
public class BrandService {
    private BrandRepository brandRepository;
    private CloudinaryService cloudinaryService;
    public BrandService(BrandRepository brandRepository, CloudinaryService cloudinaryService){
        this.brandRepository=brandRepository;
        this.cloudinaryService=cloudinaryService;
    }
    public Brand handleCreateBrand(BrandCreateDTO dto) {
        Brand newBr = new Brand();
        
        newBr.setBrandName(dto.getBrandName());
        newBr.setCountry(dto.getCountry());
        newBr.setFoundedYear(dto.getFoundedYear());
        newBr.setDescription(dto.getDescription());
        
        // Xử lý file ảnh logo
        MultipartFile file = dto.getLogoFile();
        if (file != null && !file.isEmpty()) {
            // Gọi CloudinaryService đẩy ảnh 
            String realImageUrl = cloudinaryService.uploadImage(file, "brands");
            newBr.setLogos(realImageUrl);
        }
        return this.brandRepository.save(newBr);
    }

    public Brand handleGetBrandById(Long id) {
        Optional<Brand> brandOptional=this.brandRepository.findById(id);
         if (brandOptional.isEmpty()) {
            throw new BusinessException("Brand không tồn tại với ID: " + id);
         }
        return brandOptional.get();
    }
    public PageResponse handleGetAllBrand(Pageable pageable) {
         Page<Brand> pageBrand=this.brandRepository.findAll(pageable);

         Meta mt = new Meta();
         mt.setPage(pageable.getPageNumber() + 1); 
         mt.setPageSize(pageable.getPageSize());
         mt.setPages(pageBrand.getTotalPages());
         mt.setTotal(pageBrand.getTotalElements());

         PageResponse res = new PageResponse();
         res.setMeta(mt);
         res.setResult(pageBrand.getContent());

         return res;
    }

    public void handleDeleteBrandById(Long id){
        this.brandRepository.deleteById(id);
    }

    public Brand handleUpdateBrand(Long id, BrandCreateDTO dto) {
        Brand existingBrand = this.handleGetBrandById(id);
        if (existingBrand == null) {
            return null;
        }

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

        return this.brandRepository.save(existingBrand);
    }
    
}
