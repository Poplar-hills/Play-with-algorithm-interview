package Array.TwoPointerSlidingWindow;

import static Utils.Helpers.log;

/*
* Minimum Size Subarray Sum
*
* - Given an array of n positive integers and a positive integer s, find the minimal length of a
*   contiguous subarray (连续子数组) of which the sum ≥ s. If there isn't one, return 0 instead.
* */

public class L209_MinimumSizeSubarraySum {
    public static int minSubArrayLen(int s, int[] nums) {  // 解法1：brute force

    }

    public static int minSubArrayLen2(int s, int[] nums) {  // 解法2：滑动窗口

    }

    public static void main(String[] args) {
        int[] nums = new int[] {2, 3, 1, 2, 4, 3};
        log(minSubArrayLen(7, nums));  // expects 2. The subarray [4,3] has the minimal length

        int[] nums2 = new int[] {2, 3, 1, 2, 4, 3};
        log(minSubArrayLen2(7, nums2));  // expects 2
    }
}
