package study.stepup.productaccountingservice.repositories;

import org.springframework.data.repository.CrudRepository;
import study.stepup.productaccountingservice.entities.AccountPool;

import java.util.List;


public interface AccountPoolRepository extends CrudRepository<AccountPool, Long>
{
    List<AccountPool> findAll();
    List<AccountPool> findByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCode (String branchCode, String currencyCode, String mdmCode, String priorityCode, String registryTypeCode);

}
