package n1h5.models.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import n1h5.models.domain.DTO.BrandCreateDTO;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.response.BrandResponse;
import n1h5.models.service.BrandService;

@RestController
@RequestMapping("/api/v1/brand")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping(value = "/createBrand", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BrandResponse> createBrand(@ModelAttribute BrandCreateDTO dto) {
        BrandResponse newBr = this.brandService.handleCreateBrand(dto);
        return ResponseEntity.ok().body(newBr);
    }
    
    @GetMapping("/getBrand/{id}")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable("id") Long id) {
        BrandResponse brand = this.brandService.handleGetBrandById(id);
        return ResponseEntity.ok().body(brand);
    }

    @GetMapping("/getBrand")
    public ResponseEntity<PageResponse> getAllBrand(Pageable pageable) {
        PageResponse data = this.brandService.handleGetAllBrand(pageable);
        return ResponseEntity.ok().body(data);
    }

    @DeleteMapping("/deleteBrand/{id}")
    public ResponseEntity<String> deleteBrandById(@PathVariable("id") Long id) {
        this.brandService.handleDeleteBrandById(id);
        return ResponseEntity.ok().body("delete success");
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BrandResponse> updateBrand(
            @PathVariable("id") Long id,
            @ModelAttribute BrandCreateDTO dto) {
        BrandResponse updatedBrand = this.brandService.handleUpdateBrand(id, dto);
        return ResponseEntity.ok(updatedBrand);
    }
}