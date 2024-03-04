package study.stepup;

public class Fraction implements Fractionable{
    private int num;
    private int denum;
    private int doubleValueIsInvoked = 0;

    @Override
    public int getCountDoubleValueInvoked() {
        return doubleValueIsInvoked;
    }


    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    @Mutator
    @Override
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator
    @Override
    public void setDenum(int denum) {
        this.denum = denum;
    }


    @Cache
    @Override
    public double doubleValue(){
        System.out.println("invoke double name");
        doubleValueIsInvoked++;
        return (double) num/denum;
    }
}
