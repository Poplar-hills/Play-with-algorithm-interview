package DP.StateTransition;

import static Utils.Helpers.*;

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
    * - 思路：可以看出该问题具有明显的状态转移特点，因此可以尝试先写出状态转移方程（transition function），然后使用 DP 实现。
    *   而要写出状态转移方程，需先分析有哪些 action 会使状态发生转移。由题中可知，共有4种 action：buy、sell、hold1、hold0。
    *   由此可以画出状态转移图：
    *                               +-----> sell <------+
    *                               |        |          |  ↗—↘
    *                              buy ------|------> hold1   |
    *                               ↑        ↓  ↗—↘        ↖-↙
    *                               +----- hold0   |
    *                                           ↖-↙
    *   - 该问题同时还符合最优子结构性质 —— 通过求子问题的最优解可获得原问题的最优解。这体现在，在股价已定的情况下，每天的最大
    *     收益和采取的 action 有以下递推关系：
    *       第i天的  -->  第i天采取的  -->  第i-1天的  -->  第i-1天采取  -->  ...  -->  第1天采取的
    *       最大收益      最优 action       最大收益       的最优 action               最优 action
    *
    *   - 根据状态转移图和递推关系，可写出状态转移方程：
    *       buy[i] = hold0[i-1] - prices[i]                  // 在第i天买入后的最大收益 = 前一天持有后的收益 - 第i天的股价
    *       sell[i] = max(hold1[i-1], buy[i-1]) + prices[i]  // 在第i天卖出后的最大收益 = max(前一天持有后的收益, 前一天买入后的收益) + 第i天的股价
    *       hold1[i] = max(hold1[i-1], buy[i-1])
    *       hold0[i] = max(hold0[i-1], sell[i-1])
    *
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static int maxProfit(int[] prices) {
        if (prices.length < 2) return 0;

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

    public static void main(String[] args) {
        log(maxProfit(new int[]{1, 2, 3, 0, 2}));  // expects 3. [buy, sell, cooldown, buy, sell]
        log(maxProfit(new int[]{1, 2}));           // expects 1. [buy, sell]
        log(maxProfit(new int[]{2, 1}));           // expects 0. [cooldown, cooldown]
        log(maxProfit(new int[]{3, 3}));           // expects 0. [cooldown, cooldown] or [buy, sell]
    }
}
