package study.stepup.productaccountingservice.repositories;

import org.springframework.data.repository.CrudRepository;
import study.stepup.productaccountingservice.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long>
{
    Optional<Product> findByNumber(String number);
}
