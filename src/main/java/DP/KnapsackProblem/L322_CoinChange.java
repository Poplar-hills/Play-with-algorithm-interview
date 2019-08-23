package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Coin Change
*
* - You are given coins of different denominations and a total amount of money amount. Write a function to
*   compute the fewest number of coins that you need to make up that amount. If that amount of money cannot
*   be made up by any combination of the coins, return -1. （注：同一面额的硬币可以使用无数次）
*
* - 分析：这是个典型的完全背包问题，参考 L_CompleteKnapsack。
*   - 子问题定义：f(i, a) 表示“用前 i 个硬币填满容量 a 所需的最少硬币个数”。
*   - 状态转移方程：f(i, a) = min(k + f(i-1, s-coins[i] * k))，其中 0 <= coins[i]*k <= a。
*
* */

public class L322_CoinChange {
    /*
    * 解法1：DP + 一维数组
    * - 思路：与 L_CompleteKnapsack 的解法4相同。
    *        c\a| 0  1  2  3  4  5  6  7  8  9  10  11
    *         2 | 0  M  1  M  2  M  3  M  4  M   5   M  - 只考虑硬币2时，a 的遍历范围是 [2, 11] ∴ a=0,1 处的值不变
    *             ↓  ↓  ↓  ↓  ↓
    *         5 | 0  M  1  M  2  1  3  2  4  3   2   4  - 考虑硬币2,5时，a 的遍历范围是 [1, 11] ∴ a=0~4 处的值不变
    *   - 当 c=5, a=0~4 时 ∵ a < c，说明容量连一个5硬币都装不下 ∴ 直接使用上一行的值；
    *   - 当 c=5, a=6 时 ∵ a > c 但 a-c 处的值为 M，说明容量装得下一个5硬币，但装下之后没法再用2硬币填满剩余容量 ∴ 直接使用上一行的值；
    *   - 当 c=5, a=7 时 ∵ a > c 且 a-c 处的值不为 M，说明容量装得下一个5硬币，且装下之后剩余容量可用2硬币填满 ∴ 从两种方案中取最小的。
    * - 时间复杂度 O(n*a)，空间复杂度 O(a)。
    * */
    public static int coinChange(int[] coins, int amount) {
        int[] cache = new int[amount + 1];
        Arrays.fill(cache, Integer.MAX_VALUE);    // ∵ 要求的是最小值 ∴ 初值设为正最大
        cache[0] = 0;                             // 解决最基本问题

        for (int coin : coins)
            for (int a = coin; a <= amount; a++)  // 从左到右进行遍历和覆盖
                if (cache[a - coin] != Integer.MAX_VALUE)
                    cache[a] = Math.min(cache[a], cache[a - coin] + 1);

        return cache[amount] == Integer.MAX_VALUE ? -1 : cache[amount];
    }

    public static void main(String[] args) {
        log(coinChange(new int[]{1, 2, 5}, 11));      // expects 3. (5 + 5 + 1)
        log(coinChange(new int[]{2, 5}, 11));         // expects 4. (5 + 2 + 2 + 2)
        log(coinChange(new int[]{2}, 3));             // expects -1.
        log(coinChange(new int[]{2, 5, 10, 1}, 27));  // expects 4.
    }
}
