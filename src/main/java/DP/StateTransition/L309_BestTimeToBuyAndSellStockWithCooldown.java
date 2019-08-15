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
    * 解法1：
    * - 思路：
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int maxProfit(int[] prices) {
        if (prices.length < 2) return 0;

        int has1_doNothing = -prices[0];
        int has1_Sell = 0;
        int has0_doNothing = 0;
        int has0_Buy = -prices[0];

        for (int i = 1; i < prices.length; i++) {
            has1_doNothing = Math.max(has1_doNothing, has0_Buy);
            has0_Buy = -prices[i] + has0_doNothing;
            has0_doNothing = Math.max(has0_doNothing, has1_Sell);
            has1_Sell = prices[i] + has1_doNothing;
        }

        return Math.max(has1_Sell, has0_doNothing);
    }

    public static void main(String[] args) {
        log(maxProfit(new int[]{1, 2, 3, 0, 2}));  // expects 3. [buy, sell, cooldown, buy, sell]
        log(maxProfit(new int[]{1, 2}));           // expects 1. [buy, sell]
        log(maxProfit(new int[]{2, 1}));           // expects 0. [cooldown, cooldown]
        log(maxProfit(new int[]{3, 3}));           // expects 0. [cooldown, cooldown] or [buy, sell]
    }
}
