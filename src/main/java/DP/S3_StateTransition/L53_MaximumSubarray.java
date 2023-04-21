package DP.S3_StateTransition;

import static Utils.Helpers.log;

/**
 * Maximum Subarray
 * - Given an integer array nums, find the subarray with the largest sum, and return its sum.
 *
 * - 💎 思路：
 *   1. Brute force - O(n^3)
 *   2. 2 pointers + Prefix sum - O(n^2)
 *   3. 转化为求前 n 个元素的最大 sum - O(n)
 *   4. 用 DP 求 [0,n] 上每一位上的 subarray min sum，然后去其中的最小值 - O(n)
 *   5. Kadane 算法 - O(n)
 */

public class L53_MaximumSubarray {
    /**
     * 解法1：Brute force
     * - 时间复杂度 O(n^3)，空间复杂度 O(1)。
     */
    public static int maxSubArray(int[] nums) {
        int n = nums.length, res = Integer.MIN_VALUE;

        for (int l = 0; l < n; l++) {
            for (int r = l; r < n; r++) {
                int sum = 0;
                for (int k = l; k <= r; k++)
                    sum += nums[k];
                res = Math.max(res, sum);
            }
        }

        return res;
    }

    /**
     * 解法2：Prefix sum
     * - 时间复杂度 O(n^2)，空间复杂度 O(1)。
     */
    public static int maxSubArray2(int[] nums) {
        int n = nums.length, res = Integer.MIN_VALUE;
        int[] preSums = new int[n];

        for (int i = 0; i < n; i++)
            preSums[i] = i == 0 ? nums[0] : preSums[i - 1] + nums[i];

        for (int l = 0; l < n; l++) {
            for (int r = l; r < n; r++) {
                int subSum = preSums[r] - preSums[l] + nums[l];
                res = Math.max(res, subSum);
            }
        }

        return res;
    }

    public static void main(String[] args) {
        log(maxSubArray2(new int[]{4, -4, 2}));         // expects 4. (4)
        log(maxSubArray2(new int[]{4, -1, 2, -3, 1}));  // expects 5. (4-1+2)
        log(maxSubArray2(new int[]{4, 1, -2, -2, 1}));  // expects 5. (4+1)
        log(maxSubArray2(new int[]{4, 1, -2, -2, 8}));  // expects 9. (4+1-2-2+8)
    }
}
