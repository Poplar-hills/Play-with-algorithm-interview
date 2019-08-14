package DP.StateTransition;

import static Utils.Helpers.log;

/*
* House Robber II
*
* - 基本与 L198_HouseRobber 中的条件一样，只是该题中的街道为环形，即给定的数组 nums 中的最后一个元素和第一个元素相邻。问在该
*   街道中，在不触发警报的情况下，最大能盗取多少财产。
* */
public class L213_HouseRobberII {
    public static int rob(int[] nums) {
        return 0;
    }

    public static void main(String[] args) {
        log(rob(new int[]{2, 3, 2}));     // expects 3.
        log(rob(new int[]{1, 2, 3, 1}));  // expects 4.
    }
}
