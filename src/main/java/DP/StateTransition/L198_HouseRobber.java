package DP.StateTransition;

import static Utils.Helpers.log;

/*
* House Robber
*
* - You are a professional robber planning to rob houses along a street. Each house has a certain amount of
*   money stashed, the only constraint stopping you from robbing each of them is that adjacent houses have
*   security system connected and it will automatically contact the police if two adjacent houses were broken
*   into on the same night.
* - Given a list of non-negative integers representing the amount of money of each house, determine the maximum
*   amount of money you can rob tonight without alerting the police.
* */

public class L198_HouseRobber {
    public static int rob(int[] nums) {
        return 0;
    }

    public static void main(String[] args) {
        log(rob(new int[]{1, 2, 3, 1}));     // expects 4.  House 1 -> house 3 ($1 + $3 = $4).
        log(rob(new int[]{2, 7, 9, 3, 1}));  // expects 12. House 1 -> house 3 -> house 5 ($2 + $9 + $1).
    }
}