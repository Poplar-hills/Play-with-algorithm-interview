package DP.StateTransition;

import static Utils.Helpers.*;

/*
* Best Time to Buy and Sell Stock with Cooldown
*
* - 基本与 L198_HouseRobber 中的条件一样，只是本题中的街道为一棵二叉树，问在该街道中，在不触发警报的情况下，最多能盗取多少财产。
*   其本质上是在问如何在二叉树上选择不相邻的节点，使节点值之和最大。
* */

public class L309_BestTimeToBuyAndSellStockWithCooldown {
    /*
    * 解法1：
    * - 思路：
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int maxProfit(int[] prices) {
        return 0;
    }

    public static void main(String[] args) {
        log(maxProfit(new int[]{1, 2, 3, 0, 2}));  // expects 3. [buy, sell, cooldown, buy, sell]
    }
}
