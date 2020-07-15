package DP.S4_KnapsackProblem;

import static Utils.Helpers.*;

/*
 * Best Time to Buy and Sell Stock II
 *
 * - Say you have an array for which the ith element is the price of a given stock on day i. Design an
 *   algorithm to find the maximum profit.
 *
 * - You may complete at most 2 transactions.
 *
 * - Note you may not engage in multiple transactions at the same time (i.e., you must sell the stock before
 *   you buy again).
 * */

public class L123_BestTimeToBuyAndSellStockIII {
    /*
     * 解法1：DP
     * - 思路：先将数据可视化出来，例如 prices=[3,5,0,3,2,5,2,5]：
     *      |
     *    5 |       *                       *           *
     *    4 |    •    •                   •    •       •
     *    3 | *        •        *       •        •   •
     *    2 |           •     •       *            *
     *    1 |            •  •
     *    0 + - - - - - - * - - - - - - - - - - - - - - - -
     *        0     1     2     3     4     5     6     7
     *   在 L122 中 ∵ 可以交易任意多次 ∴ 只要将所有单次上涨的幅度相加即是最大收益。而该题中 ∵ 要在最多交易2次的情况下找到
     *   最大收益 ∴ 就需要考虑跨越 valley 的上涨的情况 —— 是2个单次上涨幅度相加更大，还是一个跨越 valley 的上涨幅度（如
     *   [2,5]）再加另一个上涨幅度更大 —— 这就无法像 L122 解法1中那样仅使用遍历就能求解，而是需要使用 DP 才行。
     *
     *   ∵ 存在股价和交易次数2个变量 ∴ 在使用 DP 时需要同时考虑这两个变量 ∴ 需要使用二维 dp 数组：maxProfit[t][d] 表示
     *   “在第 d 天，最多交易 t 次时所能获得的最大的收益”。由此列出二维表：
     *       t\p [ 3,  5,  0,  3,  2,  5,  2,  5 ]
     *        0 |  0   0   0   0   0   0   0   0    - 当最多交易0次时，不管股价如何，最大收益都只能为0
     *        1 |  0   2   2   3   3   5   5   5
     *        2 |  0   2   2   5   5   7   7   8
     *
     *   t=0、1 时非常容易看出结果；而当 t=2 时（尤其从 p=3 往后可以进行2次交易时）需要考虑更多可能性。此时可以考虑 ——
     *   在任意一天都有2种选择，要么卖出，要么不卖：
     *     - 若在第 d 天不卖，最多交易 t 次时所能获得的最大收益
     *           = 前一天最多交易 t 次所能获得的最大收益
     *           = maxProfit[t][d-1]
     *     - 若在第 d 天卖出，最多交易 t 次时所能获得的最大收益
     *           = 第 d 天的股价 + 在前 d-1 天内买入且之前最多交易 t-1 次所能获得的最大收益
     *           = 第 d 天的股价 + 比较并选出 [0,d-1] 区间内每天买入且之前最多交易 t-1 次所获最大收益中的最大的那个
     *           = prices[d] + for d in [0,d-1]，findMax(-prices[d] + maxProfit[t-1][d-1])
     *
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int n = prices.length;
        int[][] dp = new int[3][n];  // dp[t][d] 表示“在第 d 天，最多交易 t 次时所能获得的最大的收益”

        for (int t = 1; t < 3; t++) {           // 固定交易次数（t=0 时最大收益都是0 ∴ 可跳过）
            int maxProfitAfterBuy = Integer.MIN_VALUE;  // 在前 d-1 天内买入且之前最多交易 t-1 次所能获得的最大收益
            for (int d = 1; d < n; d++) {
                maxProfitAfterBuy = Math.max(maxProfitAfterBuy, -prices[d-1] + dp[t-1][d-1]);  // 注意是 -prices[d-1]
                dp[t][d] = Math.max(dp[t][d-1], prices[d] + maxProfitAfterBuy);
            }
        }

        return dp[2][n - 1];
    }

    public static void main(String[] args) {
        log(maxProfit(new int[]{3, 5, 0, 3, 2, 5, 2, 5}));  // expects 8. [-, -, buy, -, -, sell, buy, sell]
        log(maxProfit(new int[]{0, 1, 2, 3, 4, 4, 3, 2}));  // expects 4. [buy, -, -, -, sell, -, -, -]
        log(maxProfit(new int[]{7, 6, 4, 3, 1}));           // expects 0. no transaction.
    }
}