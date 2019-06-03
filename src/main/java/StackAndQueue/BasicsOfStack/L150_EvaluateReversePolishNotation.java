package StackAndQueue.BasicsOfStack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.BiFunction;

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
    * 解法1：Stack
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int evalRPN(String[] tokens) {
        Deque<String> stack = new ArrayDeque<>();

        for (String s : tokens) {
            if (isOperator(s)) {
              int operand2 = Integer.parseInt(stack.pop());
              int operand1 = Integer.parseInt(stack.pop());
              stack.push(calculate(operand1, operand2, s));
            } else {
                stack.push(s);
            }
        }

        return Integer.parseInt(stack.pop());
    }

    private static boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/");
    }

    private static String calculate(int operand1, int operand2, String operator) {
        switch (operator) {
            case "+": return String.valueOf(operand1 + operand2);
            case "-": return String.valueOf(operand1 - operand2);
            case "*": return String.valueOf(operand1 * operand2);
            case "/": return String.valueOf(operand1 / operand2);
            default: return null;
        }
    }

    /*
    * 解法2：Stack（lambda 版）
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int evalRPN2(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();

        for (String s : tokens) {
            BiFunction<Integer, Integer, Integer> f = getFunction(s);
            if (f != null) {
                int operand2 = stack.pop();
                int operand1 = stack.pop();
                stack.push(f.apply(operand1, operand2));
            } else {
                stack.push(Integer.parseInt(s));
            }
        }

        return stack.pop();
    }

    private static BiFunction<Integer, Integer, Integer> getFunction(String operator) {
        switch (operator) {
            case "+": return (x, y) -> x + y;
            case "-": return (x, y) -> x - y;
            case "*": return (x, y) -> x * y;
            case "/": return (x, y) -> x / y;
        }
        return null;
    }

    public static void main(String[] args) {
        log(evalRPN2(new String[]{"2", "1", "+", "3", "*"}));   // expects 9. ((2 + 1) * 3) = 9

        log(evalRPN2(new String[]{"4", "13", "5", "/", "+"}));  // expects 6. (4 + (13 / 5)) = 6

        log(evalRPN2(new String[]{"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"}));
        // expects 22.
        // ((10 * (6 / ((9 + 3) * -11))) + 17) + 5
        //= ((10 * (6 / (12 * -11))) + 17) + 5
        //= ((10 * (6 / -132)) + 17) + 5
        //= ((10 * 0) + 17) + 5
        //= (0 + 17) + 5
        //= 17 + 5
    }
}
