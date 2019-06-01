package StackAndQueue.BasicsOfStack;

import java.util.Stack;

import static Utils.Helpers.*;

/*
* Evaluate Reverse Polish Notation
*
* - Evaluate the value of an arithmetic expression in Reverse Polish Notation.
*   1. Valid operators are +, -, *, /.
*   2. Each operand may be an integer or another expression.
* */

public class L150_EvaluateReversePolishNotation {
    public static int evalRPN(String[] tokens) {

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
