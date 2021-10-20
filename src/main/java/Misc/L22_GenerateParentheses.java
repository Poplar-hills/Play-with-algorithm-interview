package Misc;

import java.util.List;

import static Utils.Helpers.log;

/*
 * Generate Parentheses
 *
 * - Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.
 * */

public class L22_GenerateParentheses {
    /*
     * 解法1：
     * -
     * */
    public static List<String> generateParenthesis(int n) {
        return null;
    }

    public static void main(String[] args) {
        log(generateParenthesis(3));  // expects ["((()))","(()())","(())()","()(())","()()()"]
        log(generateParenthesis(1));  // expects ["()"]
    }
}
