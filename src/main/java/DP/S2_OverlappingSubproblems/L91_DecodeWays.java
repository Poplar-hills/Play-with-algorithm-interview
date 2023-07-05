package DP.S2_OverlappingSubproblems;

import static Utils.Helpers.log;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Decode Ways
 *
 * - A message containing letters from A-Z is being encoded into numbers using the following mapping:
 *      'A' -> 1
 *      'B' -> 2
 *         ...
 *      'Z' -> 26
 *   Given a non-empty string containing only digits, determine the total number of ways to decode it.
 * */

public class L91_DecodeWays {
    /*
     * 超时解：DFS + Recursion
     * - 思路：该题目与 L70_ClimbingStairs 类似，都是在做某事时每次有2种选择，求共有多少种不同的选择组合能最终做成该事。
     *   对本题来说：在解码字符串时，每次都有2种选择：解码1个数字 or 解码2个数字，且该问题符合最优子结构性质：
     *   f("213") = f("13") + f("3") = 2 + 1 = 3，因此有：
     *     - 子问题定义：f(i) 表示“从字符串 s 的第 i 个字符到最后一个字符之间的解码方式数”；
     *     - 递推表达式：f(i) = f(i+1) + f(i+2)，其中 i ∈ [0,n-2)，且：
     *       1. 递归的终止条件：f("") = 1；
     *       2. 以0开头的字符串无法解码：f("0...") = 0。
     *   则整个解码过程就可以用递归的方式进行：
     *               f("102213")                            5
     *               1↙       10↘                         ↗   ↖
     *          f("02213")   f("2213")                  0       5
     *                      2↙       22↘                      ↗   ↖
     *                f("213")       f("13")                3       2
     *                2↙   21↘       1↙   13↘             ↗   ↖   ↗   ↖
     *           f("13")   f("3")  f("3")  f("")         2    1   1    1
     *          1↙   13↘     3↓      3↓                ↗  ↖   ↑   ↑
     *       f("3")  f("")  f("")   f("")             1    1  1   1
     *        3↓                                      ↑
     *       f("")                                    1
     * - 💎 经验：DFS、DP 类的题目要先写出递推表达式，非常有助于代码实现，这不不要偷懒。
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
     * */
    public static int numDecodings_1(String s) {
        if (s == null || s.length() == 0) return 0;
        return dfs_1(s, 0);
    }

    private static int dfs_1(String s, int i) {  // 索引 i 指向本次递归中最后一个要解码的字符
        if (i == s.length()) return 1;           // f("") 的情况
        if (s.charAt(i) == '0') return 0;        // f("0...") 的情况

        int count = dfs_1(s, i + 1);
        if (i + 2 <= s.length() && Integer.parseInt(s.substring(i, i + 2)) <= 26)
            count += dfs_1(s, i + 2);

        return count;
    }

    /*
     * 解法1：DFS + Recursion + Memoization
     * - 思路：在👆🏻超时解中，可见 f("13") 被计算了2次 ∴ 可以在超时解的基础上加入 Memoization 优化。
     * - 实现：那么应该基于什么 key 做 Memoization 呢？∵ 我们在递归时 i 是变化的（即 i 是递归表达式中的变量），
     *   ∴ 可基于 i 来建立 Memoization cache。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int numDecodings(String s) {
        if (s == null || s.length() == 0) return 0;
        int[] cache = new int[s.length()];
        Arrays.fill(cache, -1);  // ∵ 计算结果可能为0，所以要初始化为-1
        return dfs(s, 0, cache);
    }

    public static int dfs(String s, int i, int[] cache) {
        if (i == s.length()) return 1;     // f("") 的情况
        if (s.charAt(i) == '0') return 0;  // f("0...") 的情况
        if (cache[i] != -1) return cache[i];

        int count = dfs(s, i + 1, cache);
        if (i + 1 < s.length() && Integer.parseInt(s.substring(i, i + 2)) <= 26)
            count += dfs(s, i + 2, cache);

        return cache[i] = count;
    }

    /*
     * 解法2：DP
     * - 思路：将解法1直接转换为 DP 的写法（其实本质思路与解法1是一样的 —— 都是自上而下分解任务），子问题定义和递推表达式不变：
     *   - f(i) 表示“从字符串 s 的第 i 个字符到最后一个字符之间的解码方式数”；
     *   - f(i) = f(i + 1) + f(i + 2)，其中 i ∈ [0,n-2)，且：
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
     * - 思路：不同于解法2，该解法采用自下而上的 DP 思路，先解决基本问题，再递推出高层次问题的解。
     *   - 子问题定义：f(i) 表示“从字符串 s 的第 0 个字符到第 i 个字符之间的解码方式数”；
     *   - 递推表达式：f(i) = f(i-1) + f(i-2)，其中 i ∈ [2,n)。
     * - 👉 总结：对比来说，解法2更加直观易懂。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int numDecodings3(String s) {
        if (s == null || s.length() == 0) return 0;
        int n = s.length();

        int[] dp = new int[n + 1];           // 多开辟一位空间（dp[0]）用于存储初值
        dp[0] = 1;                           // 初值
        dp[1] = s.charAt(0) == '0' ? 0 : 1;  // 从索引1开始，dp[i] 上存储字符 s[i-1] 上的解 f(i-1)

        for (int i = 2; i <= n; i++) {
            if (s.charAt(i - 1) != '0')      // dp[i] 上存储的是字符 s[i-1] 上的解 ∴ 这里 i 要-1
                dp[i] = dp[i - 1];

            int num = Integer.parseInt(s.substring(i - 2, i));  // 将 s[i-2, i-1] 这两个字符转为 int
            if (num >= 10 && num <= 26) 
                dp[i] += dp[i - 2];
        }

        return dp[n];
    }

    public static void main(String[] args) {
        log(numDecodings3("27"));     // expects 1. 2,7 -> "BG"
        log(numDecodings3("12"));     // expects 2. 1,2 -> "AB" or 12 -> "L"
        log(numDecodings3("227"));    // expects 2. 22,7 -> "VG" or 2,2,7 -> "BBG"
        log(numDecodings3("226"));    // expects 3. 2,26 -> "BZ" or 22,6 -> "VF" or 2,2,6 -> "BBF"
        log(numDecodings3("102213")); // expects 5. ...
    }
}
