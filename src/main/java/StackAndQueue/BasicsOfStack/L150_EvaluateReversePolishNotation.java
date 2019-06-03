package StackAndQueue.BasicsOfStack;

import java.util.Stack;

import static Utils.Helpers.*;

/*
* Evaluate Reverse Polish Notation
*
* - Evaluate the value of an arithmetic expression in Reverse Polish Notation.
*   1. Valid operators are +, -, *, /.
*   2. Each operand may be an integer or another expression.
*   3. The given RPN expression is always valid. That means the expression would always evaluate to a result
*      and there won't be any divide by zero operation.
* */

public class L150_EvaluateReversePolishNotation {
    /*
    * 解法1：
    * */
    public static int evalRPN(String[] tokens) {
        Stack<String> stack = new Stack<>();

        for (String s : tokens) {
            if (isOperator(s)) {
              int operand2nd = Integer.parseInt(stack.pop());
              int operand1st = Integer.parseInt(stack.pop());
              stack.push(calculate(operand1st, operand2nd, s));
            } else {
                stack.push(s);
            }
        }

        return Integer.parseInt(stack.pop());
    }

    private static boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/");
    }

    private static String calculate(int operand1st, int operand2nd, String s) {
        switch (s) {
            case "+": return String.valueOf(operand1st + operand2nd);
            case "-": return String.valueOf(operand1st - operand2nd);
            case "*": return String.valueOf(operand1st * operand2nd);
            case "/": return String.valueOf(operand1st / operand2nd);
            default: return null;
        }
    }

    public static void main(String[] args) {
        log(evalRPN(new String[]{"2", "1", "+", "3", "*"}));   // expects 9. ((2 + 1) * 3) = 9

        log(evalRPN(new String[]{"4", "13", "5", "/", "+"}));  // expects 6. (4 + (13 / 5)) = 6

        log(evalRPN(new String[]{"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"}));
        // expects 22.
        // ((10 * (6 / ((9 + 3) * -11))) + 17) + 5
        //= ((10 * (6 / (12 * -11))) + 17) + 5
        //= ((10 * (6 / -132)) + 17) + 5
        //= ((10 * 0) + 17) + 5
        //= (0 + 17) + 5
        //= 17 + 5
    }
}
