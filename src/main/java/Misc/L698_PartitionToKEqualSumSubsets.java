package Misc;

import static Utils.Helpers.log;

/*
 * - Given an integer array nums and an integer k, return true if it is possible to divide this array into k
 *   non-empty subsets whose sums are all equal.
 * */

public class L698_PartitionToKEqualSumSubsets {
    /*
     * 解法1：
     * -
     * */
    public static boolean canPartitionKSubsets(int[] nums, int k) {
        return false;
    }

    public static void main(String[] args) {
        log(canPartitionKSubsets(new int[]{4, 3, 2, 3, 5, 2, 1}, 4));
        // expects true. Can be divided into (5), (1,4), (2,3), (2,3)

        log(canPartitionKSubsets(new int[]{1, 2, 3, 4}, 3));
        // expects false
    }
}