package n1h5.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import n1h5.models.domain.catalog.Category;
import n1h5.models.domain.pagination.Meta;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.domain.request.CategoryRequest;
import n1h5.models.domain.response.CategoryResponse;
import n1h5.models.repository.CategoryRepository;
import n1h5.models.util.Annotation.LogActivity;
import n1h5.models.util.Exception.BusinessException;

@Service 
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private CategoryResponse mapToResponse(Category entity) {
        return CategoryResponse.builder()
                .categoryId(entity.getCategoryId())
                .categoryName(entity.getCategoryName())
                .description(entity.getDescription())
                .build();
    }

    @LogActivity(action = "CREATE", entityName = "Category")
    public CategoryResponse handleCreateCategory(CategoryRequest request) {
        if (this.categoryRepository.existsByCategoryName(request.getCategoryName())) {
            throw new BusinessException("Category name đã tồn tại");
        }

        Category category = Category.builder()
                .categoryName(request.getCategoryName())
                .description(request.getDescription())
                .build();

        Category savedCategory = this.categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    public CategoryResponse handleGetCategoryById(Long id) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category không tồn tại với ID: " + id));
        return mapToResponse(category);
    }

    public PageResponse handleGetAllCategory(Pageable pageable) {
        Page<Category> pageCategory = this.categoryRepository.findAll(pageable);

        Meta mt = new Meta();
        mt.setPage(pageable.getPageNumber() + 1); 
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageCategory.getTotalPages());
        mt.setTotal(pageCategory.getTotalElements());

        List<CategoryResponse> listResponses = pageCategory.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        PageResponse res = new PageResponse();
        res.setMeta(mt);
        res.setResult(listResponses);
        return res;
    }

    @LogActivity(action = "DELETE", entityName = "Category")
    public void handleDeleteCategoryById(Long id) {
        if (!this.categoryRepository.existsById(id)) {
            throw new BusinessException("Category không tồn tại với ID: " + id);
        }
        this.categoryRepository.deleteById(id);
    }

    @LogActivity(action = "UPDATE", entityName = "Category")
    public CategoryResponse handleUpdateCategory(Long id, CategoryRequest request) {
        Category existingCategory = this.categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category không tồn tại với ID: " + id));

        existingCategory.setCategoryName(request.getCategoryName());
        existingCategory.setDescription(request.getDescription());

        Category updatedCategory = this.categoryRepository.save(existingCategory);
        return mapToResponse(updatedCategory);
    }
}