package n1h5.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import n1h5.models.domain.catalog.CarSeries;

public interface  CarSeriesRepository extends JpaRepository<CarSeries,Long>{
    
}
