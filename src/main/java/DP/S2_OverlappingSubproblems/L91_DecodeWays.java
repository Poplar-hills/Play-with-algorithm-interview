package DP.S2_OverlappingSubproblems;

import static Utils.Helpers.log;

import java.util.Arrays;

/*
 * Decode Ways
 *
 * - A message containing letters from A-Z is being encoded to numbers using the following mapping:
 *      'A' -> 1
 *      'B' -> 2
 *         ...
 *      'Z' -> 26
 *   Given a non-empty string containing only digits, determine the total number of ways to decode it.
 * */

public class L91_DecodeWays {
    /*
     * 超时解：DFS
     * - 思路：该题目与 L70_ClimbingStairs 类似，都是在做某事时每次有2种选择，求共有多少种不同的选择组合能最终做成该事。
     *   对本题来说：在解码字符串时，每次都有2种选择：解码1个数字 or 解码2个数字，且该问题符合最优子结构性质：
     *   f("213") = f("13") + f("3") = 2 + 1 = 3，因此有：
     *     - 子问题定义：f(i) 表示“从索引 i 开始到结尾之间的字符串的解码方式个数”；
     *     - 递推表达式：f(i) = f(i + 1) + f(i + 2)，其中 i ∈ [0, len-3]，且：
     *       1. 递归的终止条件：f("") = 1；
     *       2. 以0开头的字符串无法解码：f("0...") = 0。
     *   则整个解码过程就可以用递归的方式进行：
     *               f("102213")                            5
     *                ↙       ↘                           ↗   ↖
     *          f("02213")   f("2213")                  0       5
     *                       ↙       ↘                        ↗   ↖
     *                f("213")       f("13")                3       2
     *                 ↙    ↘         ↙    ↘              ↗   ↖   ↗   ↖
     *           f("13")   f("3")  f("3")  f("")         2    1   1    1
     *           ↙    ↘       ↓       ↓                ↗  ↖   ↑   ↑
     *       f("3")  f("")  f("")   f("")             1    1  1   1
     *         ↓                                      ↑
     *       f("")                                    1
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
     * */
    public static int numDecodings(String s) {
        if (s == null || s.length() == 0) return 0;
        return helper(s, 0);
    }

    private static int helper(String s, int i) {  // 索引 i 指向本次递归中最后一个要解码的字符
        if (i == s.length()) return 1;            // f("") 的情况
        if (s.charAt(i) == '0') return 0;         // f("0...") 的情况

        int res = helper(s, i + 1);
        if (i + 2 <= s.length() && Integer.parseInt(s.substring(i, i + 2)) <= 26)
            res += helper(s, i + 2);

        return res;
    }

    /*
     * 解法1：DFS + Recursion + Memoization
     * - 思路：在超时解的基础上加入 Memoization 优化。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int numDecodings1(String s) {
        if (s == null || s.length() == 0) return 0;
        int[] cache = new int[s.length()];
        Arrays.fill(cache, -1);             // ∵ 计算结果可能为0，所以要初始化为-1
        return dfs(s, 0, cache);
    }

    public static int dfs(String s, int i, int[] cache) {
        if (i == s.length()) return 1;
        if (s.charAt(i) == '0') return 0;
        if (cache[i] != -1) return cache[i];

        int res = dfs(s, i + 1, cache);
        if (i + 1 < s.length() && Integer.parseInt(s.substring(i, i + 2)) < 27)
            res += dfs(s, i + 2, cache);

        return cache[i] = res;
    }

    /*
     * 解法2：DP
     * - 思路：将解法1直接转换为 DP 的写法（其实本质思路与解法1是一样的 —— 都是自上而下分解任务），子问题定义和递推表达式不变：
     *   - f(i) 表示“从索引 i 开始的字符串的解码方式个数”；
     *   - f(i) = f(i + 1) + f(i + 2)，其中 i ∈ [0, len-3]，且：
     *     1. 递推的起始条件：f("") = 1；
     *     2. 以0开头的字符串无法解码：f("0...") = 0。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int numDecodings2(String s) {
        if (s == null || s.length() == 0) return 0;

        int n = s.length();
        int[] dp = new int[n + 1];     // 多开辟一位存放 f("") 的解
        dp[n] = 1;                     // f("") = 1

        for (int i = n - 1; i >= 0; i--) {
            if (s.charAt(i) == '0') {  // f("0...") = 0
                dp[i] = 0;
                continue;
            }
            dp[i] = dp[i + 1];
            if (i + 2 <= n && Integer.parseInt(s.substring(i, i + 2)) <= 26)
                dp[i] += dp[i + 2];
        }

        return dp[0];
    }

    /*
     * 解法3：DP
     * - 思路：
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int numDecodings3(String s) {
        if (s == null || s.length() == 0) return 0;
        int n = s.length();

        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = s.charAt(0) == '0' ? 0 : 1;

        for (int i = 2; i <= n; i++) {
            if (s.charAt(i - 1) != '0')
                dp[i] = dp[i - 1];

            int num = Integer.parseInt(s.substring(i - 2, i));
            if (num >= 10 && num <= 26)
                dp[i] += dp[i - 2];
        }

        return dp[n];
    }

    public static void main(String[] args) {
        log(numDecodings3("27"));     // expects 1. 2,7  -> "BG"
        log(numDecodings3("12"));     // expects 2. 1,2  -> "AB" or 12 -> "L"
        log(numDecodings3("227"));    // expects 2. 22,7 -> "VG" or 2,2,7 -> "BBG"
        log(numDecodings3("226"));    // expects 3. 2,26 -> "BZ" or 22,6 -> "VF" or 2,2,6 -> "BBF"
        log(numDecodings3("102213")); // expects 5. ...
    }
}
