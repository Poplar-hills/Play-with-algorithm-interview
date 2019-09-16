package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Coin Change
*
* - You are given coins of different denominations and a total amount of money amount. Write a function to
*   compute the fewest number of coins that you need to make up that amount. Return -1 if that amount of money
*   cannot be made up by any combination of the coins. Note you have an infinite number of each kind of coin.
*
* - 初步分析：该题跟完全背包问题类似，但区别在于：
*   1. 求最少所需硬币个数，而非最大价值；
*   2. 选取的各硬币面额之和要刚好等于要求的额度，不能多不能少。
* */

public class L322_CoinChange {
    /*
     * 解法1：DP + 二维数组
     * - 思路：参考 _CompleteKnapsack 解法2。对于 coins[2, 5], amount=11 来说：
     *     coins i\j | 0  1  2  3  4  5  6  7  8  9  10  11
     *       2    0  | 0  M  1  M  2  M  3  M  4  M   5   M  - 最基本问题，只考虑硬币2的情况
     *                 ↓  ↓  ↓  ↓  ↓
     *       5    1  | 0  M  1  M  2  1  3  2  4  3   2   4  - 考虑硬币2、5时，j 从5开始遍历 ∴ 0~4处的值不变
     *   - 当 i=1, j=0~4 时 ∵ j < coins[1]，说明 j 连一个5硬币都装不下 ∴ 直接使用上一行的值；
     *   - 当 i=1, j=6 时 ∵ j > coins[1] 但 dp[a-j]=M，说明 j 装得下一个5硬币，但装下之后没法再用2硬币填满剩余容量 ∴ 直接使用上一行的值；
     *   - 当 i=1, j=7 时 ∵ j > coins[1] 且 dp[a-j]!=M，说明 j 装得下一个5硬币，且装下之后剩余容量可用2硬币填满 ∴ 从两种方案中取最小的。
     *   - 由此可得：
     *     - 子问题定义：f(i, j) 表示“用前 i 个硬币填满容量 j 所需的最少硬币个数”；
     *     - 状态转移方程：f(i, j) = min(f(i-1, j), 1 + f(i-1, j-v[i]))。
     * - 时间复杂度 O(n*amount^2)，空间复杂度 O(n*amount)。
     * */
    public static int coinChange(int[] coins, int amount) {
        if (amount < 1) return 0;

        int n = coins.length;
        int[][] dp = new int[n][amount + 1];

        for (int j = 0; j <= amount; j++)
            dp[0][j] = (j % coins[0] == 0) ? (j / coins[0]) : Integer.MAX_VALUE;

        for (int i = 1; i < n; i++)
            for (int j = 0; j <= amount; j++)
                for (int k = 0; k * coins[i] <= j; k++)
                    if (dp[i - 1][j - coins[i] * k] != Integer.MAX_VALUE)
                        dp[i][j] = Math.min(dp[i - 1][j], k + dp[i - 1][j - coins[i] * k]);

        return dp[n - 1][amount] == Integer.MAX_VALUE ? -1 : dp[n - 1][amount];
    }

    /*
     * 解法2：DP + 一维数组
     * - 思路：参考 _CompleteKnapsack 解法3，状态转移方程化简为 f(j) = min(f(j), 1 + f(j-v[i]))。
     * - 时间复杂度 O(n*amount^2)，空间复杂度 O(amount)。
     * */
    public static int coinChange2(int[] coins, int amount) {
        if (amount < 1) return 0;

        int n = coins.length;
        int[] dp = new int[amount + 1];

        for (int j = 0; j <= amount; j++)
            dp[j] = (j % coins[0] == 0) ? (j / coins[0]) : Integer.MAX_VALUE;

        for (int i = 1; i < n; i++)
            for (int j = amount; j >= coins[i]; j--)  // 从右往左遍历/覆盖
                for (int k = 0; k * coins[i] <= j; k++)
                    if (dp[j - coins[i] * k] != Integer.MAX_VALUE)
                        dp[j] = Math.min(dp[j], k + dp[j - coins[i] * k]);

        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }

    /*
     * 解法3：DP + 一维数组
     * - 思路：参考 _CompleteKnapsack 解法4，去除了内层对 k 的循环，对 j 的循环改为从左向右。
     * - 时间复杂度 O(n*amount)，空间复杂度 O(amount)。
     * */
    public static int coinChange3(int[] coins, int amount) {
        if (amount < 1) return 0;

        int[] dp = new int[amount + 1];

        for (int j = 0; j <= amount; j++)
            dp[j] = (j % coins[0] == 0) ? (j / coins[0]) : Integer.MAX_VALUE;  // 解决最基本问题（∵ 要求的是最小值 ∴ 初值设为正最大）

        for (int i = 1; i < coins.length; i++)        // i 从1开始遍历
            for (int j = coins[i]; j <= amount; j++)  // 从第一个能放入 coins[i] 的容量开始向右进行遍历和覆盖
                if (dp[j - coins[i]] != Integer.MAX_VALUE)
                    dp[j] = Math.min(dp[j], dp[j - coins[i]] + 1);

        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }

    /*
     * 解法4：解法3的简化版
     * - 思路：本解法与解法3的不同之处在于对最基本问题的设定不同。
     * - 时间复杂度 O(n*amount)，空间复杂度 O(amount)。
     * */
    public static int coinChange4(int[] coins, int amount) {
        if (amount < 1) return 0;

        int[] dp = new int[amount + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);  // 全初始化为正最大
        dp[0] = 0;                           // 解决最基本问题只有 dp[0] 而已

        for (int coin : coins)               // i 从0开始遍历、递推
            for (int j = coin; j <= amount; j++)
                if (dp[j - coin] != Integer.MAX_VALUE)
                    dp[j] = Math.min(dp[j], dp[j - coin] + 1);

        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }

    /*
     * 解法5：Recursion + Memoization
     * - 时间复杂度 O(n*amount)，空间复杂度 O(amount)。
     * */
    public static int coinChange5(int[] coins, int amount) {
        if (amount < 1) return 0;
        return minCoinNum(coins, amount, new int[amount + 1]);
    }

    private static int minCoinNum(int[] coins, int j, int[] cache) {
        if (j == 0) return 0;
        if (j < 0) return -1;
        if (cache[j] != 0) return cache[j];

        int min = Integer.MAX_VALUE;
        for (int coin : coins) {
            int res = minCoinNum(coins, j - coin, cache);
            if (res >= 0 && res < min)
                min = res + 1;
        }

        return cache[j] = (min == Integer.MAX_VALUE) ? -1 : min;
    }

    public static void main(String[] args) {
        log(coinChange(new int[]{2, 5}, 11));         // expects 4. (5 + 2 + 2 + 2)
        log(coinChange(new int[]{2, 5, 10, 1}, 27));  // expects 4.
        log(coinChange(new int[]{1, 2, 5}, 11));      // expects 3. (5 + 5 + 1)
        log(coinChange(new int[]{2}, 3));             // expects -1.
    }
}
