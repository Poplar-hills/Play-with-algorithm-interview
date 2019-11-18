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
     *   ，这两个都是 O(n) 操作 ∴ 总复杂度为 O(n^2 * 2^n)。
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
        for (int j = i + 1; j <= s.length(); j++) {  // ∵ j 是作为 substring 时的 endIndex ∴ j 最大取值可以为 s.length()
            String str = s.substring(i, j);
            if (isPalindrome(str)) {
                List<String> newPath = new ArrayList<>(list);  // 复制字符串列表
                newPath.add(str);
                dfs(s, j, newPath, res);  // ∵ 要继续递归的对象是剩下没处理的字符串 ∴ 继续往下递归时的起点为 j
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
     * 解法2：Recursion + Backtracking (解法1的性能优化版)
     * - 思路：与解法1一致。
     * - 实现：不在每次分支时复制 list，而只在递归到底找到符合条件的 list 时将其复制进 res 中 ∴ 减少了 list 的复制，从而一定
     *   程度上提升性能。但要注意在返回上一层递归时要去掉最后一个加入 list 的元素，以恢复上一层中 list 的状态。
     * - 时间复杂度 O(n * 2^n)，空间复杂度 O(n)。
     * */
    public static List<List<String>> partition2(String s) {
        List<List<String>> res = new ArrayList<>();
        dfs2(s, 0, new ArrayList<>(), res);
        return res;
    }

    private static void dfs2(String s, int i, List<String> list, List<List<String>> res) {
        if (i == s.length()) {
            res.add(new ArrayList<>(list));    // 递归到底后再将 list 复制进 res 中
            return;
        }
        for (int j = i + 1; j <= s.length(); j++) {
            String str = s.substring(i, j);
            if (isPalindrome(str)) {
                list.add(str);
                dfs2(s, j, list, res);
                list.remove(list.size() - 1);  // 恢复上一层中 list 的状态，以便继续回溯查找
            }
        }
    }

    public static void main(String[] args) {
        log(partition2("aab"));   // expects [["aa","b"], ["a","a","b"]]
        log(partition2("aabb"));  // expects [["a","a","b","b"], ["a","a","bb"], ["aa","b","b"], ["aa","bb"]]
    }
}
