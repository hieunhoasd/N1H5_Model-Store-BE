package n1h5.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import n1h5.models.domain.auth.Users;

public interface  UsersRepository extends JpaRepository<Users,Integer>{
    
}
