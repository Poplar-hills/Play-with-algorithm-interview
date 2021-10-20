package Misc;

import static Utils.Helpers.log;

/*
 * Increasing Triplet Subsequence
 *
 * - Given an integer array nums, return true if there exists a triple of indices (i, j, k) such that i < j < k
 *   and nums[i] < nums[j] < nums[k]. If no such indices exists, return false.
 * */

public class L334_IncreasingTripletSubsequence {
    /*
     * 解法1：
     * -
     * */
    public static boolean increasingTriplet(int[] nums) {
        return true;
    }

    public static void main(String[] args) {
        log(increasingTriplet(new int[]{1, 2, 3, 4, 5}));     // expects true. Any triplet where i < j < k is valid.
        log(increasingTriplet(new int[]{5, 4, 3, 2, 1}));     // expects false. No triplet exists.
        log(increasingTriplet(new int[]{2, 1, 5, 0, 4, 6}));  // expects true. The triplet (3, 4, 5) is valid
                                                              // because nums[3] == 0 < nums[4] == 4 < nums[5] == 6.
    }
}
