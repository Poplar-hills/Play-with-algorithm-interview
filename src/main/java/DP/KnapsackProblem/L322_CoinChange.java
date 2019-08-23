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
*   - 约束/变量：
*     1. 硬币 i
*     2. 金额 s
*
*   - 子问题：f(i, a) 表示“用前 i 个硬币填满面额 s 所需的最少硬币个数”。
*   - 状态转移方程：f(i, a) = min(k + f(i-1, s-coins[i] * k))，其中 0 <= coins[i]*k <= a。
*
* */

public class L322_CoinChange {
    public static int coinChange(int[] coins, int amount) {
        int n = coins.length;
        int[] cache = new int[amount + 1];
        Arrays.fill(cache, Integer.MAX_VALUE);
        cache[0] = 0;

        for (int i = 0; i < n; i++)
            for (int a = coins[i]; a <= amount; a++)
                if (cache[a - coins[i]] != Integer.MAX_VALUE)
                    cache[a] = Math.min(cache[a], cache[a - coins[i]] + 1);

        return cache[amount] == Integer.MAX_VALUE ? -1 : cache[amount];
    }

    public static void main(String[] args) {
        log(coinChange(new int[]{1, 2, 5}, 11));      // expects 3. (5 + 5 + 1)
        log(coinChange(new int[]{2}, 3));             // expects -1.
        log(coinChange(new int[]{2, 5, 10, 1}, 27));  // expects 4.
    }
}
