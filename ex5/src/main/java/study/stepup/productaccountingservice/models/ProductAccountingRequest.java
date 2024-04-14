package study.stepup.productaccountingservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Getter @Setter
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class ProductAccountingRequest
{
    @NotNull(message="Имя обязательного атрибута instanceId не заполнено")
    private Long instanceId;

    @NotBlank(message = "Имя обязательного атрибута registryTypeCode не заполнено")
    private String registryTypeCode;

    @NotBlank(message = "Имя обязательного атрибута accountType не заполнено")
    @Pattern(message = "accountType: Клиентский или Внутрибанковский", regexp = "^Клиентский|Внутрибанковский$")
    private String accountType;

    @NotBlank(message = "Имя обязательного атрибута currencyCode не заполнено")
    @Pattern(message = "currencyCode - трехзначный код ", regexp = "^\\d{3}$")
    private String currencyCode;

    @NotBlank(message = "Имя обязательного атрибута BranchCode не заполнено")
    private String branchCode;

    @NotNull(message = "Имя обязательного атрибута priorityCode не заполнено")
    @Pattern(message = "priorityCode всегда 00", regexp = "^00$")
    private String priorityCode;

    @NotBlank(message = "Имя обязательного атрибута mdmCode не заполнено")
    private String mdmCode;

    private String clientCode;
    private String trainRegion;
    private String counter;
    private String salesCode;

}
