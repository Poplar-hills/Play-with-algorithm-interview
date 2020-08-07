package DP.S4_KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
 * DP Basics: Complete Knapsack Problem
 *
 * - 在0/1背包问题基础上改变一点 —— 每种物品不再又有一个，而是可以放入任意多个。
 *
 * - 分析：
 *   - 思路1：Greedy Algorithm：
 *     采用贪心算法看上去是可行的：∵ 每种物品有无限个 ∴ 先不断放入性价比最高的物品，直到背包容量不足以再放时尝试放入性价比第二高的
 *     物品，若仍旧无法填满，则再用性价比第三高的物品来填充…… 这样看上去可行，但仍然存在反例：
 *                           Item       0     1
 *                           Weight     5     7
 *                           Value      5     8
 *                           v / w      1    1.14
 *     若背包容量为10，∵ 物品1的性价比 > 物品0 ∴ 先放物品1，之后的剩余空间已放不下任何一种物品了，所以背包里的价值就是8。而实际上
 *     放入两个物品0即可得到价值10，因此贪心算法并不适用。
 *
 *   - 思路2：DP：
 *     DP 的关键是寻找原问题的子问题，并写出状态转移方程：
 *     - 子问题与0/1背包一样，仍然是：f(i, j) 表示“用前 i 个物品填充剩余容量为 j 的背包所能得到的最大价值”。
 *     - 状态转移方程：∵ 每件物品可以放入无数个 ∴ 对每件物品来说，策略已经不再是放或不放的问题了，而是放入多少件的问题。设能放入的
 *       物品 i 的件数为 k，则约束条件为：0 <= w[i]*k <= j，即在剩余容量 j 内可以放入0, 1, 2, ... k 件物品 i。因此状态转移
 *       方程为：f(i, j) = max(v[i]*k + f(i-1, j-w[i]*k))，其中 0 <= w[i]*k <= j。
 * */

public class B2_CompleteKnapsack {
    /*
     * 解法1：Recursion + Memoization
     * - 思路：top-down 方式。
     * - 对比：在0/1背包中，对于第 i 个物品只有放/不放2种情况，只需从这2种选择中选出最优的即可；而完全背包问题则需在 k 种选择中选
     *   出最优解，这就需要多一层循环来求最大值。
     * - 时间复杂度 O(n*c^2)，空间复杂度 O(n*c)。
     * */
    public static int knapsack(int[] w, int[] v, int c) {
        int n = w.length;
        int[][] cache = new int[n][c + 1];
        for (int[] row : cache)
            Arrays.fill(row, -1);
        return helper(n - 1, c, w, v, cache);  // 从 n-1 开始递归
    }

    private static int helper(int i, int j, int[] w, int[] v, int[][] cache) {
        if (i < 0 || j == 0) return 0;
        if (cache[i][j] != -1) return cache[i][j];

        int res = helper(i - 1, j, w, v, cache);
        if (j >= w[i])
            for (int k = 1; w[i] * k <= j; k++)  // 从放入0, 1, 2, ... k 件物品 i 中选出价值最大的方案（这里也是与0/1背包问题的唯一区别）
                res = Math.max(res, v[i] * k + helper(i - 1, j - w[i] * k, w, v, cache));

        return cache[i][j] = res;
    }

    /*
     * 解法2：DP + 二维数组
     * - 思路：类似 _ZeroOneKnapsack 的解法2。
     * - 时间复杂度 O(n*c^2)，空间复杂度 O(n*c)。
     * */
    public static int knapsack2(int[] w, int[] v, int c) {
        int n = w.length;
        if (n <= 0) return 0;

        int[][] dp = new int[n][c + 1];

        for (int j = 0; j <= c; j++)
            dp[0][j] = (j / w[0]) * v[0];  // 解决最基本问题（填充第一行）

        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= c; j++) {
                dp[i][j] = dp[i - 1][j];
                for (int k = 0; w[i] * k <= j; k++)
                    dp[i][j] = Math.max(dp[i][j], v[i] * k + dp[i - 1][j - w[i] * k]);
            }
        }

        return dp[n - 1][c];
    }

    /*
     * 解法3：DP + 一维数组（解法2的空间优化版）
     * - 思路：类似 _ZeroOneKnapsack 中的解法4，状态转移方程简化为：f(i, j) = max(f(j), v[i]*k + f(j - w[j]*k))。
     * - 时间复杂度 O(n*c^2)，空间复杂度 O(c)。
     * */
    public static int knapsack3(int[] w, int[] v, int c) {
        int n = w.length;
        if (n <= 0) return 0;

        int[] dp = new int[c + 1];

        for (int j = 0; j <= c; j++)
            dp[j] = (j / w[0]) * v[0];

        for (int i = 1; i < n; i++)
            for (int j = c; j >= w[i]; j--)
                for (int k = 0; w[i] * k <= j; k++)
                    dp[j] = Math.max(dp[j], v[i] * k + dp[j - w[i] * k]);

        return dp[c];
    }

    /*
     * 解法4：解法3的优化版
     * - 思路：前3种解法相比 _ZeroOneKnapsack 中的解法来说多了一层对 k 的循环，用于确定“同样的物品应放几个最优”，但也因此提高
     *   了时间复杂度（n*c*c）。优化方法是将“遍历 k 逐一尝试”的思路改为“基于前面的结果进行递推”的思路。
     * - 实现：将对 j 的循环改为从左往右覆盖，即从第一个放得下物品 i 的容量 w[i] 开始，比较再放入/不再放入一个物品 i 的价值。例如：
     *        w  v | i\c  0  1  2  3  4  5  6  7  8  9  10  11  12  13  14
     *        5  5 |  0   0  0  0  0  0  5  5  5  5  5  10  10  10  10  10
     *        7  8 |  1   0  0  0  0  0  5  5  8  8  8  10  10  13  13  16
     *   当 i=1 时，j 从7开始向右遍历：
     *     - 当 j=7 时 ∵ 7处刚好能容纳一个物品1 ∴ 比较放入/不放一个物品1的价值谁更大（v[1] 与 dp[7]），谁就是 dp[7] 的最新取值。
     *     - 当 j=8 时 ∵ 8处已经能容纳一个物品1 ∴ 比较放入/不放一个物品1的价值谁更大（v[1] + dp[1] 与 dp[8]），谁就是 dp[8] 的最新取值。
     *     - 当 j=14 时 ∵ 14处已经能容纳两个物品1 ∴ 比较再放入/不再放一个物品1的价值谁更大（v[1] + dp[7] 与 dp[14]），谁就是 dp[14] 的最新取值。
     *   状态转移方程：f(i, j) = max(f(j), v[i] + f(j - w[j]))。
     * - 时间复杂度 O(n*c)，空间复杂度 O(c)。
     * */
    public static int knapsack4(int[] w, int[] v, int c) {
        int n = w.length;
        if (n <= 0) return 0;

        int[] dp = new int[c + 1];

        for (int i = 0; i < n; i++)
            for (int j = w[i]; j <= c; j++)  // 从左到右 [w[i], c] 进行覆盖（这里是与 _ZeroOneKnapsack 解法4的最大区别）
                dp[j] = Math.max(dp[j], v[i] + dp[j - w[i]]);

        return dp[c];
    }

    public static void main(String[] args) {
        log(knapsack(         // expects 10. (5 + 5)
            new int[]{5, 7},  // weight
            new int[]{5, 8},  // value
            10));             // capacity

        log(knapsack(         // expects 16. (8 + 8)
            new int[]{5, 7},
            new int[]{5, 8},
            14));
    }
}
