package study.stepup;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class UtilTest {

    @Test
    @DisplayName("Проверка работы кэша")
    public void isUtilCacheCorrect() {
        Fraction fr = new Fraction(2, 5);
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
}
