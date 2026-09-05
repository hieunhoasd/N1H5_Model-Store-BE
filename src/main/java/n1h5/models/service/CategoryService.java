package n1h5.models.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import n1h5.models.domain.catalog.Category;
import n1h5.models.domain.pagination.Meta;
import n1h5.models.domain.pagination.PageResponse;
import n1h5.models.repository.CategoryRepository;
import n1h5.models.util.Exception.BusinessException;

@Service 
public class CategoryService {
     private final CategoryRepository categoryRepository;
     public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository=categoryRepository;
     }

     public Category handleCreateCategory(Category category) {
        if (this.categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new BusinessException("category name đã tồn tại");
        }
        return this.categoryRepository.save(category);
     }

      public Category handleGetCategoryById(Long id) {
         Optional<Category> cateOptional = this.categoryRepository.findById(id);
         if (cateOptional.isEmpty()) {
            throw new BusinessException("Category không tồn tại với ID: " + id);
         }
         return cateOptional.get();
      }
      
      public PageResponse handleGetAllCategory(Pageable pageable) {
         Page<Category> pageCategory = this.categoryRepository.findAll(pageable);

         // 1. Gán đầy đủ thông số cho Meta
         Meta mt = new Meta();
         mt.setPage(pageable.getPageNumber() + 1); 
         mt.setPageSize(pageable.getPageSize());
         mt.setPages(pageCategory.getTotalPages());
         mt.setTotal(pageCategory.getTotalElements());

         // 2. Gán Meta và dữ liệu danh sách vào Response DTO
         PageResponse res = new PageResponse();
         res.setMeta(mt);
         res.setResult(pageCategory.getContent());
         return res;
      }

      public void handleDeleteCategoryById(Long id){
         this.categoryRepository.deleteById(id);
      }
      
      public Category handleUpdateCategory(Category category){
         Optional<Category> newcateOptional = this.categoryRepository.findById(category.getCategoryId());
         if(newcateOptional.isEmpty()){
            throw new BusinessException("Category không tồn tại với ID: " + category.getCategoryId());
         }
         Category existingCategory = newcateOptional.get();
         existingCategory.setCategoryName(category.getCategoryName());
         existingCategory.setDescription(category.getDescription());
         return this.categoryRepository.save(existingCategory);
      }
      
}
