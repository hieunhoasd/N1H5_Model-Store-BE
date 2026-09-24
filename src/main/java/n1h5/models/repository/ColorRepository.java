package n1h5.models.repository;

import n1h5.models.domain.catalog.Color;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ColorRepository extends JpaRepository<Color, Long> {
}