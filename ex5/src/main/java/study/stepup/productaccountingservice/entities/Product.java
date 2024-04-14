package study.stepup.productaccountingservice.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Valid
@ToString
@Table(name = "tpp_product")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long product_code_id;

    @NotNull
    @Column(nullable = false)
    private Long client_id;

    @NotNull
    @Column(length = 50, nullable = false) @Nationalized
    private String type;

    @NotNull @Setter
    @Column(length = 50, nullable = false) @Nationalized
    private String number;

    @NotNull
    @Column(nullable = false)
    private Long priority;

    @NotNull
    @Column(nullable = false)
    private Timestamp date_of_conclusion;
    private Timestamp start_date_time;
    private Timestamp end_date_time;

    private Long days;

    @Column(columnDefinition = "DECIMAL")
    private Float penalty_rate;

    @Column(columnDefinition = "DECIMAL")
    private Float nso;

    @Column(columnDefinition = "DECIMAL")
    private Float threshold_amount;

    @Column(length = 50) @Nationalized
    private String requisite_type;

    @Column(length = 50) @Nationalized
    private String interest_rate_type;

    @Column(columnDefinition = "DECIMAL")
    private Float tax_rate;

    @Column(length = 100) @Nationalized
    private String reasone_close;

    @Column(length = 50) @Nationalized
    private String state;

}
