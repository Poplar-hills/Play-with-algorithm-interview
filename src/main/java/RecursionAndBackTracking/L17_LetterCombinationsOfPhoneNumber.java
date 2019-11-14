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
 *
 * - 💎 回溯法总结：
 *   - “回溯”指的是递归结束后返回上一层的行为。
 *   - “回溯法”指的就是通过 递归->返回->递归->返回->…… 这样的方式来搜索解的一种算法思想（DFS 就是回溯法的具体例子）。
 *   - 回溯法的时间效率一般比较低 ∵ 要遍历到所有叶子节点（通常是指数级别，即 O(2^n)）。
 *   - 回溯法是暴力解法的一个主要实现方式，尤其是在不能简单使用循环遍历（不知道要循环几遍）的情况下（例如树形结构中）。
 *   - 回溯法可以通过“剪枝”避免到达所有叶子节点来优化时间效率。
 *   - 动态规划其实就是在回溯法的基础上改进的。
 * */

public class L17_LetterCombinationsOfPhoneNumber {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：该题是一个组合问题，但可以转化为树形问题求解（类似 L494_TargetSum 解法1）。例如对于 digits="23"，根据
     *   digit->letter 的映射可将其表达为三叉树：
     *                        ②
     *               a/      b|       c\
     *             ③         ③         ③
     *         d/ e| f\   d/ e| f\   d/ e| f\
     *        ad  ae  af bd  be  bf cd  ce  cf
     *
     * - 时间复杂度 O(3^n * 4^m)，其中 digits 里能映射为3个字母的数字个数为 n，能映射为4个字母的数字个数为 m。该解的时间复杂
     *   度就相当于所有不同组合的个数，例如 digits="237"，其中"2"、"3"各有3种取值，"7"有4种取值 ∴ 一共有 3*3*4 种组合方式。
     * - 空间复杂度 O(len(digits))。
     * */
    private static final String[] letterMap =     // 用数组实现映射表最方便（前两个空字符串是为了便于随机访问）
        new String[]{"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    public static List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        dfs(digits, 0, "", res);
        return res;
    }

    public static void dfs(String digits, int i, String combo, List<String> res) {
        if (digits.isEmpty()) return;
        String letterStr = letterMap[digits.charAt(i) - '0'];  // 将 char 转换为 int（'5'-'0'的结果为5）

        for (char l : letterStr.toCharArray()) {
            String newCombo = combo + l;          // append 每个字母以生成不同的组合
            if (i == digits.length() - 1)         // 若到达叶子节点则将组合放入结果集中
                res.add(newCombo);
            else                                  // 否则继续递归
                dfs(digits, i + 1, newCombo, res);
        }
    }

    /*
     * 解法2：Iteration (解法1的非递归版)
     * - 思路：纯用循环遍历实现：对于 digits="23" 来说：
     *                       res = [""]
     *            / "a"  ->  ""+"a" -> temp=["a"]
     *        "2" - "b"  ->  ""+"b" -> temp=["a", "b"]
     *            \ "c"  ->  ""+"c" -> temp=["a", "b", "c"]
     *
     *                       res = ["a", "b", "c"]
     *                  /->  "a"+"d" -> temp=["ad"]
     *            / "d" -->  "b"+"d" -> temp=["ad", "bd"]
     *           /      \->  "c"+"d" -> temp=["ad", "bd", "cd"]
     *          /       /->  "a"+"e" -> temp=["ad", "bd", "cd", "ae"]
     *        "3" - "e" -->  "b"+"e" -> temp=["ad", "bd", "cd", "ae", "be"]
     *          \       \->  "c"+"e" -> temp=["ad", "bd", "cd", "ae", "be", "ce"]
     *           \      /->  "a"+"f" -> temp=["ad", "bd", "cd", "ae", "be", "ce", "af"]
     *            \ "f" -->  "b"+"f" -> temp=["ad", "bd", "cd", "ae", "be", "ce", "af", "bf"]
     *                  \->  "c"+"f" -> temp=["ad", "bd", "cd", "ae", "be", "ce", "af", "bf", "cf"]
     * - 时间复杂度 O(3^n * 4^m)，空间复杂度 O(1)。
     * */
    public static List<String> letterCombinations2(String digits) {
        List<String> res = new ArrayList<>();
        if (digits.isEmpty()) return res;
        res.add("");                  // 注意这里要先放入一个 trigger 才能启动后面的逻辑填入数据

        for (char d : digits.toCharArray()) {
            List<String> temp = new ArrayList<>();
            String letterStr = letterMap[d - '0'];

            for (char l : letterStr.toCharArray())
                for (String s : res)  // 将 res 中已有的字符串再拿出来拼接上 l
                    temp.add(s + l);

            res = temp;
        }

        return res;
    }

    /*
     * 解法3：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<String> letterCombinations3(String digits) {
        return null;
    }

    public static void main(String[] args) {
        log(letterCombinations2("23"));  // expects ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"]
        log(letterCombinations2(""));    // expects []
    }
}
