package tr.com.kyilmaz80.myparser.func;

import java.util.Arrays;

public class MaxFunction implements MultiArgMathFunction {

    public static String name = "max";
    public int argCount = -1;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getArgCount() {
        return argCount;
    }

    @Override
    public void setArgCount(int argCount) {
        this.argCount = argCount;
    }

    @Override
    public void setName(String name) {
       this.name = name;
    }



    @Override
    public double calculate(Double[] args) {
        Arrays.sort(args);
        return args[args.length - 1];
    }


}
