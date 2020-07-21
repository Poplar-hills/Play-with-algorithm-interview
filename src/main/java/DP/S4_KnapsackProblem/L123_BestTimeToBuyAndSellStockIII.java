package DP.S4_KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
 * Best Time to Buy and Sell Stock III
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
     * - 时间复杂度 O(kn)，空间复杂度 O(kn)。
     * */
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int k = 2;
        int n = prices.length;
        int[][] dp = new int[k+1][n];  // dp[t][d] 表示“在第 d 天，最多交易 t 次时所能获得的最大的收益”

        for (int t = 1; t <= k; t++) {                  // 固定交易次数（t=0 时最大收益都是0 ∴ 可跳过）
            int maxProfitAfterBuy = Integer.MIN_VALUE;  // 在前 d-1 天内买入且之前最多交易 t-1 次所能获得的最大收益
            for (int d = 1; d < n; d++) {
                maxProfitAfterBuy = Math.max(maxProfitAfterBuy, -prices[d-1] + dp[t-1][d-1]);  // 注意是 -prices[d-1]
                dp[t][d] = Math.max(dp[t][d-1], prices[d] + maxProfitAfterBuy);
            }
        }

        return dp[k][n - 1];
    }

    /*
     * 解法2：DP
     * - 💎思路：若确定使用 DP 来解一道题，则：
     *   1. 根据原问题确定子问题的定义 —— 原问题是“最后一天的最大收益” ∴ 子问题应该是“第 d 天的最大收益”；
     *   2. 找到递推表达式（状态转移方程）—— 这就需要从题中识别出所有会对子问题产生影响的变量 —— 第 d 天的最大收益取决于：
     *        a. 第 d-1 天的最大收益；
     *        b. 第 d 天的操作（buy/sell/hold）；
     *        - 由此可列出递推表达式：maxProfit[d] = maxProfit[d-1] + prices[d]。
     *      然而该表达式并不完善 ∵ 第 d 天能做的操作（即👆表达式中是否能 + prices[d]）取决于“当前是否持有股票”这一变量 ——
     *      只有没有股票才能买入、有股票才能卖出 ∴ 要给表达式加一维将该变量考虑进来 —— 0表示没有股票，1表示持有股票：
     *        - maxProfit[d][0] = max(maxProfit[d-1][0], maxProfit[d-1][1] + prices[d])
     *        - maxProfit[d][1] = max(maxProfit[d-1][1], maxProfit[d-1][0] - prices[d])
     *      然而该表达式仍然不完善 ∵ 在买入时（即👆表达式中是否能 - prices[d]）没有考虑“当前交易次数是否 < k 次”的限制
     *      ∴ 要给表达式再加一维将该变量考虑进来：
     *        - maxProfit[d][t][0] = max(maxProfit[d-1][t][0], maxProfit[d-1][t][1] + prices[d])
     *        - maxProfit[d][t][1] = max(maxProfit[d-1][t][1], maxProfit[d-1][t-1][0] - prices[d])
     *      注意：交易次数+1发生在买入时，即买入时已经在进行一次交易了 ∴ 卖出时的股价上表达式中在 + prices[d] 时
     *
     * - 👉总结：面试中的 DP 题目最难也就是3维的了。
     * - 时间复杂度 O(kn)，空间复杂度 O(kn)。
     * */
    public static int maxProfit2(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int k = 2;
        int n = prices.length;
        int[][][] dp = new int[n][k+1][2];  // dp[d][t][h] 表示“在第 d 天，是/否持有股票，最多交易 t 次时所能获得的最大的收益”

        for (int t = 0; t < k; t++)  // 注意处理 base cases：第0天、持有股票、最多交替 t 次时的最大收益都是负的第0天的股价
            dp[0][t][1] = -prices[0];

        for (int d = 1; d < n; d++) {
            for (int t = 1; t <= k; t++) {
                dp[d][t][0] = Math.max(dp[d-1][t][0], dp[d-1][t][1] + prices[d]);
                dp[d][t][1] = Math.max(dp[d-1][t][1], dp[d-1][t-1][0] - prices[d]);
            }
        }

        return dp[n-1][k][0];
    }

    public static void main(String[] args) {
        log(maxProfit2(new int[]{3, 5, 0, 3, 2, 5, 2, 5}));  // expects 8. [-, -, buy, -, -, sell, buy, sell]
        log(maxProfit2(new int[]{0, 1, 2, 3, 4, 4, 3, 2}));  // expects 4. [buy, -, -, -, sell, -, -, -]
        log(maxProfit2(new int[]{7, 6, 4, 3, 1}));           // expects 0. no transaction.
    }
}