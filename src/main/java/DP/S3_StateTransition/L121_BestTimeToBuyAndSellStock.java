package DP.S3_StateTransition;

import static Utils.Helpers.*;

/*
 * Best Time to Buy and Sell Stock
 *
 * - Say you have an array for which the ith element is the price of a given stock on day i. Design an
 *   algorithm to find the maximum profit.
 *
 * - If you were only permitted to complete at most 1 transaction (i.e., buy one and sell one share of the stock),
 *
 * - Note that you cannot sell a stock before you buy one.
 * */

public class L121_BestTimeToBuyAndSellStock {
    public static int maxProfit(int[] prices) {
        return 0;
    }

    public static void main(String[] args) {
        log(maxProfit(new int[]{7, 1, 5, 3, 6, 4}));  // expects 5. [-, buy, -, -, sell, -]
        log(maxProfit(new int[]{7, 6, 4, 3, 1}));     // expects 0. no transaction.
    }
}