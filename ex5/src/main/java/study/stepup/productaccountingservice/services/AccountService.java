package study.stepup.productaccountingservice.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.stepup.productaccountingservice.entities.Account;
import study.stepup.productaccountingservice.entities.AccountPool;
import study.stepup.productaccountingservice.exceptions.NotFoundException;
import study.stepup.productaccountingservice.repositories.AccountPoolRepository;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {

    private AccountPoolRepository accountPoolRepository;

    Account getAccount(String branchCode, String currencyCode, String mdmCode, String defaultPriorityCode, String registerType) {
        List<AccountPool> accountPoolList = accountPoolRepository.findByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCode(
                branchCode, currencyCode, mdmCode, defaultPriorityCode.isEmpty() ? "00" : defaultPriorityCode, registerType);
        if (accountPoolList.isEmpty() || accountPoolList.get(0).getAccountList() == null || accountPoolList.get(0).getAccountList().isEmpty()) {
            throw new NotFoundException(
                    String.format("Счет не найден по комбинации значений: branchCode:%s isoCurrencyCode:%s MdmCode:%s priorityCode:%s registerType:%s",
                            branchCode, currencyCode, mdmCode, defaultPriorityCode, registerType)
            );
        }
        return accountPoolList.get(0).getAccountList().get(0);
    }
}
