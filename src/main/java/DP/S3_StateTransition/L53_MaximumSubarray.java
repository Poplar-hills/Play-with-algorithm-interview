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

    /**
     * 解法3：DP
     * - 思路：
     *   - 子问题定义：f(i) 表示"在所有以 i 为终止下标的 subarray 中，数组之和最大的那个值"
     *   - f(i) = max(f(i-1) + nums[i], min[i])
     * - 例：[-4, 4, -2, 3, -1]
     *       -4
     *           4
     *               2
     *                  5
     *                      4
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     */
    public static int maxSubArray3(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];

        for (int i = 1; i < n; i++)
            dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);

        int res = Integer.MIN_VALUE;
        for (int d : dp)
            res = Math.max(res, d);

        return res;
    }

    /**
     * 解法4：Kadane algorithm
     * - 思路：
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     */
    public static int maxSubArray4(int[] nums) {
        return 0;
    }

    public static void main(String[] args) {
        log(maxSubArray3(new int[]{-4, 4, -2, 3, -1}));  // expects 5. (4-2+3)
        log(maxSubArray3(new int[]{4, -1, 2, -3, 1}));   // expects 5. (4-1+2)
        log(maxSubArray3(new int[]{4, -4, 2}));          // expects 4. (4)
        log(maxSubArray3(new int[]{4, 1, -2, -2, 8}));   // expects 9. (4+1-2-2+8)
    }
}
