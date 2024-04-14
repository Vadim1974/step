package study.stepup.productaccountingservice.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.sql.Timestamp;

@Getter @Setter
@Valid
public class Arrangement
{

    private String generalAgreementId;
    private String supplementaryAgreementId;

    @Pattern(message = "допустимые arrangementType: НСО,ЕЖО,СМО,ДБДС", regexp = "^НСО|ЕЖО|СМО|ДБДС$")
    private String arrangementType;

    private Long shedulerJobId;

    @NotBlank(message = "Обязательный параметр number не заполнен")
    private String number;

    @NotNull(message = "Обязательный параметр openingDate не заполнен")
    private Timestamp openingDate;

    private Timestamp closingDate;
    private Timestamp CancelDate;
    private Long validityDuration;
    private String cancellationReason;
    private String	Status;
    private Timestamp interestCalculationDate;
    private Float interestRate;
    private Float coefficient;
    private String	coefficientAction;
    private Float minimumInterestRate;
    private Float minimumInterestRateCoefficient;
    private Float minimumInterestRateCoefficientAction;
    private Float maximalnterestRate;
    private Float maximalnterestRateCoefficient;
    private String maximalnterestRateCoefficientAction;
}
