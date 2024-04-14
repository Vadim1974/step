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
@Valid
@Getter
@ToString
@Table(name = "tpp_ref_product_register_type")
public class RefProductRegisterType
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internal_id;

    @NotNull @Setter
    @Column(unique = true, nullable = false, length = 100) @Nationalized
    private String value;

    @NotNull
    @Column(length = 100, nullable = false) @Nationalized
    private String register_type_name;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH, optional = false)
    @JoinColumn(name = "product_class_code", nullable = false, referencedColumnName = "value")
    private RefProductClass refProductClass;
    //private String productClassCode;

    private Timestamp register_type_start_date;
    private Timestamp register_type_end_date;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH, optional = false)
    @JoinColumn(name = "account_type", nullable = false, referencedColumnName = "value")
    private RefAccountType refAccountType;
}
