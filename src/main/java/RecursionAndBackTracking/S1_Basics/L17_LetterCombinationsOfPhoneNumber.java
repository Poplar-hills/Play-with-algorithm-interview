package RecursionAndBackTracking.S1_Basics;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*
 * Letter Combinations of a Phone Number
 *
 * - Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the
 *   number could represent.
 *
 * - A mapping of digit to letters (just like on the telephone buttons) is given below:
 *     +--------+--------+--------+
 *     | 1-*    | 2-abc  | 3-def  |      - Note that 1 does not map to any letters
 *     +--------+--------+--------+
 *     | 4-ghi  | 5-jkl  | 6-mno  |
 *     +--------+--------+--------+
 *     | 7-pqrs | 8-tuv  | 9-wxyz |
 *     +--------+--------+--------+
 *
 * - 💎 回溯法总结：
 *   - “回溯”指的是递归结束后返回上一层的行为，而“回溯法”指的就是通过“递归->返回->递归->返回->……”这样的方式对树形结构进行“穷举”，
 *     从而遍历所有可能性的一种算法思想：
 *                  1
 *                /   \
 *               2     3      - 当遍历了 1->2->4 之后，为了遍历其他的可能，需要从“4”回到“2”，才能去遍历“5”；
 *              / \   / \     - 当遍历了 1->2->5 之后，再从“5”回到“2”再回到“1”，才能继续搜索树的右半边。
 *             4   5 6   7
 *   - DFS 就是回溯思想的一个具体应用。
 *   - 回溯法的时间效率一般比较低 ∵ 要遍历到所有叶子节点（通常是指数级别，即 O(2^n)）。
 *   - 回溯法是暴力解法的一个主要实现方式，尤其是在不能简单使用循环遍历（不知道要循环几遍）的情况下（例如树形结构中）。
 *   - “剪枝”对于回溯的效率至关重要，通过避免到达所有叶子节点来优化时间复杂度。
 *   - 动态规划其实就是在回溯法的基础上改进的。
 * */

public class L17_LetterCombinationsOfPhoneNumber {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：该题是一个组合问题，但可以转化为树形问题求解（类似 L494_TargetSum 解法1）。例如对于 digits="23"，根据
     *   digit->letter 的映射可将其表达为三叉树，并使用回溯法求解：
     *                        ②
     *               a/      b|       c\
     *             ③         ③         ③
     *         d/ e| f\   d/ e| f\   d/ e| f\
     *        ad  ae  af bd  be  bf cd  ce  cf
     *
     * - 时间复杂度 O(2^n) 级别（具体为 O(3^n * 4^m)），其中 n 为 digits 中能映射为3个字母的数字个数，m 为能映射为4个字母
     *   的数字个数。该解的时间复杂度就相当于所有不同组合的个数，例如 digits="237"，其中"2"、"3"各有3种取值，"7"有4种取值
     *   ∴ 一共有 3*3*4 种组合方式。
     * - 空间复杂度 O(len(digits))。
     * */
    private static final String[] letterMap =
        {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};  // 前两个空字符串是为了便于后面的访问

    public static List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        backtrack(digits, 0, "", res);
        return res;
    }

    public static void backtrack(String digits, int i, String combo, List<String> res) {
        if (i == digits.length()) {
            res.add(combo);
            return;
        }
        String letters = letterMap[digits.charAt(i) - '0'];  // 将 char 转换为 int（'5'-'0'的结果为5）
        for (Character c : letters.toCharArray())
            backtrack(digits, i + 1, combo + c, res);
    }

    /*
     * 解法2：循环
     * - 思路：纯用循环遍历实现：对于 digits="23" 来说：
     *                       res = [""]                 - 将 res 中的每一个元素与"2"对应的每一个字母组合
     *            / "a"  ->  ""+"a" -> temp=["a"]
     *        "2" - "b"  ->  ""+"b" -> temp=["a", "b"]
     *            \ "c"  ->  ""+"c" -> temp=["a", "b", "c"]
     *
     *                       res = ["a", "b", "c"]      - 将 res 中的每一个元素与"3"对应的每一个字母组合
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
        res.add("");                    // 注意这里要先放入一个 trigger 才能启动后面的逻辑填入数据

        for (char d : digits.toCharArray()) {
            List<String> temp = new ArrayList<>();
            String letters = letterMap[d - '0'];

            for (char l : letters.toCharArray())
                for (String s : res)    // 将 res 中已有的字符串再拿出来拼接上 l
                    temp.add(s + l);

            res = temp;
        }

        return res;
    }

    /*
     * 解法3：循环 (解法2的简化版)
     * - 思路：解法2通过一个临时列表 temp 实现了对 res 中的元素进行加工和添加的功能，而这个过程其实可以采用 Queue 来化简：
     *   [""]
     *    -> ["a","b","c"]
     *    -> ["b","c","ad","ae","af"]
     *    -> ["c","ad","ae","af","bd","be","bf"]
     *    -> ["ad","ae","af","bd","be","bf","cd","ce","cf"]
     * - 时间复杂度 O(3^n * 4^m)，空间复杂度 O(3^n * 4^m)。
     * */
    public static List<String> letterCombinations3(String digits) {
        Queue<String> q = new LinkedList<>();
        if (digits.isEmpty()) return new ArrayList<>();
        q.offer("");

        while (q.peek().length() != digits.length()) {  // 若队首元素长度 = digits 长度，说明所有组合都已找到
            String combo = q.poll();                    // 出队下一个代加工的组合
            String letters = letterMap[digits.charAt(combo.length()) - '0'];  // 根据该组合的长度找到加工原料
            for (char l : letters.toCharArray())        // 加工
                q.offer(combo + l);
        }

        return new ArrayList<>(q);
    }

    public static void main(String[] args) {
        log(letterCombinations("23"));  // expects ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"]
        log(letterCombinations(""));    // expects []
    }
}
