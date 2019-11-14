package RecursionAndBackTracking;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Letter Combinations of a Phone Number
 *
 * - Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the
 *   number could represent.
 *
 * - A mapping of digit to letters (just like on the telephone buttons) is given below:
 *     +--------+--------+--------+
 *     | 1-*    | 2-abc  | 3-def  |
 *     +--------+--------+--------+
 *     | 4-ghi  | 5-jkl  | 6-mno  |
 *     +--------+--------+--------+
 *     | 7-pqrs | 8-tuv  | 9-wxyz |
 *     +--------+--------+--------+
 *   Note that 1 does not map to any letters.
 * */

public class L17_LetterCombinationsOfPhoneNumber {
    /*
     * 解法1：Recursion (DFS)
     * - 思路：该题是一个组合问题，但可以转化为树形问题求解，例如 digits="23"，根据 digit->letter 的映射可表达为三叉树：
     *                        ②
     *               a/      b|       c\
     *             ③         ③         ③
     *         d/ e| f\   d/ e| f\   d/ e| f\
     *        ad  ae  af bd  be  bf cd  ce  cf
     *
     * - 时间复杂度 O()，空间复杂度 O()，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    private static final String[] letterMap =
        new String[]{"", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    public static List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        helper(digits, 0, "", res);
        return res;
    }

    public static void helper(String digits, int i, String combo, List<String> res) {
        if (digits.isEmpty()) return;
        String letterStr = letterMap[digits.charAt(i) - '0' - 1];  // -'0' 可以将 char 转换为 int

        for (char l : letterStr.toCharArray()) {
            String combined = combo + l;
            if (i == digits.length() - 1)
                res.add(combined);
            else
                helper(digits, i + 1, combined, res);
        }
    }

    public static void main(String[] args) {
        log(letterCombinations("23"));  // expects ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"]
        log(letterCombinations(""));    // expects []
    }
}
