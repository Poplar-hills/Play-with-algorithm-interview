package StackAndQueue.S1_BasicsOfStack;

import java.util.*;

import static Utils.Helpers.*;

/*
 * Valid Parentheses
 *
 * - Given a string containing just the characters '(', ')', '{', '}', '[', ']', determine if the input string is valid.
 * */

public class L20_ValidParentheses {
    /*
    * 解法1：Stack
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        Set<Character> openBrackets = new HashSet<>(Arrays.asList('(', '[', '{'));

        for (char c : s.toCharArray()) {
            if (openBrackets.contains(c))
                stack.push(c);
            else if (stack.isEmpty() || !match(stack.pop(), c))  // 注意若 stack 为空（open bracket 不够匹配）的情况
                return false;
        }

        return stack.isEmpty();  // 若最后 stack 不为空则也是无效的
    }

    private static boolean match(char a, char b) {
        return a == '(' && b == ')' || a == '[' && b == ']' || a == '{' && b == '}';
    }

    public static void main(String[] args) {
        log(isValid("()"));          // expects true
        log(isValid("()[]{}"));      // expects true
        log(isValid("{[]}"));        // expects true
        log(isValid(""));            // expects true
        log(isValid("(]"));          // expects false
        log(isValid("([)]"));        // expects false
        log(isValid("{[]}()[{})"));  // expects false
        log(isValid("]"));           // expects false
    }
}
