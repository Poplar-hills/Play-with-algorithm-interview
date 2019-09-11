package DP.StateTransition;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Best Time to Buy and Sell Stock with Cooldown
*
* - 给定一个数组，表示一只股票在某几天内的价格。设计一个使利益最大化的交易算法，条件：
*   1. You may do as many transactions as you like (ie, buy one and sell one share of the stock multiple times.
*   2. You can't engage in multiple transactions at the same time (ie, must sell the stock before you buy again).
*   3. After you sell your stock, you cannot buy stock on next day. (ie, cooldown 1 day).
* */

public class L309_BestTimeToBuyAndSellStockWithCooldown {
    /*
    * 解法1：DP
    * - 思路：∵ 该问题具有明显的状态转移特点 ∴ 可以采用 L198_HouseRobber 解法4的状态转移思路，写出状态转移方程后使用 DP 实现。
    *   而要写出状态转移方程，需先分析有哪些 action 会使状态发生转移。由题中可知，共有4种 action：buy、sell、hold1、hold0。
    *   由此可以画出状态转移图：
    *                               +-----> sell <------+
    *                               |        |          |  ↗—↘
    *                              buy ------|------> hold1   |
    *                               ↑        ↓  ↗—↘        ↖-↙
    *                               +----- hold0   |
    *                                           ↖-↙
    *   - 该问题同时还符合最优子结构性质 —— 在股价已定的情况下，每天的最大收益和采取的 action 有以下递推关系：
    *       第i天的  -->  第i天采取的  -->  第i-1天的  -->  第i-1天采取  -->  ...  -->  第1天采取的
    *       最大收益      最优 action       最大收益       的最优 action               最优 action
    *
    *   - 根据状态转移图和递推关系，可写出状态转移方程：
    *       buy[i] = hold0[i-1] - prices[i]                  // 在第i天买入后的最大收益 = 前一天持有后的收益 - 第i天的股价
    *       sell[i] = max(hold1[i-1], buy[i-1]) + prices[i]  // 在第i天卖出后的最大收益 = max(前一天持有后的收益, 前一天买入后的收益) + 第i天的股价
    *       hold1[i] = max(hold1[i-1], buy[i-1])
    *       hold0[i] = max(hold0[i-1], sell[i-1])
    *
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int n = prices.length;
        int[] buy = new int[n];
        int[] sell = new int[n];
        int[] hold0 = new int[n];
        int[] hold1 = new int[n];

        buy[0] = -prices[0];
        sell[0] = 0;
        hold0[0] = 0;
        hold1[0] = -prices[0];

        for (int i = 1; i < n; i++) {
            buy[i] = hold0[i-1] - prices[i];
            sell[i] = Math.max(hold1[i-1], buy[i-1]) + prices[i];
            hold1[i] = Math.max(hold1[i-1], buy[i-1]);
            hold0[i] = Math.max(hold0[i-1], sell[i-1]);
        }

        return Math.max(sell[n - 1], hold0[n - 1]);
    }

    /*
    * 解法2：DP（解法1的空间优化版）
    * - 思路：在解法1的基础上 ∵ 每个数组都只用到 i-1 处的值 ∴ 不需要维护整个数组，只需记录单个值即可。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int maxProfit2(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int buy = -prices[0];      // 第0天买入后的收益
        int sell = 0;              // 第0天卖出后的收益（∵ 不可能第0天就卖出 ∴ 设为0）
        int hold1 = -prices[0];    // 第0天持有1股后的收益（∵ 不可能第0天就持有股票 ∴ 设为负的第0天的股价）
        int hold0 = 0;             // 第0天什么都不做的收益

        for (int i = 1; i < prices.length; i++) {  // 每天都尝试4种 action 所能获得的最大收益
            int lastBuy = buy;
            int lastSell = sell;
            int lastHold1 = hold1;
            int lastHold0 = hold0;

            buy = lastHold0 - prices[i];                      // 第i天买入后的最大收益 = 前一天持有后的收益 - 第i天的股价
            sell = Math.max(lastHold1, lastBuy) + prices[i];  // 第i天卖出后的最大收益 = max(前一天持有后的收益, 前一天买入后的收益) + 第i天的股价
            hold1 = Math.max(lastHold1, lastBuy);
            hold0 = Math.max(lastHold0, lastSell);
        }

        return Math.max(sell, hold0);
    }

    /*
    * 解法3：Recursion + Memoization
    * - 思路：
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int maxProfit3(int[] prices) {
        int n = prices.length;
        if (n < 2) return 0;

        int[] buys = new int[n], sells = new int[n];
        Arrays.fill(buys, -1);
        Arrays.fill(sells, -1);

        return sell(prices, n - 1, buys, sells);
    }

    private static int sell(int[] prices, int i, int[] buys, int[] sells) {
        if (i == 0) return 0;
        if (i == 1) return Math.max(0, prices[1] - prices[0]);
        if (sells[i] != -1) return sells[i];

        return 0;
    }

    private static int buy(int[] prices, int i, int[] buys, int[] sells) {
        if (i == 0) return -prices[0];
        if (i == 1) return Math.max(-prices[0], -prices[1]);
        if (buys[i] != -1) return buys[i];

        return 0;
    }

    public static void main(String[] args) {
        log(maxProfit(new int[]{1, 2, 3, 0, 2}));  // expects 3. [buy, sell, cooldown, buy, sell]
        log(maxProfit(new int[]{1, 2}));           // expects 1. [buy, sell]
        log(maxProfit(new int[]{2, 1}));           // expects 0. [cooldown, cooldown]
        log(maxProfit(new int[]{3, 3}));           // expects 0. [cooldown, cooldown] or [buy, sell]
    }
}
