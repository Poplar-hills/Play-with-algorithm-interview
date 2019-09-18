package Greedy.GreedyBasics;

import static Utils.Helpers.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* Is Subsequence
*
* - Given a string s and a string t, check if s is subsequence of t.
*
* - Assumptions:
*   1. There is only lower case English letters in both s and t.
*   2. t is potentially a very long (length ~= 500,000) string, and s is a short string (<=100).
*
* - Follow up:
*   If there are lots of incoming S, say S1, S2, ... , Sk where k >= 1B, and you want to check one by one to
*   see if T has its subsequence. In this scenario, how would you change your code?
*
* */

public class L392_IsSubsequence {
    /*
     * 解法1：Greedy（双指针）
     * TODO: 为什么双指针也算是 greedy？？？
     * - 时间复杂度 O(n)，空间复杂度 O(1)，其中 n = len(s)。
     * */
    public static boolean isSubsequence(String s, String t) {
        int i = 0, j = 0;

        while (i < s.length() && j < t.length()) {
            if (s.charAt(i) == t.charAt(j)) i++;
            j++;
        }

        return i == s.length();
    }

    /*
     * 解法2：Greedy
     * - 思路：与解法1一致。
     * - 实现：用 indexOf() 方法代替解法1中的双指针。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。该解法统计性能比解法1高很多，原因是解法1 while 中要调用2次 charAt()，而本解法中的
     *   indexOf() 在 for 中只调用一次（charAt() 和 indexOf() 都是遍历搜索）。
     * */
    public static boolean isSubsequence2(String s, String t) {
        int i = 0;
        for (char size : s.toCharArray()) {
            i = t.indexOf(size, i);     // 注意 indexOf 第2个参数的用法
            if (i == -1) return false;
            i++;
        }

        return true;
    }

    /*
     * 解法3：Recursion
     * - 思路：与解法1一致。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isSubsequence3(String s, String t) {
        if (s.length() == 0) return true;

        for (int i = 0; i < t.length(); i++)
            if (s.charAt(0) == t.charAt(i))
                return isSubsequence3(s.substring(1), t.substring(i + 1));

        return false;  // 若 t 遍历完了还 return 说明 s 中有 t 中没有的字符 ∴ return false
    }

    /*
     * 解法4：DP
     * - 思路：采用 L1143_LongestCommonSubsequence 的方法 —— len(若最长公共子串) = len(s)，则说明 s 是 t 的子串。
     * - 优化：可继续采用 L1143 中解法4、5的方式优化空间复杂度。
     * - 时间复杂度 O(n*m)，空间复杂度 O(n*m)。
     * */
    public static boolean isSubsequence4(String s, String t) {
        int ls = s.length(), lt = t.length();
        int[][] dp = new int[ls + 1][lt + 1];

        for (int i = ls - 1; i >= 0; i--)
            for (int j = lt - 1; j >= 0; j--)
                dp[i][j] = s.charAt(i) == t.charAt(j)
                    ? dp[i + 1][j + 1] + 1
                    : Math.max(dp[i + 1][j], dp[i][j + 1]);

        return dp[0][0] == ls;
    }

    /*
     * 解法5：Binary Search
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static boolean isSubsequence5(String s, String t) {
        Map<Character, List<Integer>> map = new HashMap<>();  // 为 t 生成一个 {字符 -> 索引列表} 的 map
        for (int i = 0; i < t.length(); i++) {
            List<Integer> l = map.getOrDefault(t.charAt(i), new ArrayList<>());
            l.add(i);
            map.put(t.charAt(i), l);
        }

        int cur = -1;
        for (char c : s.toCharArray()) {
            if (!map.containsKey(c)) return false;
            cur = binarySearch(cur, map.get(c));  // 在索引列表中搜索（∵ 列表中的索引是有序的 ∴ 可以使用二分搜索）
            if (cur == -1) return false;
        }

        return true;
    }

    private static int binarySearch(int index, List<Integer> l) {
        int lo = 0, hi = l.size()-1;
        if (l.get(hi) <= index) return -1;
        if (l.get(lo) > index) return l.get(lo);

        while (lo < hi) {
            int mi = (lo+hi) / 2;
            if (l.get(mi) <= index) lo = mi+1;
            else hi = mi;
        }

        return l.get(lo);
    }

    public static void main(String[] args) {
        log(isSubsequence5("abc", "ahbgdc"));  // expects true
        log(isSubsequence5("axc", "ahbgdc"));  // expects false
        log(isSubsequence5("aacc", "axaxyzcc"));  // expects true
        log(isSubsequence5("aacc", "axaxyzc"));   // expects false
    }
}
