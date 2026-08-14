package n1h5.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import n1h5.models.domain.auth.Users;
public interface  AuthRepository extends JpaRepository<Users, Integer>{
    
}
