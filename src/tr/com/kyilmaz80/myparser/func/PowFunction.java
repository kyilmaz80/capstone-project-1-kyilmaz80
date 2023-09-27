package tr.com.kyilmaz80.myparser.func;

public class PowFunction implements DoubleArgMathFunction{
    private static String name = "pow";
    @Override
    public double calculate(double arg1, double arg2) {
        return Math.pow(arg1, arg2);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
