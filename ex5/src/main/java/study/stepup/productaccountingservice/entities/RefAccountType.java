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
@Table(name = "tpp_ref_account_type")
public class RefAccountType
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internal_id;

    @NotNull
    @Column(unique = true, nullable = false, length = 100)
    @Nationalized
    private String value;
}
