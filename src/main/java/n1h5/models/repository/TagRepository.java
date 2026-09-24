package n1h5.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import n1h5.models.domain.catalog.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    boolean existsByTagName(String tagName);
}