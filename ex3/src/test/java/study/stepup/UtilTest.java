package study.stepup;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class UtilTest {

    @Test
    @DisplayName("Проверка работы кэша")
    public void isUtilCacheCorrect() {
        TestFraction fr = new TestFraction(2, 5);
        Fractionable fra = Utils.cache(fr);
        //getCountDoubleValueInvoked - счетчик вызовов метода doubleValue
        Assertions.assertEquals(0, fra.getCountDoubleValueInvoked());

        //Три вызова, но только первый не из кэша
        double doubleValue = (double) 2/5;
        Assertions.assertTrue( fra.doubleValue() == doubleValue && fra.getCountDoubleValueInvoked() == 1);
        Assertions.assertTrue( fra.doubleValue() == doubleValue && fra.getCountDoubleValueInvoked() == 1);
        Assertions.assertTrue( fra.doubleValue() == doubleValue && fra.getCountDoubleValueInvoked() == 1);

        //Проверка, что после мутатора будет вызов не из кэша
        fra.setDenum(8);
        doubleValue = (double) 2/8;
        Assertions.assertTrue( fra.doubleValue() == doubleValue && fra.getCountDoubleValueInvoked() == 2);
        Assertions.assertTrue( fra.doubleValue() == doubleValue && fra.getCountDoubleValueInvoked() == 2);
    }

    @Test
    @DisplayName("Проверка работы кэша со временем устаревания")
    public void isUtilTimeCacheCorrect() {
        TestFraction fr= new TestFraction(2,3);
        Fractionable num =Utils.cache(fr);
        //getCountDoubleValueInvoked - счетчик вызовов метода doubleValue
        Assertions.assertEquals(0, num.getCountDoubleValueInvoked());

        double doubleValue = (double) 2/3;
        Assertions.assertTrue( num.doubleValue() == doubleValue && num.getCountDoubleValueInvoked() == 1);
        Assertions.assertTrue( num.doubleValue() == doubleValue && num.getCountDoubleValueInvoked() == 1);

        doubleValue = (double) 5/3;
        num.setNum(5);
        Assertions.assertTrue( num.doubleValue() == doubleValue && num.getCountDoubleValueInvoked() == 2);
        Assertions.assertTrue( num.doubleValue() == doubleValue && num.getCountDoubleValueInvoked() == 2);

        doubleValue = (double) 2/3;
        num.setNum(2);
        Assertions.assertTrue( num.doubleValue() == doubleValue && num.getCountDoubleValueInvoked() == 2);
        Assertions.assertTrue( num.doubleValue() == doubleValue && num.getCountDoubleValueInvoked() == 2);

        System.out.println("Thread.sleep(1500)");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue( num.doubleValue() == doubleValue && num.getCountDoubleValueInvoked() == 3);
        Assertions.assertTrue( num.doubleValue() == doubleValue && num.getCountDoubleValueInvoked() == 3);
    }

}
