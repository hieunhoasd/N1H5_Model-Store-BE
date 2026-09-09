package n1h5.models.repository;

import n1h5.models.domain.catalog.CarVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarVariantRepository extends JpaRepository<CarVariant, Long> {
    
}