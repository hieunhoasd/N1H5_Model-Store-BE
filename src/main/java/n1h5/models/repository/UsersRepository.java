package n1h5.models.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import n1h5.models.domain.auth.Users;

public interface  UsersRepository extends JpaRepository<Users,Integer>{
    Optional<Users> findByEmail(String email);
}
