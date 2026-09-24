package n1h5.models.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.request.CategoryRequest;
import n1h5.models.domain.response.CategoryResponse;
import n1h5.models.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = this.categoryService.handleCreateCategory(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCategory/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("id") Long id) {
        CategoryResponse category = this.categoryService.handleGetCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/getCategory")
    public ResponseEntity<PageResponse> getAllCategory(Pageable pageable) {
        PageResponse data = this.categoryService.handleGetAllCategory(pageable);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable("id") Long id) {
        this.categoryService.handleDeleteCategoryById(id);
        return ResponseEntity.ok("delete success");
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable("id") Long id,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse updatedCategory = this.categoryService.handleUpdateCategory(id, request);
        return ResponseEntity.ok(updatedCategory);
    }
}