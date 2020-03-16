package StackAndQueue.S1_BasicsOfStack;

import java.util.*;
import java.util.function.BiFunction;

import static Utils.Helpers.*;

/*
 * Evaluate Reverse Polish Notation
 *
 * - Evaluate the value of an arithmetic expression in Reverse Polish Notation (RPN, 逆波兰表示法).
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
        Stack<Integer> stack = new Stack<>();
        Set<String> set = new HashSet<>(Arrays.asList("+", "-", "*", "/"));

        for (String t : tokens) {
            if (set.contains(t)) {
                int operand2 = stack.pop();
                int operand1 = stack.pop();
                stack.push(calculate(operand1, operand2, t));
            } else {
                stack.push(Integer.parseInt(t));
            }
        }

        return stack.pop();
    }

    private static int calculate(int operand1, int operand2, String operator) {
        switch (operator) {
            case "+": return operand1 + operand2;
            case "-": return operand1 - operand2;
            case "*": return operand1 * operand2;
            case "/": return operand1 / operand2;
            default: throw new IllegalArgumentException("Invalid operator");
        }
    }

    /*
     * 解法2：Stack（FP 版）
     * - 思路：与解法1一致。
     * - 实现：根据 operator 得到对应的函数，再 apply 操作数。更多函数式接口 SEE: http://ocpj8.javastudyguide.com/ch10.html
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int evalRPN2(String[] tokens) {
        Stack<Integer> stack = new Stack<>();

        for (String t : tokens) {
            BiFunction<Integer, Integer, Integer> fn = getOperation(t);
            if (fn != null) {
                int operand2 = stack.pop();
                int operand1 = stack.pop();
                stack.push(fn.apply(operand1, operand2));
            } else {
                stack.push(Integer.parseInt(t));
            }
        }

        return stack.pop();
    }

    private static BiFunction<Integer, Integer, Integer> getOperation(String operator) {
        switch (operator) {
            case "+": return (x, y) -> x + y;
            case "-": return (x, y) -> x - y;
            case "*": return (x, y) -> x * y;
            case "/": return (x, y) -> x / y;
            default: throw new IllegalArgumentException("Invalid operator");
        }
    }

    public static void main(String[] args) {
        log(evalRPN(new String[]{"2", "1", "+", "3", "*"}));   // expects 9. ((2 + 1) * 3) = 9

        log(evalRPN(new String[]{"4", "13", "5", "=", "+"}));  // expects 6. (4 + (13 / 5)) = 6

        log(evalRPN(new String[]{"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"}));
        // expects 22. ((10 * (6 / ((9 + 3) * -11))) + 17) + 5 = 22
    }
}
