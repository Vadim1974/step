package study.stepup.productaccountingservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "account_pool")
public class AccountPool
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // serial PRIMARY KEY,

    @OneToMany(mappedBy = "accountPool", fetch = FetchType.EAGER)
    private List<Account> accountList;

    @Column(length = 50, name = "branch_code") @Nationalized
    private String branchCode; // VARCHAR(50),

    @Column(length = 30, name = "currency_code") @Nationalized
    private String currencyCode; // VARCHAR(30),

    @Column(length = 50, name = "mdm_code") @Nationalized
    private String mdmCode; // VARCHAR(50),

    @Column(length = 30, name = "priority_code") @Nationalized
    private String priorityCode; // VARCHAR(30),

    @Column(length = 50, name = "registry_type_code") @Nationalized
    private String registryTypeCode; // VARCHAR(50)
}
