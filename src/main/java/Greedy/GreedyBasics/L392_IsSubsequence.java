package Greedy.GreedyBasics;

import static Utils.Helpers.log;

import java.util.Arrays;

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
     * 解法1：双指针
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
            i = t.indexOf(size, i);
            if (i == -1) return false;
            i++;
        }

        return true;
    }

    /*
     * 解法3：DP
     * - 思路：采用 L1143_LongestCommonSubsequence 的方法 —— len(若最长公共子串) = len(s)，则说明 s 是 t 的子串。
     * - 优化：可继续采用 L1143 解法4、5的方式优化空间复杂度。
     * - 时间复杂度 O(n*m)，空间复杂度 O(n*m)。
     * */
    public static boolean isSubsequence3(String s, String t) {
        int ls = s.length(), lt = t.length();
        int[][] dp = new int[ls + 1][lt + 1];

        for (int i = ls - 1; i >= 0; i--)
            for (int j = lt - 1; j >= 0; j--)
                dp[i][j] = s.charAt(i) == t.charAt(j)
                    ? dp[i + 1][j + 1] + 1
                    : Math.max(dp[i + 1][j], dp[i][j + 1]);

        return dp[0][0] == ls;
    }

    public static void main(String[] args) {
        log(isSubsequence3("abc", "ahbgdc"));  // expects true
        log(isSubsequence3("axc", "ahbgdc"));  // expects false
    }
}
