package study.stepup.productaccountingservice.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;


import java.sql.Timestamp;


@Valid
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ProductRequest
{
    private Long instanceId;

    @NotBlank(message = "Имя обязательного атрибута productType не заполено")
    @Pattern(message = "допустимые productType: НСО, СМО, ЕЖО, ДБДС, договор", regexp = "^НСО|СМО|ЕЖО|ДБДС|договор$")
    private String productType;

    @NotBlank(message = "Имя обязательного атрибута productCode не заполено")
    private String productCode;

    @NotBlank(message = "Имя обязательного атрибута registerType не заполено")
    private String registerType;

    @NotBlank(message = "Имя обязательного атрибута mdmCode не заполено")
    private String mdmCode;

    @Setter
    @NotBlank(message = "Имя обязательного атрибута contractNumber не заполено")
    private String contractNumber;

    @NotNull(message = "Имя обязательного атрибута contractDate не заполено")
    private Timestamp contractDate;

    @NotNull(message = "Имя обязательного атрибута priority не заполено")
    private Long priority;

    private Float interestRatePenalty;
    private Float minimalBalance;
    private Float thresholdAmount;
    private String accountingDetails;

    @Pattern(message = "допустимые значения rateType: дифференцированная, прогрессивная", regexp = "^дифференцированная|прогрессивная$")
    private String rateType;

    private Float taxPercentageRate;

    private Float technicalOverdraftLimitAmount;

    @NotNull(message = "Имя обязательного атрибута contractId не заполено")
    private Integer contractId;

    @NotBlank(message = "Имя обязательного атрибута branchCode не заполено")
    private String branchCode;

    @NotBlank(message = "Имя обязательного атрибута isoCurrencyCode не заполено")
    @Pattern(message = "isoCurrencyCode - трехзначный код ", regexp = "^\\d{3}$")
    private String isoCurrencyCode;

    @NotBlank(message = "Имя обязательного атрибута urgencyCode не заполено")
    @Pattern(message = "urgencyCode всегда 00", regexp = "^00$")
    private String urgencyCode;

    private Integer referenceCode;
    private AdditionalPropertiesVip additionalPropertiesVip;

    @Valid
    private Arrangement[] instanceArrangement;



}
