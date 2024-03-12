package study.stepup;

public class TestFraction implements Fractionable {
    private int num;
    private int denum;

    private final Hide hide = new Hide();

    private static class Hide{
        long countDoubleValueInvoked = 0;
    }


    public TestFraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(1000)
    public double doubleValue() {
        hide.countDoubleValueInvoked++;
        System.out.println("invoke double value " + hide.countDoubleValueInvoked);
        return (double) num / denum;
    }

    @Override
    public long getCountDoubleValueInvoked() {
        return hide.countDoubleValueInvoked;
    }


}

