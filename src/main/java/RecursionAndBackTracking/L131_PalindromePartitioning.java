package RecursionAndBackTracking;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Palindrome Partitioning
 *
 * - Given a string s, partition s such that every substring of the partition is a palindrome. Return all
 *   possible palindrome partitioning of s.
 * */

public class L131_PalindromePartitioning {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：该题是一个组合问题 ∴ 可以转化为树形问题求解，具体来说可采用类似 L17、L93 中解法1的回溯法。例如对于 "aabb" 来说：
     *                    ""
     *             /      |      \
     *          "a"      "aa"    "aabb"
     *           |       /  \
     *          "a"    "b"  "bb"
     *          /  \    |
     *        "b" "bb" "b"
     *         |
     *        "b"
     * - 时间复杂度 O(2^n * n^2)：一个长度为 n 的字符串有 n-1 个间隔，而在每个间隔上都有2种选择：切分或不切分 ∴ 该字符串共有
     *   2^(n-1) 种切分方式，即需要 2^(n-1) 次递归 ∴ 是 O(2^n) 量级的复杂度。而每次递归需要复制字符串列表 + 执行 isPalindrome
     *   ，这两个都是 O(n) 操作 ∴ 总复杂度为 O(2^n * n^2)。
     * - 空间复杂度 O(n)。
     * */
    public static List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        dfs(s, 0, new ArrayList<>(), res);
        return res;
    }

    private static void dfs(String s, int i, List<String> list, List<List<String>> res) {
        if (i == s.length()) {
            res.add(list);
            return;
        }
        for (int j = i + 1; j <= s.length(); j++) {
            String str = s.substring(i, j);
            if (isPalindrome(str)) {
                List<String> newPath = new ArrayList<>(list);  // 复制字符串列表
                newPath.add(str);
                dfs(s, j, newPath, res);  // 注意继续往下递归时的起点为 j（∵ 要继续递归的对象是剩下没处理的字符串）
            }
        }
    }

    private static boolean isPalindrome(String str) {
        for (int i = 0; i < str.length() / 2; i++)
            if (str.charAt(i) != str.charAt(str.length() - 1 - i))
                return false;
        return true;
    }

    /*
     * 解法2：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<String>> partition2(String s) {
        List<List<String>> res = new ArrayList<>();
        return res;
    }

    public static void main(String[] args) {
        log(partition("aab"));   // expects [["aa","b"], ["a","a","b"]]
        log(partition("aabb"));  // expects [["a","a","b","b"], ["a","a","bb"], ["aa","b","b"], ["aa","bb"]]
    }
}
