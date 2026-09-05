package n1h5.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import n1h5.models.domain.catalog.Brand;

public interface  BrandRepository extends JpaRepository<Brand,Long>{
    
}
