package study.stepup.productaccountingservice.repositories;

import org.springframework.data.repository.CrudRepository;
import study.stepup.productaccountingservice.entities.*;

import java.util.List;
import java.util.Optional;

public interface RefProductRegisterTypeRepository extends CrudRepository<RefProductRegisterType, Long>
{
    List<RefProductRegisterType> findAllByRefProductClass_ValueAndRefAccountType_Value(String productClassValue, String accountClassValue);

    Optional<RefProductRegisterType> findByValue (String value);

}
