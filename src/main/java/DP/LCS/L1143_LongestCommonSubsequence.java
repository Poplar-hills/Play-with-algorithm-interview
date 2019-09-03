package DP.LCS;

import static Utils.Helpers.*;

/*
* Longest Common Subsequence (LCS, 最长公共子序列)
*
* - Given two strings s1 and s2, return the length of their longest common subsequence.
*
* - LCS 的应用非常广泛，最经典的就是文本处理，除此之外还用于基因工程中判断两段基因的相似性，LCS 约长则两段基因越相似。
* */

public class L1143_LongestCommonSubsequence {
    /*
    * 超时解：DFS
    * - 分析：与 L376 类似，该问题也有两个维度需要进行动态规划。因此：
    *   - 子问题定义：f(m, n) 表示“s1[0..m] 和 s2[0..n] 两段字符串的 LCS 的长度”。
    *   - 状态转移方程：∵ 每个字符都有两种状态 —— 在/不在 LCS 中：
    *     - 若 s1[m] == s2[n]，即该字符在 LCS 中时：f(m, n) = 1 + f(m-1, n-1)，其中的1就是该字符；
    *     - 若 s1[m] != s2[n]，即该字符不在 LCS 中时：f(m, n) = max(f(m-1, n), f(m, n-1))；即让两个字符串各自
    *       收缩一个字符：s1[0..m-1]、s2[0..n] 与 s1[0..m]、s2[0..n-1]，看这两种情况里哪种情况的的 LCS 更长。
    *                       ABCD|AEBD              - 此时 D == D，LCS 长度+1，同时收缩
    *                           ↓
    *                        ABC|AEB               - 此时 C != B，不在 LCS 中，各自收缩
    *                       ↙       ↘
    *                 AB|AEB        ABC|AE         - 左分支中 B == B，LCS 长度+1，同时收缩
    *                   ↓          ↙       ↘
    *                  A|AE     AB|AE     ABC|A    - 左分支中 A != E，不在 LCS 中，各自收缩
    *                ↙     ↘     ...       ...
    *             -|AE     A|A                     - 左左分支已收缩到底，左右分支中 A == A，LCS 长度+1，同时收缩
    *                       ↓
    *                      -|-                     - 左右分支已收缩到底
    * - 时间复杂度 O(2^(m+n))，空间复杂度 O(m+n)。
    * */
    public static int longestCommonSubsequence(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0)
            return 0;
        return helper(s1, s2, s1.length() - 1, s2.length() - 1);
    }

    private static int helper(String s1, String s2, int m, int n) {
        if (m < 0 || n < 0) return 0;
        return s1.charAt(m) == s2.charAt(n)
            ? 1 + helper(s1, s2, m - 1, n - 1)
            : Math.max(helper(s1, s2, m - 1, n), helper(s1, s2, m, n - 1));
    }

    public static void main(String[] args) {
        log(longestCommonSubsequence("abcd", "aebd"));   // expects 3. "ace"
        log(longestCommonSubsequence("abcde", "ace"));   // expects 3. "ace"
        log(longestCommonSubsequence("abc", "abc"));     // expects 3. "abc"
        log(longestCommonSubsequence("abc", "def"));     // expects 0.
    }
}