package Array.S5_SlidingWindow;

import static Utils.Helpers.log;

/**
 * Maximum Absolute Sum of Any Subarray
 *
 * - Given an integer array nums. Return the maximum absolute sum of any (possibly empty) subarray of nums.
 */

public class L1749_MaximumAbsoluteSumOfAnySubarray {
    /**
     * 解法1：Prefix Sum
     * - 思路：最大的子数组之和的绝对值 = [0,i] 中最大的子数组之和 - [0,i] 中最小的子数组之和 ∴ 只需维护两个变量，记录遍历
     *   过程中最大、最小的 sum，最后返回他们的差即可。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     */
    public static int maxAbsoluteSum(int[] nums) {
        int sum = 0, minSum = 0, maxSum = 0;  // 注意 minSum, maxSum 只能初始化为0（TODO:???）
        for (int n : nums) {
            sum += n;
            minSum = Math.min(minSum, sum);
            maxSum = Math.max(maxSum, sum);
        }
        return maxSum - minSum;
    }

    public static void main(String[] args) {
        log(maxAbsoluteSum(new int[]{1, -3, 2, 3, -4}));      // expects 5. [2, 3]
        log(maxAbsoluteSum(new int[]{2, -5, 1, -4, 3, -2}));  // expects 8. [-5, 1, -4]
        log(maxAbsoluteSum(new int[]{-20, 13, -20, 0, 13, -5, 15, -18, 5, -8}));  // expects 27. [-20+13-20]
        log(maxAbsoluteSum(new int[]{-8, 0, -2, 12, -31, 8, -17, 5, -9}));  // expects 44. [-31+8-17+5-9]
    }
}