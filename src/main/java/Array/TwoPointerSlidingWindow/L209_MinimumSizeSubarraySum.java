package Array.TwoPointerSlidingWindow;

import static Utils.Helpers.log;

/*
* Minimum Size Subarray Sum
*
* - Given an array of n positive integers and a positive integer s, find the minimal length of a
*   contiguous subarray (连续子数组) of which the sum ≥ s. If there isn't one, return 0 instead.
* */

public class L209_MinimumSizeSubarraySum {
    public static int minSubArrayLen(int s, int[] nums) {  // 解法1：brute force，复杂度为 O(n^3)
        if(s <= 0 || nums == null)
            throw new IllegalArgumentException("Illegal Arguments");

        int minLen = nums.length + 1;
        for (int i = 0; i < nums.length; i++) {  // 子串长度是由左右边界确定的，因此使用双重循环遍历左右所有边界值，即遍历所有子串
            for (int j = 0; j < nums.length; j++) {
                int sum = 0;
                for (int k = i; k <= j; k++)  // 遍历每个子串中的所有元素
                    sum += nums[k];
                if (sum >= s)
                    minLen = Math.min(j - i + 1, minLen);
            }
        }

        if (minLen == nums.length + 1)  // 注意 corner case
            return 0;

        return minLen;
    }

    public static int minSubArrayLen2(int s, int[] nums) {  // 解法2：滑动窗口
        if (s <= 0 || nums == null)
            throw new IllegalArgumentException("Illegal Arguments");

        // sum[i] 存放 nums[0...i-1] 的和
        int[] sums = new int[nums.length + 1];
        for (int i = 1; i < sums.length; i++)
            sums[i] = sums[i - 1] + nums[i - 1];

        int minLen = nums.length + 1;


        if (minLen == nums.length + 1)  // 注意 corner case
            return 0;

        return minLen;
    }

    public static void main(String[] args) {
        int[] nums = new int[] {2, 3, 1, 2, 4, 3};
        log(minSubArrayLen(7, nums));  // expects 2. The subarray [4,3] has the minimal length

        int[] nums3 = new int[] {2, 3, 1, 2, 4, 3};
        log(minSubArrayLen2(7, nums3));  // expects 2
    }
}
