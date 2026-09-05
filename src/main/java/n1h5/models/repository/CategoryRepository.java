package n1h5.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import n1h5.models.domain.catalog.Category;


public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByCategoryName(String categoryName);
}
