package study.stepup.productaccountingservice.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.stepup.productaccountingservice.entities.*;
import study.stepup.productaccountingservice.models.ProductAccountingRequest;
import study.stepup.productaccountingservice.models.ProductAccountingResponse;
import study.stepup.productaccountingservice.repositories.*;
import study.stepup.productaccountingservice.exceptions.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProductAccountingService {

    private ProductRegisterRepository productRegisterRepository;
    private ProductRepository productRepository;
    private RefProductRegisterTypeRepository refProductRegisterTypeRepository;
    private AccountService accountService;

    public ProductAccountingResponse create(ProductAccountingRequest request)
    {
        Product product = getTppProduct(request.getInstanceId());

        //Шаг 1
        checkTppProductRegisterDoubles(request, product.getId());

        //Шаг 3
        RefProductRegisterType refProductRegisterType = getTppRefProductRegisterType(request);

        //Шаг 4
        Account account = accountService.getAccount(request.getBranchCode(),
                request.getCurrencyCode(), request.getMdmCode(), request.getPriorityCode(), request.getRegistryTypeCode());

        //Шаг 5
        return saveTppProductRegister(request, product, refProductRegisterType, account);
    }

    ProductAccountingResponse saveTppProductRegister(ProductAccountingRequest request, Product product,
                                                     RefProductRegisterType refProductRegisterType, Account account)
    {
        String stateOpen = "OPEN";
        ProductRegister productRegister = new ProductRegister(
                null,
                product.getId(),
                refProductRegisterType,
                account.getId(),
                request.getCurrencyCode(),
                stateOpen,
                account.getAccount_number());
        ProductRegister rez = productRegisterRepository.save(productRegister);
        return new ProductAccountingResponse(rez.getId().toString());
    }

    RefProductRegisterType getTppRefProductRegisterType (ProductAccountingRequest request)
    {
        Optional<RefProductRegisterType> optionalTppRefProductRegisterType = refProductRegisterTypeRepository.findByValue(request.getRegistryTypeCode());
        if (optionalTppRefProductRegisterType.isEmpty()) {
            throw new NotFoundException(String.format("Код Продукта %s не найден в каталоге продуктов %s для данного типа",
                    request.getRegistryTypeCode(), RefProductRegisterType.class)
            );
        }
        return optionalTppRefProductRegisterType.get();
    }


    void checkTppProductRegisterDoubles(ProductAccountingRequest request, Long productId)
    {
        List<ProductRegister> productRegisterList = productRegisterRepository.findAllByProductId(productId);

        if (productRegisterList != null && !productRegisterList.isEmpty() && !request.getRegistryTypeCode().isBlank())
        {
            for (ProductRegister productRegister : productRegisterList)
            {
                if (productRegister.getRefProductRegisterType().getValue().equals(request.getRegistryTypeCode()))
                {
                    throw new BadRequestException(String.format("Параметр registryTypeCode тип регистра %s уже существует для ЭП с ИД %s",
                            request.getRegistryTypeCode(), productId)
                    );
                }
            }
        }
    }

    Product getTppProduct(Long instanceId)
    {
        if(instanceId == null) {
            throw new NotFoundException("Параметр instanceId отсутствует");
        }

        Optional<Product> optionalTppProduct = productRepository.findById(instanceId);
        if (optionalTppProduct.isEmpty())
        {
            throw new NotFoundException(String.format("Экземпляр продукта с параметром instanceId %s не найден",
                    instanceId)
            );
        }

        return optionalTppProduct.get();
    }
}
