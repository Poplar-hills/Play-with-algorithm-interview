package DP.LIS;

import static Utils.Helpers.*;

/*
* Longest Increasing Subsequence
*
* - Given an unsorted array of integers, find the length of longest increasing subsequence.
* */

public class L300_LongestIncreasingSubsequence {
    public static int lengthOfLIS(int[] nums) {
        return 0;
    }

    public static void main(String[] args) {
        log(lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));  // expects 4. The LIS is [2,3,7,101]
    }
}