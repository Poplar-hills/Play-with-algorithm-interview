package DP.S3_StateTransition;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
 * Best Time to Buy and Sell Stock II
 *
 * - Say you have an array for which the ith element is the price of a given stock on day i. Design an
 *   algorithm to find the maximum profit.
 *
 * - You may do as many transactions as you like (i.e., buy one and sell one share of the stock multiple times).
 *
 * - Note you may not engage in multiple transactions at the same time (i.e., you must sell the stock before
 *   you buy again).
 * */

public class L122_BestTimeToBuyAndSellStockII {
    /*
     * 解法1：Peak Valley（Kadane's Algorithm）
     * - 思路：先将数据可视化出来，例如 prices=[7,1,5,3,6,4]：
     *      |
     *    7 | *                     peak(j)
     *    6 |  •        peak(i)       *
     *    5 |   •         *         •    •
     *    4 |    •      •    •    •         *
     *    3 |     •    •        * valley(j)
     *    2 |      •  •
     *    1 |       * valley(i)
     *    0 + - - - - - - - - - - - - - - - - -
     *        0     1     2     3     4     5
     *   ∵ 可以交易任意多次 ∴ 最大收益 = 所有上升区间的幅度之和，即 maxProfit = sum(peak(i) - valley(i))。注意这里的
     *   关键点在于，每个“上升区间”必须是前后相邻的两个价位之间的，而不能是中间隔着一个 valley —— ∵ 可以交易任意多次
     *   ∴ sum(peak(i) - valley(i)) 一定 > peak(j) - valley(i)。
     * - 实现：借助 Kadane's Algorithm（即最大区间和算法）—— 先求出所有前后两元素的差值，再过滤掉其中的负差值，
     *   剩下的正差值之和即是原问题的解。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int maxsell(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int[] diffs = new int[prices.length];

        for (int i = 1; i < prices.length; i++)
            diffs[i] = prices[i] - prices[i - 1];

        return Arrays.stream(diffs)
            .filter(n -> n > 0)
            .reduce(0, Integer::sum);
	}

    /*
     * 解法2：Peak Valley（解法1的简化版）
     * - 思路：与解法1的思路一致。
     * - 实现：求出所有前后两元素的差值其实不需要计算所有的 diff，只需在遍历时判断后面元素是否 > 前面元素即可。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int maxProfit2(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int maxProfit = 0;

        for (int i = 1; i < prices.length; i++)
            if (prices[i] > prices[i - 1])
                maxProfit += prices[i] - prices[i - 1];

        return maxProfit;
	}

	/*
     * 解法3：DP
     * - 思路：The action we can do on ith day is either buy (if last action is sell), or sell
     *   (if last action is buy), or do nothing ∴ 第i天上不同的 action 会获得不同的最大收益：
     *     - 第i天尝试买入的最大收益 = max(第i天不买入的最大收益, 第i天买入的最大收益)；
     *     - 第i天尝试卖出的最大收益 = max(第i天不卖出的最大收益, 第i天卖出的最大收益)；
     *   其中，若要在第i天买入，则需之前先卖出过 ∴ 第i天买入的最大收益 = 第i-1天尝试卖出的最大收益 - 第i天的股价；
     *   同样，若要在第i天卖出，则需之前先买入过 ∴ 第i天卖出的最大收益 = 第i-1天尝试买入的最大收益 + 第i天的股价；
     *     - buy(i) = max(buy(i-1), sell[i-1] - prices[i])
     *     - sell(i) = max(sell(i-1), buy[i-1] + prices[i])
     *     - 注意：buy(i)、sell(i) 的定义是在第 i 天“尝试”买入/卖出的最大收益 —— 并不一定真在第 i 天买/卖。
     *   ∵ 要获得最大收益，则最后一定要卖出（不能持有股票）才行 ∴ sell[n-1] 即是原问题的解。
     *   例如：prices = [ 7,  1,  5,  3,  6,  4]
     *           buy = [-7, -1, -1,  1,  1,  3]
     *          sell = [ 0,  0,  4,  4,  7,  7]
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int maxProfit3(int[] prices) {
        int n = prices.length;
        int[] buy = new int[n];
        int[] sell = new int[n];

        buy[0] = -prices[0];
        sell[0] = 0;

        for (int i = 1; i < n; i++) {
            buy[i] = Math.max(buy[i - 1], sell[i - 1] - prices[i]);
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i]);
        }

        return sell[n - 1];
    }

    /*
     * 解法4：DP（解法3的简化版）
     * - 思路：与解法3一致。
     * - 实现：∵ 解法3中，sell[i]、buy[i] 只与 buy[i-1]、sell[i-1] 相关 ∴ 可以用 lastBuy、lastSell 两个状态代替
     *   buy[i-1]、sell[i-1]，不再维护整个 buy、sell 数组。通过 lastBuy、lastSell 即可递推出在当前股价下买入或卖出所能
     *   获得的最大收益 currBuy、currSell。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int maxProfit4(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int lastBuy = -prices[0];  // 上次尝试买入获得的最大收益
        int lastSell = 0;          // 上次尝试卖出获得的最大收益

        for (int price : prices) {
            int currBuy = Math.max(lastBuy, lastSell - price);   // 在当前股价下买入所能获得的最大收益
            int currSell = Math.max(lastSell, lastBuy + price);  // 在当前股价下卖出所能获得的最大收益
            lastBuy = currBuy;
            lastSell = currSell;
        }

        return lastSell;
    }

    /*
     * 解法5：DP
     * - 思路：∵ 可以交易任意次 ∴ 在 L123_BestTimeToBuyAndSellStockIII 解法2基础上去掉交易次数的维度：
     *   maxProfit[d][0] = max(maxProfit[d-1][0], maxProfit[d-1][1] + prices[d]);
     *   maxProfit[d][1] = max(maxProfit[d-1][1], maxProfit[d-1][0] - prices[d]);
     * - 💎注意：不要把“可以交易任意次”与 L188、L121、L123 中的“最多交易 k 次”混淆 —— 当可交易任意次时，dp 数组就不需要交易
     *   次数的维度，而最多交易 k 次的情况下则需要。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int maxProfit5(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int n = prices.length;
        int[][] dp = new int[n][2];
        dp[0][1] = -prices[0];

        for (int d = 1; d < n; d++) {
            dp[d][0] = Math.max(dp[d-1][0], dp[d-1][1] + prices[d]);
            dp[d][1] = Math.max(dp[d-1][1], dp[d-1][0] - prices[d]);
        }

        return dp[n-1][0];
    }

    public static void main(String[] args) {
        log(maxProfit5(new int[]{7, 1, 5, 3, 6, 4}));  // expects 7. [-, buy, sell, buy, sell, -]
        log(maxProfit5(new int[]{1, 2, 3, 4, 5}));     // expects 4. [buy, -, -, -, sell]
        log(maxProfit5(new int[]{7, 6, 4, 3, 1}));     // expects 0. no transaction.
    }
}