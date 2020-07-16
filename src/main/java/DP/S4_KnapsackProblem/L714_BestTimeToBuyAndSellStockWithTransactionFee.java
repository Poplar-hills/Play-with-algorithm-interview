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
                dp[t][d] = Math.max(dp[t][d-1], prices[d] + maxProfitAfterBuy - fee);
            }
        }

        int maxProfit = 0;
        for (int t = 0; t < k + 1; t++)
            maxProfit = Math.max(maxProfit, dp[t][n-1]);

        return maxProfit;
    }

    /*
     * 超时解（Memory & Time Limit Exceeded）：DP
     * - 思路：与 L123_BestTimeToBuyAndSellStockIII 解法1一致。
     * - 时间复杂度 O(kn)，空间复杂度 O(n)。
     * */
    public static int maxProfit(int[] prices, int fee) {
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
                if (d == n - 1)
                    maxProfit = Math.max(maxProfit, dp[t%2][d]);
            }
        }

        return maxProfit;
    }

    public static void main(String[] args) {
        log(maxProfit(new int[]{1, 3, 2, 8, 4, 9}, 2));
        // expects 8. [buy, -, -, sell, buy, sell]. Max profit = ((8-1)-2) + ((9-4)-2) = 8.
    }
}