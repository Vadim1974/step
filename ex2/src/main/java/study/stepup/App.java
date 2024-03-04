package study.stepup;

public class App
{
    public static void main( String[] args )
    {
        Fraction fr = new Fraction(2,5);
        Fractionable num = Utils.cache(fr);
        System.out.println(num.doubleValue()); ;
        System.out.println(num.doubleValue());
        System.out.println(num.doubleValue());
        System.out.println("Set num(5)");
        num.setNum(5);
        System.out.println(num.doubleValue());
        System.out.println(num.doubleValue());
    }
}
