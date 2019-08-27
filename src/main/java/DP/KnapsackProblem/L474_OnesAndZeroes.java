package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;
import java.util.Comparator;

/*
* Ones and Zeroes
*
* - 给定一个字符串数组，其中每个字符串都是由01组成的，问用 m 个0和 n 个1最多能组成数组中的多少个字符串。
* - Note: each 0 and 1 can be used at most once.
* */

public class L474_OnesAndZeroes {
    /*
    * 错误解：第一直觉是用贪心算法，但贪心算法无法做到全局最优，例如 test case 3。
    * */
    public static int findMaxForm(String[] strs, int m, int n) {
        Arrays.sort(strs, Comparator.comparingInt(String::length));  // 让字符串从短到长排列
        int res = 0;
        for (String str : strs) {
            int i = 0;
            for ( ; i < str.length(); i++) {
                char c = str.charAt(i);
                if ((c == '0' && m == 0) || (c == '1' && n == 0)) break;
                if (c == '0') m--;
                if (c == '1') n--;
            }
            if (i == str.length()) res++;
            if (m == 0 && n == 0) break;
        }
        return res;
    }

    /*
    * 解法1：DP
    * - 思路：该题实际上是一道多维的0/1背包问题 —— 用数组中的字符串（相当于物品）同时填充0、1两个背包。每个字符串都有放/不放两种
    *   选择，因此：
    *   - 子问题定义：f(i, z, o) 表示“用前 i 个字符串填充0容量为 z 和1容量为 o 的背包，最多能填充的物品数量”；
    *   - 状态转移方程：f(i, z, o) = max(f(i-1, z, o), 1 + f(i-1, z-zeros[i], o-ones[i]))。
    * - 时间复杂度 O(l*m*n)，空间复杂度 O(l*m*n)。
    * */
    public static int findMaxForm1(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0) return 0;

        int l = strs.length;
        int[][][] dp = new int[l][m + 1][n + 1];

        for (int i = 0; i < l; i++) {
            int[] cnt = count1(strs[i]);

            for (int z = 0; z <= m; z++) {
                for (int o = 0; o <= n; o++) {
                    if (i == 0) {               // 解决 base case
                        if (z >= cnt[0] && o >= cnt[1])
                            dp[i][z][o] = 1;
                    } else {
                        dp[i][z][o] = dp[i-1][z][o];
                        if (z >= cnt[0] && o >= cnt[1])
                            dp[i][z][o] = Math.max(dp[i][z][o], 1 + dp[i-1][z - cnt[0]][o - cnt[1]]);
                    }
                }
            }
        }

        return dp[l - 1][m][n];
    }

    private static int[] count1(String s) {
        int zeros = 0, ones = 0;
        for (char c : s.toCharArray()) {
            if (c == '0') zeros++;
            if (c == '1') ones++;
        }
        return new int[]{zeros, ones};
    }

    /*
    * 解法2：DP + 二维表（解法1的化简版）
    * - 思路：按照 L_ZeroOneKnapsack 解法4的思路复用内层的二维表，从而不再需要为每个 i 的结算结果进行缓存，因此状态转移方程化
    *   简为：f(z, o) = max(f(z, o), 1 + f(z-zeros[i], o-ones[i]))。
    * - 时间复杂度 O(l*m*n)，空间复杂度 O(m*n)。
    * */
    public static int findMaxForm2(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0) return 0;

        int[][] dp = new int[m + 1][n + 1];

        for (String s : strs) {
            int[] cnt = count2(s);
            for (int z = m; z >= cnt[0]; z--)  // 内层两个循环都是从右向左遍历 & 覆盖
                for (int o = n; o >= cnt[1]; o--)
                    dp[z][o] = Math.max(dp[z][o], 1 + dp[z - cnt[0]][o - cnt[1]]);
        }

        return dp[m][n];
    }

    private static int[] count2(String s) {
        int[] res = new int[2];
        for (int i = 0; i < s.length(); i++)
            res[s.charAt(i) - '0']++;   // 0和1的 ASCII 值之差1
        return res;
     }

    public static void main(String[] args) {
        log(findMaxForm2(new String[]{"10", "0001", "111001", "1", "0"}, 5, 3));  // expects 4. ("10", "0001", "1", "0")
        log(findMaxForm2(new String[]{"10", "0001", "111001", "1", "0"}, 4, 3));  // expects 3. ("10", "1", "0")
        log(findMaxForm2(new String[]{"111", "1000", "1000", "1000"}, 9, 3));     // expects 3. ("1000", "1000", "1000")
        log(findMaxForm2(new String[]{"10", "0", "1"}, 1, 1));                    // expects 2. ("0", "1")
        log(findMaxForm2(new String[]{"0", "00", "1"}, 1, 0));                    // expects 1. ("0")
    }
}