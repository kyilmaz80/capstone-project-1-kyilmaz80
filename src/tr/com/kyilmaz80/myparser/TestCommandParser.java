package tr.com.kyilmaz80.myparser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestCommandParser {
    public static void main(String[] args) {
        //-enableassertions ile derlenmeli.
        String testName;
        boolean allTests = false;

        if (args.length == 0) {
            System.out.println("Running all tests");
            testName = "test";
            allTests = true;
        } else {
            System.out.println("Running test " + args[0]);
            testName = args[0];
        }

        Method[] methods = TestCommandParser.class.getDeclaredMethods();
        String[] selectedTests = testName.split("[,;]");
        if (selectedTests.length == 1) {
            run(allTests, testName, methods);
        }else {
            for(String selectedTest: selectedTests) {
                run(allTests, selectedTest, methods );
            }
        }




    }

    public static void run(boolean allTests, String testName, Method[] methods) {
        for(Method method: methods) {
            if (allTests || method.getName().equalsIgnoreCase(testName)) {
                if (!method.getName().contains("test")) {
                    continue;
                }
                try {
                    System.out.println("**********" + method.getName().toUpperCase() + "******************") ;
                    method.invoke(null);
                    System.out.println();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    throw new RuntimeException(method.getName() + " failed");
                }
            }
        }
    }

    public static void test1() {
        assert ExpressionParser.parse("5 + 6 * 7").equals("5 6 7 * +");
        assert ExpressionParser.parse("5*6+7").equals("5 6 * 7 +");
        assert ExpressionParser.parse("5/4 *3 +2").equals("5 4 / 3 * 2 +");
        assert ExpressionParser.parse("5/4-3*2").equals("5 4 / 3 2 * -");
    }

    public static void test2() {
        assert ExpressionEvaluator.eval(ExpressionParser.parse("5+6*7")) == 47.0;
        assert ExpressionEvaluator.eval(ExpressionParser.parse("5*6+7")) == 37.0;
        assert ExpressionEvaluator.eval(ExpressionParser.parse("5/4*3+2")) == 5.75;
        assert ExpressionEvaluator.eval(ExpressionParser.parse("5/4-3*2")) == -4.75;
    }

    public static void test3() {
        //assert CommandParser.eval(CommandParser.parse("5*4+cos(0)")) == 21.0;
        //assert CommandParser.eval(CommandParser.parse("5*4+cos(0)*5")) == 25.0;

        String exp = ExpressionParser.parse("5*4+sqrt(cos(0)*25)");
        System.out.println(exp);
        Double res = ExpressionEvaluator.eval(exp);
        System.out.println(res);
        //5 4 * sqrt cos 0 25 * +
        assert ExpressionEvaluator.eval(exp) == 25.0;
        //String postfix = CommandParser.parse("5*6+7");
        //System.out.println(postfix);
        //Double val = CommandParser.eval(postfix);
        //System.out.println(val);
    }

    public static void test4() {
        String exp = ExpressionParser.parse("5*4+sqrt(pow(5,2))");
        System.out.println(exp);
        assert ExpressionParser.parse("5*4+sqrt(pow(5,2))").equals("5 4 * sqrt pow 5 2 +");

        Double res = ExpressionEvaluator.eval(exp);
        System.out.println(res);
        //5 4 * sqrt cos 0 25 * +
        //5 4 * sqrt pow 5 2 +
    }

    public static void test5() {
        assert ExpressionEvaluator.eval(ExpressionParser.parse("5*4*cos(0)*5")) == 100.0;
        assert ExpressionEvaluator.eval(ExpressionParser.parse("5*(4+1)+5")) == 30.0;

        //String infixExpression = "5*(4+1)+5";
        //String expectedPostfixExpression = "5 4 1 + * 5 +";

        //String infixExpression = "5+6*7";
        //String expectedPostfixExpression = "5 6 7 * +";

        String infixExpression = "5*4+sqrt(cos(0)*25)";
        String expectedPostfixExpression = "5 4 * sqrt cos 0 25 * +";
        String exp = ExpressionParser.parse(infixExpression);
        System.out.println(exp);
        assert ExpressionParser.parse(infixExpression).equals(expectedPostfixExpression);


        assert ExpressionEvaluator.eval("5 4 1 + * 5 +") == 30.0; //works
        //assert CommandParser.eval(CommandParser.parse("4+6/2*3-1")) == 12.0;
    }

    public static void test6() {
        assert ExpressionEvaluator.eval(ExpressionParser.parse("5*(4+5)")) == 45.0;
        assert ExpressionEvaluator.eval(ExpressionParser.parse("5*(4+sqrt(25))")) == 45.0;
        assert ExpressionEvaluator.eval(ExpressionParser.parse("5*(4+sqrt(25)*cos(0))")) == 45.0;
        assert ExpressionEvaluator.eval(ExpressionParser.parse("5*(4+5)")) == 45.0;
        System.out.println(ExpressionEvaluator.eval(ExpressionParser.parse("5+pow(5,2)")));
        assert ExpressionEvaluator.eval(ExpressionParser.parse("5+pow(5,2)")) == 30.0;
        assert ExpressionEvaluator.eval(ExpressionParser.parse("5+pow(5,2)*sqrt(4)")) == 55.0;
        System.out.println();

        //5 pow 5 2 sqrt 4 * +
        //5 25 sqrt 4 * +
        //5 25 2 * +
        //5 50 +
        //55
        String expression2 = "5+pow(5,2)*sqrt(4)";
        String postfixExpression2 = ExpressionParser.parse(expression2);
        Double res2 = ExpressionEvaluator.eval(postfixExpression2);
        System.out.println(expression2);
        System.out.println(postfixExpression2);
        System.out.println("res1: " + res2);
    }

    public static void test7() {
        //String postfixExpression = CommandParser.parse("5*(4+sqrt(pow(5,2)))");
        //assert CommandParser.eval(postfixExpression) == 45.0;
        //5 4 sqrt pow 5 2 + *
        String expression1 = "5*(4+sqrt(pow(5,2)))";
        //String expression = "5*(4+sqrt(25))";
        String postfixExpression1 = ExpressionParser.parse(expression1);
        System.out.println(postfixExpression1);
        assert postfixExpression1.equalsIgnoreCase("5 4 sqrt pow 5 2 + *");
        Double res1 = ExpressionEvaluator.eval(postfixExpression1);
        System.out.println(expression1);
        System.out.println(postfixExpression1);
        System.out.println("5*(4+sqrt(pow(5,2))) = " + res1);
        assert res1 == 45.0;

    }

    public static void test8() {
        //Postfix operator eklerken operator ayni geldi case
        assert ExpressionEvaluator.eval(ExpressionParser.parse("4+6/2*3-1")) == 12.0;
    }

    public static void test9() {
        assert ExpressionEvaluator.eval(ExpressionParser.parse("cos(0)")) == 1.0;
        assert ExpressionParser.parse("cos(0)").equals("cos 0");
    }

    public static void test10() {
        // corner cases
//        String expression = "cos(0";
//        String postfix = CommandParser.parse(expression);
//        System.out.println(postfix);
//        assert postfix == null;
//
//        expression = "1+cos(0";
//        postfix = CommandParser.parse(expression);
//        System.out.println(postfix);
//        assert postfix == null;

//        postfix = CommandParser.parse("5*(4+sqrt(25)*cos(0)");
//        assert postfix == null;
        String expression = "5*(4+sqrt(25)*cos(0))";
        String postfixExpected = "5 4 sqrt 25 cos 0 * + *";
        String postfixResult = ExpressionParser.parse(expression);
        assert postfixResult.equals(postfixExpected);
        System.out.println(postfixResult);
        assert ExpressionEvaluator.eval(postfixResult) == 45.0;

        expression = "5*(4+sqrt(25)*cos(1))";
        System.out.println(expression);
        Double res = ExpressionEvaluator.eval(ExpressionParser.parse(expression));
        System.out.println(res);

        //Double res = CommandParser.eval(CommandParser.parse("5*(4+sqrt(25)*cos(0))"));
        //System.out.println(res);
    }

    public static void test11() {
        //mismatch parenthesis
        String expression = "5*cos(0";
        System.out.println(expression + ") eksik ) ! Beklenen durum");
        String postfixExp = ExpressionParser.parse(expression);
        System.out.println(postfixExp);
    }

    public static void test12() {
        String expression = "1+max(4,5,6)";
        String postfixExpected = "1 max3 4 5 6 +";
        String postfixResult = ExpressionParser.parse(expression);
        assert postfixResult.equals(postfixExpected);
        System.out.println(postfixResult);
        Double res = ExpressionEvaluator.eval(postfixResult);
        System.out.println(res);
        assert res == 7.0;
    }

    public static void test13() {
        String expression = "max(4,5,6)+2";
        String postfixExpected = "max3 4 5 6 2 +";
        String postfixResult = ExpressionParser.parse(expression);
        System.out.println(postfixResult);
        assert postfixResult.equals(postfixExpected);
        Double res = ExpressionEvaluator.eval(postfixResult);
        assert res == 8.0;
        System.out.println(res);
    }

    public static void test14() {
        String expression = "max(4,5,6)";
        String postfixExpected = "max3 4 5 6";
        String postfixResult = ExpressionParser.parse(expression);
        assert postfixResult.equals(postfixExpected);
        System.out.println(postfixResult);
        Double res = ExpressionEvaluator.eval(postfixResult);
        assert res == 6.0;
        System.out.println(res);
    }

    public static void test15() {
        String expression = "max(4,1,3,0)";
        String postfixExpected = "max4 4 1 3 0";
        String postfixResult = ExpressionParser.parse(expression);
        assert postfixResult.equals(postfixExpected);
        System.out.println(postfixResult);
        Double res = ExpressionEvaluator.eval(postfixResult);
        assert res == 4.0;
        System.out.println(res);
    }

    public static void test16() {
        String expression = "cos(0)+max(3,7)+5";
        String postfixExpected = "cos 0 max2 3 7 + 5 +";
        String postfixResult = ExpressionParser.parse(expression);
        System.out.println(postfixResult);
        assert postfixResult.equals(postfixExpected);
        Double res = ExpressionEvaluator.eval(postfixResult);
        System.out.println(res);
        assert res == 13.0;

    }

    public static void test17() {
        String expression = "1+max(4,5,1)+3";
        String postfixExpected = "1 max3 4 5 1 + 3 +";
        String postfixResult = ExpressionParser.parse(expression);
        System.out.println(postfixResult);
        assert postfixResult.equals(postfixExpected);
        Double res = ExpressionEvaluator.eval(postfixResult);
        System.out.println(res);
        assert res == 9.0;

    }
    public static void test18() {
        String expression = "1+cos(max(4,5,1))+3";
        String postfixExpected = "1 cos max3 4 5 1 + 3 +";
        String postfixResult = ExpressionParser.parse(expression);
        System.out.println(postfixResult);
        assert postfixResult.equals(postfixExpected);
        Double res = ExpressionEvaluator.eval(postfixResult);
        System.out.println(res);
        assert res == 4.28366218546322625;
    }

    public static void test19() {
        String expression = "1+max(4,5,cos(0))+3";
        String postfixExpected = "1 max3 4 5 cos 0 + 3 +";
        String postfixResult = ExpressionParser.parse(expression);
        System.out.println("postfix result: " + postfixResult);
        assert postfixResult.equals(postfixExpected);
        Double res = ExpressionEvaluator.eval(postfixResult);
        System.out.println(res);
        assert res == 9.0;
    }

    public static void test20() {
        String expression = "1+max(4,5,pow(5,2))+3";
        String postfixExpected = "1 max3 4 5 pow 5 2 + 3 +";
        String postfixResult = ExpressionParser.parse(expression);
        System.out.println("postfix result: " + postfixResult);
        assert postfixResult.equals(postfixExpected);
        Double res = ExpressionEvaluator.eval(postfixResult);
        System.out.println(res);
        assert res == 29.0;
    }

    public static void test21() {
        String expression = "cos(cos(0))";
        String postfixExpected = "cos cos 0";
        String postfixResult = ExpressionParser.parse(expression);
        System.out.println("postfix result: " + postfixResult);
        assert postfixResult.equals(postfixExpected);
        Double res = ExpressionEvaluator.eval(postfixResult);
        System.out.println(res);
        assert res == 0.5403023058681398;
    }

    public static void test22() {
        String expression = "1 + cos(cos(cos(0)))";
        String postfixExpected = "1 cos cos cos 0 +";
        String postfixResult = ExpressionParser.parse(expression);
        System.out.println("postfix result: " + postfixResult);
        assert postfixResult.equals(postfixExpected);
        Double res = ExpressionEvaluator.eval(postfixResult);
        System.out.println(res);
        assert res == 1.8575532158463934;
    }

    public static void test23() {
        String expression = "cos(cos(cos(0)))";
        String postfixExpected = "cos cos cos 0";
        String postfixResult = ExpressionParser.parse(expression);
        System.out.println("postfix result: " + postfixResult);
        assert postfixResult.equals(postfixExpected);
        Double res = ExpressionEvaluator.eval(postfixResult);
        System.out.println(res);
        assert res == 0.8575532158463934;
    }

    public static void test24() {
        String expression = "cos(cos(pow(5,2)))";
        String postfixExpected = "cos cos pow 5 2";
        String postfixResult = ExpressionParser.parse(expression);
        System.out.println("postfix result: " + postfixResult);
        assert postfixResult.equals(postfixExpected);
        Double res = ExpressionEvaluator.eval(postfixResult);
        System.out.println(res);
        assert res == 0.5476838819485967;
    }

    public static void test25() {
        String expression = "avg(4,6)";
        String postfixExpected = "avg2 4 6";
        String postfixResult = ExpressionParser.parse(expression);
        System.out.println("postfix result: " + postfixResult);
        assert postfixResult.equals(postfixExpected);
        Double res = ExpressionEvaluator.eval(postfixResult);
        System.out.println(res);
        assert res == 5.0;
    }
    public static void test26() {
        String expression = "avg(4,6,8)";
        String postfixExpected = "avg3 4 6 8";
        String postfixResult = ExpressionParser.parse(expression);
        System.out.println("postfix result: " + postfixResult);
        assert postfixResult.equals(postfixExpected);
        Double res = ExpressionEvaluator.eval(postfixResult);
        System.out.println(res);
        assert res == 6.0;
    }

    public static void test27() {
        String expression = "4+avg(4,6,8)";
        String postfixExpected = "4 avg3 4 6 8 +";
        String postfixResult = ExpressionParser.parse(expression);
        System.out.println("postfix result: " + postfixResult);
        assert postfixResult.equals(postfixExpected);
        Double res = ExpressionEvaluator.eval(postfixResult);
        System.out.println(res);
        assert res == 10.0;
    }


}
