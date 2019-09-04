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
    *   - 子问题定义：f(i, j) 表示“s1[i..] 和 s2[j..] 两段字符串的 LCS 的长度”。
    *   - 状态转移方程：∵ 每个字符都有两种状态 —— 在/不在 LCS 中：
    *     - 若 s1[i] == s2[j]，即该字符在 LCS 中时：f(i, j) = 1 + f(i+1, j+1)，其中的1就是该字符；
    *     - 若 s1[i] != s2[j]，即该字符不在 LCS 中时：f(i, j) = max(f(i+1, j), f(i, j+1))；即让两个字符串窗口各自
    *       向右收缩一个字符，即 s1[i+1..]、s2[j..] 与 s1[i..]、s2[j+1..]，看这两种情况里哪种情况的的 LCS 更长。
    *                       ABCD|AEBD              - 此时 A == A，LCS 长度+1，同时向右收缩
    *                           ↓
    *                        BCD|EBD               - 此时 B != E，不在 LCS 中，各自收缩
    *                       ↙       ↘
    *                 CD|EBD        BCD|BD         - 右分支中 B == B，LCS 长度+1，同时收缩
    *               ↙       ↘          ↓
    *            D|EBD     CD|BD     CD|D          - 右分支中 C != D，不在 LCS 中，各自收缩
    *             ...       ...     ↙    ↘
    *                             D|D    CD|-      - 右右分支已到底；右左分支 D == D，LCS 长度+1，同时收缩
    *                              ↓
    *                             -|-              - 右左分支已到底
    *
    * - 时间复杂度 O(2^(len1+len2))，空间复杂度 O(len1+len2)。
    * */
    public static int longestCommonSubsequence(String s1, String s2) {
        if (s1 == null || s2 == null) return 0;
        return helper(s1, s2, 0, 0);
    }

    private static int helper(String s1, String s2, int i, int j) {
        if (i == s1.length() || j == s2.length()) return 0;
        return s1.charAt(i) == s2.charAt(j)
            ? 1 + helper(s1, s2, i + 1, j + 1)
            : Math.max(helper(s1, s2, i + 1, j), helper(s1, s2, i, j + 1));
    }

    /*
    * 解法1：Recursion + Memoization
    * -
    * */
    public static int longestCommonSubsequence1(String s1, String s2) {
        return 0;
    }

    public static void main(String[] args) {
        log(longestCommonSubsequence("abcd", "aebd"));   // expects 3. "ace"
        log(longestCommonSubsequence("abcde", "ace"));   // expects 3. "ace"
        log(longestCommonSubsequence("abc", "abc"));     // expects 3. "abc"
        log(longestCommonSubsequence("abc", "def"));     // expects 0.
    }
}