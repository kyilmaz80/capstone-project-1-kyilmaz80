public class TestCommandParser {
    public static void main(String[] args) {
        //test1();
        //test2();
        test3();
    }

    public static void test1() {
        assert CommandParser.parse("5+6*7").equals("5 6 7 * +");
        assert CommandParser.parse("5*6+7").equals("5 6 * 7 +");
        assert CommandParser.parse("5/4*3+2").equals("5 4 / 3 * 2 +");
        assert CommandParser.parse("5/4-3*2").equals("5 4 / 3 2 * -");
    }

    public static void test2() {
        assert CommandParser.execute(CommandParser.parse("5+6*7")) == 47.0;
        assert CommandParser.execute(CommandParser.parse("5*6+7")) == 37.0;
        assert CommandParser.execute(CommandParser.parse("5/4*3+2")) == 5.75;
        assert CommandParser.execute(CommandParser.parse("5/4-3*2")) == -4.75;
    }

    public static void test3() {
        assert CommandParser.execute(CommandParser.parse("5*4+cos(0)")) == 21.0;
        assert CommandParser.execute(CommandParser.parse("5*4+cos(0)*5")) == 25.0;

        String exp = CommandParser.parse("5*4+sqrt(cos(0)*25)");
        System.out.println(exp);
        Double res = CommandParser.execute(exp);
        System.out.println(res);
        //5 4 * sqrt cos 0 25 * +
        //assert CommandParser.execute(exp) == 25.0;
        //String postfix = CommandParser.parse("5*6+7");
        //System.out.println(postfix);
        //Double val = CommandParser.execute(postfix);
        //System.out.println(val);
    }
}
