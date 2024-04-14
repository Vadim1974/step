package study.stepup.productaccountingservice.repositories;

import org.springframework.data.repository.CrudRepository;
import study.stepup.productaccountingservice.entities.Agreement;

import java.util.Optional;

public interface AgreementRepository extends CrudRepository<Agreement, Long>
{
    Optional<Agreement> findByNumber(String number);
}
