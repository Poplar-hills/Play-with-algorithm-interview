package DP.S3_StateTransition;

import static Utils.Helpers.*;

/*
 * Best Time to Buy and Sell Stock IV
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
    public static int maxProfit(int[] prices, int fee) {
        return 0;
    }

    public static void main(String[] args) {
        log(maxProfit(new int[]{1, 3, 2, 8, 4, 9}, 2));
        // expects 8. [buy, -, -, sell, buy, sell]. Max profit = ((8-1)-2) + ((9-4)-2) = 8.
    }
}