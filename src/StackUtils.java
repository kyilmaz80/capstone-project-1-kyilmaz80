import java.util.Stack;

public class StackUtils {
    public static double doOperation(double val1, double val2, Operators op) {
        double result = -1;
        switch(op) {
            case MULTIPLICATION -> result = val2 * val1;
            case DIVISION -> result = val2 / val1;
            case ADDITION -> result = val2 + val1;
            case SUBTRACTION -> result = val2 - val1;
            default -> System.out.println("ARITHMETIC OP NOT IMPLEMENTED!");
        }
        return result;
    }

    public static double doCalculateFunction(String funcStr, double[] args) {
        double result = -1;
        //String[] funcArray = funcStr.split("\\(");
        //String func = funcArray[0].toLowerCase();
        //double val = double.valueOf(funcs);
        switch(funcStr) {
            case "cos" -> result = Math.cos(args[0]);
            case "sin" -> result = Math.sin(args[0]);
            case "pow" -> result = Math.pow(args[1], args[0]);
            case "sqrt" -> result = Math.sqrt(args[0]);
            default -> System.out.println("FUNC NOT IMPLEMENTED!");
        }
        return result;
        //return Arrays.binarySearch(Constants.ALLOWED_MATH_FUNCTIONS, funcArray[0].toLowerCase()) >= 0;
    }
    public static double doArithmeticOperationOnStack(Stack<String> stack, String tokenString) {
        double val1 = Double.parseDouble(stack.pop());
        //can be operand or func
        double val2 = Double.parseDouble(stack.pop());

        Operators operator = Operators.fromSymbol(tokenString);
        return StackUtils.doOperation(val1, val2, operator);
        //stack.push(String.valueOf(result));
    }

    public static double doFuncOperationOnStack(Stack<String> stack, String funcStr) {

        int varCount = getFunctionArgCount(funcStr);
        double[] vals = new double[varCount];
        for(int i = 0; i < varCount; i++) {
            vals[i] = Double.parseDouble(stack.pop());
        }
        //disregard the func
        //stack.pop();
        //stack.push(String.valueOf(result));
        return doCalculateFunction(funcStr, vals);
    }

    public static String doGetFuncNameOnStack(Stack<String> stack) {
        String topElement = stack.pop();
        String topElementNext = stack.pop();
        String topElementNext2 = "";
        if (!stack.isEmpty() || stack.size() > 2) {
            topElementNext2 = stack.pop();
            stack.push(topElementNext2);
        }
        stack.push(topElementNext);
        stack.push(topElement);

        if (TokenUtils.isTokenMathFunction(topElement)) {
            return topElement;
        } else if (TokenUtils.isTokenMathFunction(topElementNext)) {
            return topElementNext;
        } else if (TokenUtils.isTokenMathFunction(topElementNext2)) {
            return topElementNext2;
        } else {
            //System.err.println("Stack overflow for function search!");
            return null;
        }
    }

    public static void doAppendOperatorToPostfixExpression(StringBuilder sb, String element) {
        String str = sb.toString().trim();
        int lastIndex = str.length()-1;
        String last = String.valueOf(str.charAt(lastIndex));
        Operators opNew = Operators.fromSymbol(element.trim());
        Operators opLast = Operators.fromSymbol(last);

        if (opNew == Operators.LEFT_PARENTHESES || opNew == Operators.RIGHT_PARENTHESES) {
            return;
        }

        if (opNew == null || opLast == null) {
            sb.append(element);
            return;
        }
        // dirty poor man's cozum
        //right parentheses not expected
        //because of poor code when input is function

        if (opNew.isOperatorHighestPriorityFrom(opLast)) {
            sb.append(element);
        }else if (opNew.isOperatorSamePriorityTo(opLast)) {
            System.err.println("Postfix operator eklerken operator ayni geldi!");
        } else {
            sb.append(element);
        }
    }
    public static boolean isLastTwoFuncOnStack(Stack<String> stack) {
        if (stack.size() < 2) {
            return false;
        }
        String op1 = stack.pop();
        String op2 = stack.pop();
        stack.push(op2);
        stack.push(op1);

        return TokenUtils.isTokenMathFunction(op1) && TokenUtils.isTokenMathFunction(op2);
    }

    public static boolean isBeforeLastOnStackIsFunction(Stack<String> stack) {
        if (stack.size() < 2) {
            return false;
        }
        String op1 = stack.pop();
        String op2 = stack.pop();
        stack.push(op2);
        stack.push(op1);

        return TokenUtils.isTokenMathFunction(op2);
    }

    public static String doGetBeforeLastOnStack(Stack<String> stack) {
        if (stack.size() < 2) {
            return null;
        }
        String op1 = stack.pop();
        String op2 = stack.pop();
        stack.push(op2);
        stack.push(op1);
        return op2;
    }
    public static boolean isBeforeLastOnStackIsLeftParentheses(Stack<String> stack) {
        if (stack.size() < 2) {
            return false;
        }
        String op1 = stack.pop();
        String op2 = stack.pop();
        stack.push(op2);
        stack.push(op1);

        return Operators.fromSymbol(op2) == Operators.LEFT_PARENTHESES;
    }

    public static boolean isOperationOnStackArithmetic(Stack<String> stack) {
        String val1 = stack.pop();
        String val2 = stack.pop();
        stack.push(val2);
        stack.push(val1);

        return TokenUtils.isTokenNumerical(val1) && TokenUtils.isTokenNumerical(val2);
    }

    public static boolean isOperationOnStackFunc(Stack<String> stack) {
        return !isOperationOnStackArithmetic(stack);
    }

    public static int getFunctionArgCount(String funcStr) {
        int result = 0;
        if (funcStr.equalsIgnoreCase("") || funcStr == null) {
            return -1;
        }
        switch(funcStr) {
            case "cos","sin","sqrt" -> result = 1;
            case "pow" -> result = 2;
            default -> System.out.println("NOT IMPLEMENTED!");
        }
        return result;
    }
}