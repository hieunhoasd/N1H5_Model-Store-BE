package n1h5.models.repository;

import n1h5.models.domain.auth.UserSocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSocialAccountRepository extends JpaRepository<UserSocialAccount, Integer> {
    Optional<UserSocialAccount> findByProviderNameAndProviderUserId(String providerName, String providerUserId);
}