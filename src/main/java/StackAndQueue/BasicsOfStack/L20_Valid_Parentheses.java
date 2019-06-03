package StackAndQueue.BasicsOfStack;

import java.util.ArrayDeque;
import java.util.Deque;

import static Utils.Helpers.*;

/*
* Valid Parentheses
*
* - Given a string containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
* */

public class L20_Valid_Parentheses {
    public static boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (isOpenBracket(c))
                stack.push(c);
            else if (stack.isEmpty() || !match(stack.pop(), c))
                return false;
        }
        return stack.isEmpty();
    }

    private static boolean isOpenBracket(char c) {
        return c == '(' || c == '[' || c == '{';
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
