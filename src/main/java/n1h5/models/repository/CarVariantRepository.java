package n1h5.models.repository;

import n1h5.models.domain.catalog.CarVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarVariantRepository extends JpaRepository<CarVariant, Long> {
    
}