package DP.S4_KnapsackProblem;

import static Utils.Helpers.*;

/*
 * Best Time to Buy and Sell Stock IV
 *
 * - Say you have an array for which the ith element is the price of a given stock on day i. Design an
 *   algorithm to find the maximum profit.
 *
 * - You may complete at most k transactions.
 *
 * - Note you may not engage in multiple transactions at the same time (i.e., you must sell the stock before
 *   you buy again).
 * */

public class L188_BestTimeToBuyAndSellStockIV {
    /*
     * 超空间限制解（Memory Limit Exceeded）：DP
     * - 思路：与 L123_BestTimeToBuyAndSellStockIII 一致。具体解释 SEE：L123 解法1。
     *   maxProfit[t][d] 表示“在第 d 天，最多交易 t 次时所能获得的最大收益”。∵ 任意一天要么卖出，要么不卖，因此：
     *   maxProfit[t][d] = max(第 d 天不卖时所能获得的最大收益, 第 d 天卖出的收益 + 在前 d-1 天内买入且之前最多交易 t-1 次的最大收益)
     *                   = max(maxProfit[t][d-1], prices[d] + for d in [0,d-1]，-prices[d] + maxProfit[t-1][d-1])
     * - 时间复杂度 O(kn)，空间复杂度 O(kn)。
     * */
    public static int maxProfit_1(int k, int[] prices) {
        if (prices == null || prices.length == 0) return 0;

        int n = prices.length;
        int[][] dp = new int[k + 1][n];  // ∵ 最多交易 k 次 ∴ dp 数组的范围应为 [0,k]

        for (int t = 1; t < k + 1; t++) {
            int maxProfitAfterBuy = Integer.MIN_VALUE;
            for (int d = 1; d < n; d++) {
                maxProfitAfterBuy = Math.max(maxProfitAfterBuy, -prices[d-1] + dp[t-1][d-1]);
                dp[t][d] = Math.max(dp[t][d-1], prices[d] + maxProfitAfterBuy);
            }
        }

        return dp[k][n-1];
    }

    /*
     * 超时解：DP（解法1的空间简化版）
     * - 思路：与👆超空间限制解一致。
     * - 实现：从上面解法中可知，dp[t][d] 只与 dp[t][d-1]、dp[t-1][d-1] 相关 ∴ 只要维护大小为2的 dp 数组即可，而不需维护
     *   整个二维表 ∴ 可以使用滚动数组技巧进行化简。
     * - 时间复杂度 O(kn)，空间复杂度 O(n)。
     * */
    public static int maxProfit(int k, int[] prices) {
        if (prices == null || prices.length == 0) return 0;

        int n = prices.length;
        int[][] dp = new int[2][n];

        for (int t = 1; t < k + 1; t++) {
            int maxProfitAfterBuy = Integer.MIN_VALUE;
            for (int d = 1; d < n; d++) {
                maxProfitAfterBuy = Math.max(maxProfitAfterBuy, -prices[d-1] + dp[(t-1) % 2][d-1]);
                dp[t % 2][d] = Math.max(dp[t % 2][d-1], prices[d] + maxProfitAfterBuy);
            }
        }

        return dp[k % 2][n - 1];
    }

    public static void main(String[] args) {
        log(maxProfit(2, new int[]{5, 9, 2, 7, 8, 7, 1, 7}));  // expects 12. [-, -, buy, -, sell, -, buy, sell]
        log(maxProfit(2, new int[]{3, 2, 6, 5, 0, 3}));        // expects 7. [-, buy, sell, -, buy, sell]
        log(maxProfit(2, new int[]{2, 4, 1}));                 // expects 2. [buy, sell, -]
    }
}