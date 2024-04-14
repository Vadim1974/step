package study.stepup.productaccountingservice.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import study.stepup.productaccountingservice.models.Arrangement;
//import ru.stepup.study2stage.dto.ArrangementDto;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Valid
@Table(name = "agreement")
@Entity
@ToString
public class Agreement
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // serial PRIMARY KEY,

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product; // product_id integer,

    @Column(length = 50) @Nationalized
    private String generalAgreementId; // VARCHAR(50),

    @Column(length = 50) @Nationalized
    private String supplementary_agreement_id; // VARCHAR(50),

    @Column(length = 50) @Nationalized
    private String arrangement_type; // VARCHAR(50),

//    @ManyToOne(fetch = FetchType.LAZY, optional = true)
//    @JoinColumn(name = "sheduler_job_id", nullable = true)
//    //@OnDelete(action = OnDeleteAction.SET_NULL)
//    private String shedulerJob; //sheduler_job_id BIGINT,

    @NotNull
    @Column(length = 50, nullable = false) @Nationalized
    private String number;// VARCHAR(50),

    @NotNull
    @Column(nullable = false)
    private Timestamp opening_date; //TIMESTAMP,
    private Timestamp closing_date; //TIMESTAMP,
    private Timestamp cancel_date; //TIMESTAMP,

    private Long validity_duration; //BIGINT,

    @Column(length = 100) @Nationalized
    private String cancellation_reason; // VARCHAR(100),

    @Column(length = 50) @Nationalized
    private String status; // VARCHAR(50),

    private Timestamp interest_calculation_date; // TIMESTAMP,

    @Column(columnDefinition = "DECIMAL")
    private Float interest_rate; // DECIMAL,

    @Column(columnDefinition = "DECIMAL")
    private Float coefficient; // DECIMAL,

    @Column(length = 50) @Nationalized
    private String coefficient_action; // VARCHAR(50),

    @Column(columnDefinition = "DECIMAL")
    private Float minimum_interest_rate; // DECIMAL,

    @Column(columnDefinition = "DECIMAL")
    private Float minimum_interest_rate_coefficient; // DECIMAL,

    @Column(length = 50) @Nationalized
    private String minimum_interest_rate_coefficient_action; // VARCHAR(50),

    @Column(columnDefinition = "DECIMAL")
    private Float maximal_interest_rate; // DECIMAL,

    @Column(columnDefinition = "DECIMAL")
    private Float maximal_interest_rate_coefficient; // DECIMAL,

    @Column(length = 50) @Nationalized
    private String maximal_interest_rate_coefficient_action; // VARCHAR(50)

    public Agreement(Long id, Arrangement arrangement, Product product)
    {
        this.id = id;
        this.product = product;
        this.generalAgreementId = arrangement.getGeneralAgreementId();
        this.supplementary_agreement_id =  arrangement.getSupplementaryAgreementId();
        this.arrangement_type = arrangement.getArrangementType();
        this.number = arrangement.getNumber();
        this.opening_date = arrangement.getOpeningDate();
        this.closing_date = arrangement.getClosingDate();
        this.cancel_date = arrangement.getCancelDate();
        this.validity_duration = arrangement.getValidityDuration();
        this.cancellation_reason = arrangement.getCancellationReason();
        this.status = arrangement.getStatus();
        this.interest_calculation_date = arrangement.getInterestCalculationDate();
        this.interest_rate = arrangement.getInterestRate();
        this.coefficient = arrangement.getCoefficient();
        this.coefficient_action = arrangement.getCoefficientAction();
        this.minimum_interest_rate_coefficient = arrangement.getMinimumInterestRateCoefficient();
        this.maximal_interest_rate = arrangement.getMaximalnterestRate();
        this.maximal_interest_rate_coefficient = arrangement.getMaximalnterestRateCoefficient();
        this.maximal_interest_rate_coefficient_action = arrangement.getMaximalnterestRateCoefficientAction();
    }
}
