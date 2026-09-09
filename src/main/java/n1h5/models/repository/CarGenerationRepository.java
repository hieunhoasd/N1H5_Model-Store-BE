package n1h5.models.repository;

import n1h5.models.domain.catalog.CarGeneration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarGenerationRepository extends JpaRepository<CarGeneration, Long> {
    
}