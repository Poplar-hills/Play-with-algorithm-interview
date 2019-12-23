package DP.S5_LongestSubsequence;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Longest Common Subsequence (LCS, 最长公共子序列)
*
* - Given two strings s1 and s2, return the length of their longest common subsequence.
*
* - LCS 的应用非常广泛，最经典的就是文本处理，除此之外还用于基因工程中两段基因的相似性判断，LCS 越长则说明两段基因越相似。
* */

public class L1143_LongestCommonSubsequence {
    /*
     * 超时解：DFS
     * - 分析：与 L376 类似，该问题也有两个维度需要进行动态规划，因此：
     *   - 子问题定义：f(i, j) 表示“s1[i..] 和 s2[j..] 两段字符串的 LCS 的长度”。
     *   - 状态转移方程：∵ 每个字符都有在/不在 LCS 中这2种状态：
     *     1. 若 s1[i] == s2[j]，即该字符在 LCS 中时：f(i, j) = 1 + f(i+1, j+1)，其中的1就是该字符；
     *     2. 若 s1[i] != s2[j]，即该字符不在 LCS 中时：f(i, j) = max(f(i+1, j), f(i, j+1))；即让两个字符串窗口各自向
     *        右收缩一个字符，即 s1[i+1..]、s2[j..] 与 s1[i..]、s2[j+1..]，看这两种情况里哪种情况的的 LCS 更长。
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
     * - 时间复杂度 O(2^(l1+l2))，空间复杂度 O(l1+l2)。
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
     * 解法1：Recursion + Memoization (DFS + cache)
     * - 时间复杂度 O(l1*l2))，空间复杂度 O(l1*l2)，其中 l1、l2 分别为两字符串的长度。
     * */
    public static int longestCommonSubsequence1(String s1, String s2) {
        if (s1 == null || s2 == null) return 0;

        int[][] cache = new int[s1.length()][s2.length()];
        for (int[] row : cache)
            Arrays.fill(row, -1);

        return helper1(s1, s2, 0, 0, cache);
    }

    private static int helper1(String s1, String s2, int i, int j, int[][] cache) {
        if (i == s1.length() || j == s2.length()) return 0;
        if (cache[i][j] != -1) return cache[i][j];

        return cache[i][j] = s1.charAt(i) == s2.charAt(j)
            ? 1 + helper1(s1, s2, i + 1, j + 1, cache)
            : Math.max(helper1(s1, s2, i + 1, j, cache), helper1(s1, s2, i, j + 1, cache));
    }

    /*
     * 解法2：DP
     * - 思路：子问题定义和状态转移方程同超时解。
     * - 实现：∵ 状态转移方程中前一状态下的解取决于后一状态（f(i, j) = 1 + f(i+1, j+1)）∴ 由后往前递推更直观。
     * - 时间复杂度 O(l1*l2))，空间复杂度 O(l1*l2)。
     * */
    public static int longestCommonSubsequence2(String s1, String s2) {
        if (s1 == null || s2 == null) return 0;

        int l1 = s1.length(), l2 = s2.length();
        int[][] dp = new int[l1 + 1][l2 + 1];  // ∵ 递推都是用后一个的值推前一个的值 ∴ 这里要多开辟1的空间用于递推最后一个元素

        for (int i = l1 - 1; i >= 0; i--)      // 从 s1、s2 的最后一个元素开始往前递推
            for (int j = l2 - 1; j >= 0; j--)
                dp[i][j] = s1.charAt(i) == s2.charAt(j)  // 由 dp[i+1][j+1] 等推出 dp[i][j]
                    ? 1 + dp[i + 1][j + 1]
                    : Math.max(dp[i + 1][j], dp[i][j + 1]);

        return dp[0][0];  // 最后取左上角的值
    }

    /*
     * 解法3：DP
     * - 实现：由前往后递推（不如解法2直观）。
     * - 时间复杂度 O(l1*l2))，空间复杂度 O(l1*l2)。
     * */
    public static int longestCommonSubsequence3(String s1, String s2) {
        if (s1 == null || s2 == null) return 0;

        int l1 = s1.length(), l2 = s2.length();
        int[][] dp = new int[l1 + 1][l2 + 1];

        for (int i = 0; i < l1; i++)      // 从 s1、s2 的第一个元素开始往后递推
            for (int j = 0; j < l2; j++)
                dp[i + 1][j + 1] = s1.charAt(i) == s2.charAt(j)  // 由 dp[i][j] 等推出 dp[i+1][j+1]
                    ? 1 + dp[i][j]
                    : Math.max(dp[i + 1][j], dp[i][j + 1]);

        return dp[l1][l2];  // 最后取右下角的值
    }

    /*
    * 解法4：DP + 滚动数组（解法2的空间优化版）
    * - 思路：类似 _ZeroOneKnapsack 解法3的思路 —— ∵ 解法2中的每个 f(i, j) 都是由 f(i+1, j+1)、f(i, j+1)、f(i+1, j)
    *   递推出来的，即上一行的值是基于下一行中的值计算出来的 ∴ dp 数组只需两行，在从下到上逐行计算时，交替使用这两行即可。
    * - 时间复杂度 O(l1*l2)，空间复杂度 O(l2)。
    * */
    public static int longestCommonSubsequence4(String s1, String s2) {
        if (s1 == null || s2 == null) return 0;

        int l1 = s1.length(), l2 = s2.length();
        int[][] dp = new int[2][l2 + 1];

        for (int i = l1 - 1; i >= 0; i--)
            for (int j = l2 - 1; j >= 0; j--)
                dp[i % 2][j] = s1.charAt(i) == s2.charAt(j)  // 若 i 为偶则写 dp[0]，读 dp[1]；若 i 为奇则写 dp[1]，读 dp[0]
                    ? 1 + dp[(i + 1) % 2][j + 1]
                    : Math.max(dp[(i + 1) % 2][j], dp[i % 2][j + 1]);

        return dp[0][0];  // ∵ 从后往前递推 l1、l2 的最后一个值都是0 ∴ 最终返回 dp[0][0]
    }

    /*
    * 解法5：DP + 滚动数组（另一种写法，不如解法3好理解，但是速度最快）
    * - 时间复杂度 O(l1*l2)，空间复杂度 O(l2)。
    * */
    public static int longestCommonSubsequence5(String s1, String s2) {
        if (s1 == null || s2 == null) return 0;

        int l1 = s1.length(), l2 = s2.length();
        int[] row1 = new int[l2 + 1];  // 两个 row 的大小都为 l2 + 1
        int[] row2 = new int[l2 + 1];

        for (int i = l1 - 1; i >= 0; i--) {
            for (int j = l2 - 1; j >= 0; j--) {
                row1[j] = s1.charAt(i) == s2.charAt(j)
                    ? 1 + row2[j + 1]                   // row2[j+1] 相当于解法2中的 dp[i+1][j+1]
                    : Math.max(row1[j + 1], row2[j]);   // row2[j] 相当于解法2中的 dp[i+1][j]
            }
            int[] temp = row1;  // 交换两行，让数组滚动起来
            row1 = row2;
            row2 = temp;
        }

        return row2[0];         // 最后返回的是 row2 的首元素
    }

    public static void main(String[] args) {
        log(longestCommonSubsequence5("abcd", "aebd"));  // expects 3. "ace"
        log(longestCommonSubsequence5("abcde", "ace"));  // expects 3. "ace"
        log(longestCommonSubsequence5("abc", "abc"));    // expects 3. "abc"
        log(longestCommonSubsequence5("bl", "yby"));     // expects 1. "b"
        log(longestCommonSubsequence5("abc", "def"));    // expects 0.
    }
}