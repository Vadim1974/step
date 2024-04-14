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
@Getter
@Valid
@ToString
@Table(name = "account")
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH, optional = false)
    @JoinColumn(name = "account_pool_id", nullable = false) @ToString.Exclude
    private AccountPool accountPool;

    @NotNull
    @Column(length = 25, nullable = false) @Nationalized
    private String account_number;

    @Column(columnDefinition = "BOOLEAN")
    private Boolean bussy;
}
