package study.stepup.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
        Account a = new Account("Ваня");
        a.setCurrency(MyCurrency.RUB, 10);
        a.setCurrency(MyCurrency.USD, 1);
        a.setCurrency(MyCurrency.USD, 0);
        System.out.println(a);
    }

}
