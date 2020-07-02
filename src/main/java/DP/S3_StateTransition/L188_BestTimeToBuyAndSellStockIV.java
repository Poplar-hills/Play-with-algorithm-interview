package DP.S3_StateTransition;

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
    public static int maxProfit(int k, int[] prices) {
        return 0;
    }

    public static void main(String[] args) {
        log(maxProfit(2, new int[]{2, 4, 1}));           // expects 2. [buy, sell, -]
        log(maxProfit(2, new int[]{3, 2, 6, 5, 0, 3}));  // expects 7. [-, buy, sell, -, buy, sell]
    }
}