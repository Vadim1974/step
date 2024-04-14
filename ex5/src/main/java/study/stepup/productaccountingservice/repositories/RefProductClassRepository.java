package study.stepup.productaccountingservice.repositories;

import org.springframework.data.repository.CrudRepository;
import study.stepup.productaccountingservice.entities.RefProductClass;

import java.util.Optional;

public interface RefProductClassRepository extends CrudRepository<RefProductClass, Long> {
    Optional<RefProductClass> findByValue(String value);
}
