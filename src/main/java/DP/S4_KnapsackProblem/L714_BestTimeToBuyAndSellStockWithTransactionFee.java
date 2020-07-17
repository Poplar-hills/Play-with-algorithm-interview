package DP.S4_KnapsackProblem;

import static Utils.Helpers.*;

/*
 * Best Time to Buy and Sell Stock with Transaction Fee
 *
 * - Your are given an array of integers prices, for which the i-th element is the price of a given stock
 *   on day i; and a non-negative integer fee representing a transaction fee. Design an algorithm to find
 *   the maximum profit.
 *
 * - You may complete as many transactions as you like, but you need to pay the transaction fee for each
 *   transaction. You may not buy more than 1 share of a stock at a time (ie. you must sell the stock share
 *   before you buy again.)
 *
 * - 💎总结：该题融合了 L123_BestTimeToBuyAndSellStockIII、L122_BestTimeToBuyAndSellStockII 两种经典思路，多琢磨！
 * */

public class L714_BestTimeToBuyAndSellStockWithTransactionFee {
    /*
     * 超时且超空间解（Memory & Time Limit Exceeded）：DP
     * - 思路：与 L123_BestTimeToBuyAndSellStockIII 解法1一致。
     * - 时间复杂度 O(kn)，空间复杂度 O(kn)。
     * */
    public static int maxProfit_1(int[] prices, int fee) {
        if (prices == null || prices.length < 2) return 0;

        int n = prices.length;
        int k = n / 2;                   // n 天最多交易 n/2 次
        int[][] dp = new int[k + 1][n];

        for (int t = 1; t < k + 1; t++) {
            int maxProfitAfterBuy = Integer.MIN_VALUE;
            for (int d = 1; d < n; d++) {
                maxProfitAfterBuy = Math.max(maxProfitAfterBuy, -prices[d-1] + dp[t-1][d-1]);
                dp[t][d] = Math.max(dp[t][d-1], prices[d] + maxProfitAfterBuy - fee);  // 卖出时减去手续费
            }
        }

        int maxProfit = 0;
        for (int t = 0; t < k + 1; t++)
            maxProfit = Math.max(maxProfit, dp[t][n-1]);  // 找出在不同的交易次数下，最后一天的最大收益

        return maxProfit;
    }

    /*
     * 超时解：DP
     * - 思路：与👆解法、L122_BestTimeToBuyAndSellStockIII 解法2一致。
     * - 实现：加入滚动数组优化空间复杂度。
     * - 时间复杂度 O(kn)，空间复杂度 O(n)。
     * */
    public static int maxProfit_2(int[] prices, int fee) {
        if (prices == null || prices.length < 2) return 0;

        int n = prices.length;
        int k = n / 2;                   // n 天最多交易 n/2 次
        int[][] dp = new int[2][n];
        int maxProfit = 0;

        for (int t = 1; t < k + 1; t++) {
            int maxProfitAfterBuy = Integer.MIN_VALUE;
            for (int d = 1; d < n; d++) {
                maxProfitAfterBuy = Math.max(maxProfitAfterBuy, -prices[d-1] + dp[(t-1)%2][d-1]);
                dp[t%2][d] = Math.max(dp[t%2][d-1], prices[d] + maxProfitAfterBuy - fee);
                if (d == n - 1)          // 顺便找出最后一天的最大收益
                    maxProfit = Math.max(maxProfit, dp[t%2][d]);
            }
        }

        return maxProfit;
    }

    /*
     * 解法1：DP
     * - 💎思路：👆两种解法都是先求出在不同交易次数（0~k）下，最后一天能获得的最大收益，然后再求出他们之中的最大者。这种
     *   思路虽然可行，但并不是最简的 ∵ 忽略了题中“可以交易任意次数”这一条件 —— 当可以交易任意次数时，最简单的解法是采用
     *   L122_BestTimeToBuyAndSellStockII 解法3的思路 —— 递推在第 i 天尝试买入、卖出的最大收益。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int maxProfit(int[] prices, int fee) {
        if (prices == null || prices.length < 2) return 0;

        int n = prices.length;
        int[] buy = new int[n];
        int[] sell = new int[n];
        buy[0] = -prices[0];

        for (int i = 1; i < n; i++) {
            buy[i] = Math.max(buy[i - 1], -prices[i] + sell[i - 1]);
            sell[i] = Math.max(sell[i - 1], prices[i] + buy[i - 1] - fee);
        }

        return sell[n - 1];
    }

    /*
     * 解法2：DP
     * - 思路：与解法1、L122_BestTimeToBuyAndSellStockII 解法4一致。
     * - 实现：∵ 解法1中，buy[i] 只与 buy[i-1]、sell[i-1] 相关（sell[i] 同理）∴ 只维护两个状态变量即可，无需维护整个
     *   buy、sell 数组，从而降低空间复杂度。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int maxProfit2(int[] prices, int fee) {
        if (prices == null || prices.length < 2) return 0;

        int lastBuy = -prices[0];
        int lastSell = 0;

        for (int price : prices) {
            int currBuy = Math.max(lastBuy, -price + lastSell);
            int currSell = Math.max(lastSell, price + lastBuy - fee);
            lastBuy = currBuy;
            lastSell = currSell;
        }

        return lastSell;
    }

    public static void main(String[] args) {
        log(maxProfit2(new int[]{1, 3, 2, 8, 4, 9}, 2));
        // expects 8. [buy, -, -, sell, buy, sell]. Max profit = ((8-1)-2) + ((9-4)-2) = 8.
    }
}