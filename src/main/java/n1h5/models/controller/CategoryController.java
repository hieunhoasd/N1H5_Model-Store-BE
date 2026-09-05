package n1h5.models.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import n1h5.models.domain.catalog.Category;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.service.CategoryService;


@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private CategoryService catregoryService;
    public CategoryController(CategoryService catregoryService){
        this.catregoryService=catregoryService;
    }
    
    @PostMapping("/create")
    public ResponseEntity<Category> CreateCategory (@RequestBody  Category Category ){
        return ResponseEntity.ok().body(this.catregoryService.handleCreateCategory(Category));
    }
    
    @GetMapping("/getCategory/{id}")
    public ResponseEntity<Category> GetCategoryById(@PathVariable("id") Long id) {
        Category category = this.catregoryService.handleGetCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/getCategory")
    public ResponseEntity<PageResponse> GetAllCategory(Pageable pageable) {
        PageResponse data = this.catregoryService.handleGetAllCategory(pageable);
        return ResponseEntity.ok().body(data);
    }
    
    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<String> DeleteCategoryById(@PathVariable("id") Long id) {
        this.catregoryService.handleDeleteCategoryById(id);
        return ResponseEntity.ok("delete success");
    }
    
    @PutMapping("updateCategory")
    public ResponseEntity<Category> UpdateCategory(@RequestBody Category category) {
        Category newCategory=this.catregoryService.handleUpdateCategory(category);
        return ResponseEntity.ok().body(newCategory);
    }
    
}
