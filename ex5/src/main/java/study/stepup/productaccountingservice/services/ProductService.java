package study.stepup.productaccountingservice.services;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.stepup.productaccountingservice.entities.*;
import study.stepup.productaccountingservice.exceptions.*;
import study.stepup.productaccountingservice.models.*;
import study.stepup.productaccountingservice.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {

    private final AccountPoolRepository accountPoolRepository;
    private final AgreementRepository agreementRepository;
    private final ProductRepository productRepository;
    private final ProductRegisterRepository productRegisterRepository;
    private final RefProductClassRepository refProductClassRepository;
    private final RefProductRegisterTypeRepository refProductRegisterTypeRepository;


    public void test() {
        log.info("AccountPool " + accountPoolRepository.count());
        for(var x:accountPoolRepository.findAll()) log.info(x.toString());
        log.info("RefProductClass " + refProductClassRepository.count());
        for(var x:refProductClassRepository.findAll()) log.info(x.toString());
        log.info("RefProductRegisterType " + refProductRegisterTypeRepository.count());
        for(var x:refProductRegisterTypeRepository.findAll()) log.info(x.toString());
        log.info("Agreement " + agreementRepository.count());
        for(var x:agreementRepository.findAll()) log.info(x.toString());
        log.info("Product " + productRepository.count());
        for(var x:productRepository.findAll()) log.info(x.toString());
        log.info("ProductRegister " + productRegisterRepository.count());
        for(var x:productRegisterRepository.findAll()) log.info(x.toString());
    }

    public ProductResponse create(ProductRequest request) {
        test();
        log.debug(request.toString());
        if (request.getInstanceId() == null) {
            return createProduct(request);
        }
        return createAgreement(request);
    }

    public ProductResponse createProduct(ProductRequest request) {
        //Шаг 1.1
        isTppProductHasNoDoubles(request);

        //Шаг 1.2
        isAgreementHasNoNumberDoubles(request);

        //Шаг 1.3
        List<RefProductRegisterType> refProductRegisterTypeList = getTppRefProductRegisterTypes(request);

        //Шаг 1.4
        Long productId = saveTppProduct(request);

        //Шаг 1.5
        List<String> productRegisterIds = saveTppProductRegister(request, productId, refProductRegisterTypeList);

        return new ProductResponse(productId.toString(), productRegisterIds, null);

    }

    void isTppProductHasNoDoubles(ProductRequest request) {
        test();
        Optional<Product> optProduct = productRepository.findByNumber(request.getContractNumber());
        if (optProduct.isPresent()) {
            throw new BadRequestException(
                    String.format("Параметр ContractNumber № договора %s уже существует для ЭП с ИД %s",
                            request.getContractNumber(), optProduct.get().getId()));
        }
        test();
    }

    void isAgreementHasNoNumberDoubles(ProductRequest request) {
        test();
        if (request.getInstanceArrangement() != null ) {
            for (var agr : request.getInstanceArrangement()) {
                Optional<Agreement> optAgr = agreementRepository.findByNumber(agr.getNumber());
//                optAgr
                if (optAgr.isPresent()) {
                    throw new BadRequestException(
                            String.format("Параметр № дополнительного согашения Number %s уже существует для ЭП с ИД %s",
                                    agr.getNumber(), optAgr.get().getId()));
                }
            }
        }
        test();
    }

    List<RefProductRegisterType> getTppRefProductRegisterTypes(ProductRequest request) {
        List<RefProductRegisterType> refProductRegisterTypeList = refProductRegisterTypeRepository.findAllByRefProductClass_ValueAndRefAccountType_Value(
                request.getProductCode(), "Клиентский");

        if (refProductRegisterTypeList == null || (long) refProductRegisterTypeList.size() < 1) {
            throw new NotFoundException(String.format("КодПродукта %s не найден в Каталоге продуктов tpp_ref_product_class",
                    request.getProductCode()));
        }

        return refProductRegisterTypeList;
    }

    Long saveTppProduct(ProductRequest request) {
        var optValue = refProductClassRepository.findByValue(request.getProductCode());
        if (optValue.isEmpty()) {
            throw new NotFoundException(String.format("КодПродукта %s не найден в Каталоге продуктов tpp_ref_product_class",
                    request.getProductCode()));
        }

        Product product = new Product(null, optValue.get().getInternalId(), 0L,
                request.getProductType(),
                request.getContractNumber(),
                request.getPriority(),
                request.getContractDate(),
                null,
                null,
                null,
                request.getInterestRatePenalty(),
                request.getMinimalBalance(),
                request.getThresholdAmount(),
                request.getAccountingDetails(),
                request.getRateType(),
                request.getTaxPercentageRate(),
                null,
                null);
        Product rez = productRepository.save(product);
        return rez.getId();
    }

    List<String> saveTppProductRegister(ProductRequest request, Long productId, List<RefProductRegisterType> refProductRegisterTypeList) {
        Account account = getAccount(request);

        List<String> registrIdList = new ArrayList<>();
        for (RefProductRegisterType refProductRegisterType : refProductRegisterTypeList) {
            ProductRegister productRegister = new ProductRegister(
                    null,
                    productId,
                    refProductRegisterType,
                    account.getId(),
                    request.getIsoCurrencyCode(),
                    "OPEN",
                    account.getAccount_number()
            );
            ProductRegister rez = productRegisterRepository.save(productRegister);
            registrIdList.add(rez.getId().toString());
        }
        return registrIdList;
    }


    Account getAccount(ProductRequest request) {
        String defaultPriorityCode = "00";
        List<AccountPool> accountPoolList = accountPoolRepository.findByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCode(request.getBranchCode(),
                request.getIsoCurrencyCode(), request.getMdmCode(), defaultPriorityCode, request.getRegisterType());
        if (accountPoolList.isEmpty() || accountPoolList.get(0).getAccountList() == null || accountPoolList.get(0).getAccountList().isEmpty()) {
            throw new NotFoundException(
                    String.format("Счет не найден по комбинации значений: branchCode:%s isoCurrencyCode:%s MdmCode:%s priorityCode:%s registerType:%s",
                    request.getBranchCode(), request.getIsoCurrencyCode(), request.getMdmCode(), defaultPriorityCode, request.getRegisterType())
            );
        }
        return accountPoolList.get(0).getAccountList().get(0);
    }

    void isArrangementHasNoDoubles(ProductRequest request, Product product) {
        test();
        if (request.getInstanceArrangement() != null) {
            for (Arrangement arrangement : request.getInstanceArrangement()) {
                Optional<Agreement> optionalAgreement = agreementRepository.findByNumber(arrangement.getNumber());
                if (optionalAgreement.isPresent()) {
                    throw new BadRequestException(
                            String.format("Параметр № Дополнительного соглашения (сделки) Number %s уже существует для ЭП с ИД %s",
                                    arrangement.getNumber(), product.getId())
                    );
                }
            }
        }
        test();
    }

    private ProductResponse createAgreement(ProductRequest request) {
        test();

        //Шаг 2.1
        Product product = getTppProduct(request);

        if (request.getInstanceArrangement() == null || request.getInstanceArrangement().length == 0)
            throw new BadRequestException("Массив дополнительных соглашний в запросе пуст");

        //Шаг 2.2
        isArrangementHasNoDoubles(request, product);

        test();

        //Шаг 2.3
        return saveAgreements(request, product);
    }

    Product getTppProduct(ProductRequest request) {
        Optional<Product> optionalTppProduct = productRepository.findById(request.getInstanceId());
        if (optionalTppProduct.isEmpty()) {
            throw new NotFoundException(
                    String.format("Экземпляр продукта с параметром instanceId %s не найден",
                            request.getInstanceId())
            );
        }
        return optionalTppProduct.get();
    }

    ProductResponse saveAgreements(ProductRequest request, Product product) {
        List<String> agreementsIds = new ArrayList<>();
        for (@Valid Arrangement arrangementDto : request.getInstanceArrangement()) {
            Agreement agreement = new Agreement(null, arrangementDto, product);
            Agreement rez = agreementRepository.save(agreement);
            agreementsIds.add(rez.getId().toString());
        }

        return new ProductResponse(product.getId().toString(), null, agreementsIds);
    }


}
