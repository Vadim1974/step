package study.stepup.productaccountingservice.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Valid
@Getter
@ToString
@Table(name = "tpp_ref_product_class")
public class RefProductClass
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="internal_id")
    private Long internalId;

    @NotNull
    @Column(unique = true, nullable = false, length = 100) @Nationalized
    private String value;

    @Column(length = 50) @Nationalized
    private String gbi_code;

    @Column(length = 100) @Nationalized
    private String gbi_name;

    @Column(length = 50) @Nationalized
    private String product_row_code;

    @Column(length = 100) @Nationalized
    private String product_row_name;

    @Column(length = 50) @Nationalized
    private String subclass_code;

    @Column(length = 100) @Nationalized
    private String subclass_name;
}
