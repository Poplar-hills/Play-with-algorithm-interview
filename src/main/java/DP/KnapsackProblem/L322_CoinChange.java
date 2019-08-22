package DP.KnapsackProblem;

import static Utils.Helpers.*;

/*
* Coin Change
*
* - You are given coins of different denominations and a total amount of money amount. Write a function to
*   compute the fewest number of coins that you need to make up that amount. If that amount of money cannot
*   be made up by any combination of the coins, return -1.
* */

public class L322_CoinChange {
    public static int coinChange(int[] coins, int amount) {
        return 0;
    }

    public static void main(String[] args) {
        log(coinChange(new int[]{1, 2, 5}, 11));  // expects 3. (5 + 5 + 1)
        log(coinChange(new int[]{2}, 3));         // expects -1.
    }
}
