package study.stepup.productaccountingservice.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Valid
@ToString
@Table(name = "tpp_product_register")
public class ProductRegister
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @NotNull @Setter
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH, optional = false)
    @JoinColumn(name = "type", nullable = false, referencedColumnName = "value")
    private RefProductRegisterType refProductRegisterType;

    @NotNull
    private Long account;

    @Column(length = 30) @Nationalized
    private String currency_code;

    @Column(length = 50) @Nationalized
    private String state;

    @Column(length = 25) @Nationalized
    private String account_number;
}
