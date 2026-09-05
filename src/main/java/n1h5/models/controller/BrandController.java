package n1h5.models.controller;

import org.springframework.web.bind.annotation.RestController;

import n1h5.models.domain.DTO.BrandCreateDTO;
import n1h5.models.domain.catalog.Brand;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.service.BrandService;
import n1h5.models.util.Exception.BusinessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
@RestController
@RequestMapping("/api/v1/brand")
public class BrandController {
    private BrandService brandService;
    public BrandController(BrandService brandService){
        this.brandService=brandService;
    }
    @PostMapping(value = "/createBrand", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Brand> createBrand(@ModelAttribute BrandCreateDTO dto) {
        Brand newBr = this.brandService.handleCreateBrand(dto);
        return ResponseEntity.ok().body(newBr);
    }

    @GetMapping("/getBrand/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable ("id") Long id){
            Brand brand =this.brandService.handleGetBrandById(id);
            return ResponseEntity.ok().body(brand);
    }

    @GetMapping("/getBrand")
    public ResponseEntity<PageResponse> getAllBrand(Pageable pageable){
        PageResponse data=this.brandService.handleGetAllBrand(pageable);
        return ResponseEntity.ok().body(data);
    }

    @DeleteMapping("/deleteBrand")
    public ResponseEntity<String> deleteBrandById(@PathVariable ("id") Long id){
        this.brandService.handleDeleteBrandById(id);
        return ResponseEntity.ok().body("delete success");
    }
    
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Brand> updateBrand(
            @PathVariable("id") Long id,
            @ModelAttribute BrandCreateDTO dto) {
        Brand updatedBrand = this.brandService.handleUpdateBrand(id, dto);
        if (updatedBrand == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBrand);
    }
    
}
