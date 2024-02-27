package study.stepup.account;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
public class AccountTests {
    @Test
    void setNewAccount_emptyName_throwExeption() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Account(""));
    }

    @Test
    void setAccount_nullName_throwExeption() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Account(null));
    }

    @Test
    void setAccountCur_negativeCount_throwExeption() {
        Account account = new Account("test");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> account.setCurrency(MyCurrency.EUR, -1));
    }

    @Test
    void setNewAccount_correctNameAndCur_returnRUB10() {
        Account a = new Account("Ваня");
        a.setCurrency(MyCurrency.RUB, 10);
        a.setCurrency(MyCurrency.USD, 1);
        a.setCurrency(MyCurrency.USD, null);
        Assertions.assertEquals("Ваня", a.getName());
        HashMap<MyCurrency, Integer> map = new HashMap<>();
        map.put(MyCurrency.RUB, 10);
        Assertions.assertEquals(map, a.getCurrencies());
    }

    @Test
    void setNewAccount_History_retruncorrectName() {
        CareTracker careTracker = new CareTracker();
        Account a = new Account("Ваня");
        careTracker.addMemento(a.createMemento());
        a.setName("Коля");
        Assertions.assertEquals("Коля", a.getName());
        a.restoreFromMemento((Account.AccountMemento) careTracker.getMemento(0));
        Assertions.assertEquals("Ваня", a.getName());
    }

    @Test
    void setNewAccount_undo_retruncorrectName() {
        Account a = new Account("Ваня");
        a.setName("Коля");
        Assertions.assertEquals("Коля", a.getName());
        a.undo();
        Assertions.assertEquals("Ваня", a.getName());
    }


    @Test
    void setNewAccount_undo_retruncorrectCurrency() {
        Account a = new Account("Ваня");
        a.setCurrency(MyCurrency.RUB, null);
        HashMap<MyCurrency, Integer> mapBefore = new HashMap<>();
        HashMap<MyCurrency, Integer> mapAfter = new HashMap<>(mapBefore);
        mapAfter.put(MyCurrency.RUB, 10);
        a.setCurrency(MyCurrency.RUB, 10);
        Assertions.assertEquals(mapAfter, a.getCurrencies());
        a.undo();
        Assertions.assertEquals(mapBefore, a.getCurrencies());
    }


    @Test
    void setNewAccount_undo_retruncorrectCurrency1() {
        MyCurrency cur = MyCurrency.RUB;
        Account a = new Account("Ваня");
        a.setCurrency(cur, 10);
        a.setCurrency(cur, 11);
        a.setCurrency(cur, 12);
        Assertions.assertEquals(12, a.getCurrency(cur));
        a.undo();
        Assertions.assertEquals(11, a.getCurrency(cur));
        a.undo();
        Assertions.assertEquals(10, a.getCurrency(cur));
        a.undo();
        Assertions.assertEquals(0, a.getCurrency(cur));
        a.undo();
        Assertions.assertNull(a.getName());
        Assertions.assertThrows(IllegalArgumentException.class, a::undo);
    }


    @Test
    void setAccountMemento_static_retrunCorrect() {
        Account a1 = new Account("a1");
        Account a2 = new Account("a2");
        Account.AccountMemento m1 = a1.createMemento();
        Account.AccountMemento m2 = a2.createMemento();
        Account a11 = new Account("a11");
        Account a21 = new Account("a21");
        a11.restoreFromMemento(m1);
        a21.restoreFromMemento(m2);
        Assertions.assertEquals(a1.getName(), a11.getName());
        Assertions.assertEquals(a2.getName(), a21.getName());
    }

}
