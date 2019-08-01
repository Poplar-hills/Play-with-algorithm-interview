package StackAndQueue.BasicsOfStack;

import java.util.*;

import static Utils.Helpers.*;

/*
* Valid Parentheses
*
* - Given a string containing just the characters '(', ')', '{', '}', '[', ']', determine if the input string is valid.
* */

public class L20_ValidParentheses {
    /*
    * 解法1：stack
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        Set<Character> openBrackets = new HashSet<>(Arrays.asList('(', '[', '{'));

        for (char c : s.toCharArray()) {
            if (openBrackets.contains(c))
                stack.push(c);
            else if (stack.isEmpty() || !match(stack.pop(), c))  // 注意 empty 的情况也要考虑
                return false;
        }

        return stack.isEmpty();
    }

    private static boolean match(char c1, char c2) {
        return (c1 == '(' && c2 == ')') || (c1 == '[' && c2 == ']') || (c1 == '{' && c2 == '}');
    }

    public static void main(String[] args) {
        log(isValid("()"));      // expects true
        log(isValid("()[]{}"));  // expects true
        log(isValid("{[]}"));    // expects true
        log(isValid(""));        // expects true
        log(isValid("(]"));      // expects false
        log(isValid("([)]"));    // expects false
        log(isValid("]"));       // expects false
    }
}
