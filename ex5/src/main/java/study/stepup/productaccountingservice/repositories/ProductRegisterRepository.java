package study.stepup.productaccountingservice.repositories;

import org.springframework.data.repository.CrudRepository;
import study.stepup.productaccountingservice.entities.ProductRegister;

import java.util.List;

public interface ProductRegisterRepository extends CrudRepository<ProductRegister, Long>
{
    List<ProductRegister> findAllByProductId(Long productId);

    List<ProductRegister> findAll();
}
