package n1h5.models.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import n1h5.models.domain.auth.Role;

public interface RoleRepository extends JpaRepository<Role,Integer>{
    Optional<Role>findByName(String name);
    
}
