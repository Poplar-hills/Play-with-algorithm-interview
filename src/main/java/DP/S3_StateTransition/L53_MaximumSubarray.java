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
 *   4. 用 DP 求 [0,n) 上每一位上的 subarray min sum，然后去其中的最小值 - O(n)
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
     * - 思路：通过建立 prefix sum 数组来消除解法1中的最内层 for 循环（无需每次遍历来求区间和）。其形式化表达为：
     *   maxSubArraySum = max(prefixSum[r] - prefixSum[l] + nums[l])。
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
     * - 思路：通过定义子问题来找到递推关系：
     *   - 定义子问题：
     *     - 若将子问题直接定义为与原问题一致：f(i) 表示"在 [0,i] 上的最大子数组之和"，这样在思考 f(i) 与 f(i-1) 之间关系时很难
     *       找到递推关系（f(0)=-4, f(1)=4, f(2)=4, f(3)=5, f(4)=5）；
     *     - ∴ 换一个思路 —— 将子问题定义为与原问题不一致：f(i) 表示"在 [0,i] 上所有以 i 为终止下标的子数组中最大的数组之和"，
     *       最后只要找到 [0,n) 上最大的 f(i) 即是原问题的解。
     *   - 递推表达式：根据子问题定义 ∵ f(i-1) 已经表示"在 [0,i] 上所有以 i 为终止下标的子数组中最大的数组之和" ∴ 求 f(i) 其实
     *     就是比较 f(i-1) + nums[i] 与 nums[i] 之间的大小（只有加了 nums[i] 才是以 i 为终止下边的 subarray）∴ 可得到递推表
     *     达式：f(i) = max(f(i-1) + nums[i], nums[i])
     * - 例：[-4, 4, -2, 3, -1]
     *       -4       - f(0) = nums[0] = 4
     *           4      - f(1)：即比较 [-4,4]、[4] 这2个子数组之和；f(1) = max(-4+4, 4) = 4
     *               2      - f(2)：即比较 [-4,4,-2]、[4,-2]、[-2] 这3个子数组之和；∵ f(1) 已经是 [-4,4]、[4] 这2个子数组之和中最大的了 ∴ f(2) = min(f(1)+nums[2], nums[2]) = min(4-2, 2) = 2
     *                  5     - f(3)：即比较 [-4,4,-2,3]、[4,-2,3]、[-2,3]、[3] 这4个子数组之和；∵ f(3) [-4,4,-2]、[4,-2]、[-2] 这3个子数组之和中最大的了 ∴ f(3) = min(f(2)+nums[3], nums[3]) = min(2+3, 3) = 5
     *                      4    - f(4)：....；f(4) = min(f(3)+nums[4], nums[4]) = min(5-1, -1) = 4
     * - Reference SEE: https://zhuanlan.zhihu.com/p/85188269
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     */
    public static int maxSubArray3(int[] nums) {
        int n = nums.length, maxSubSum = Integer.MIN_VALUE;
        int[] dp = new int[n];
        dp[0] = nums[0];

        for (int i = 1; i < n; i++)
            dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);

        for (int d : dp)
            maxSubSum = Math.max(maxSubSum, d);  // 最后再求 dp 数组中的最大值

        return maxSubSum;
    }

    /**
     * 解法4：Kadane's algorithm（即 DP 的空间优化版）
     * - 思路：与解法3一致。
     * - 💎 实现：Kadane's algorithm is a DP algorithm used to find the max subarray sum of a given array
     *   of integers. It's a very efficient algorithm with a time complexity of O(n). 其核心思想是在计算过程
     *   中迭代计算 nums[0..i] 区间上的最大后缀和。其计算过程如下：
     *     [-4, 4, -2, 3, -1]
     *      i                 - [0,0] 区间上的最大后缀和就是 nums[0] = -4
     *          i             - [0,1] 区间上的最大后缀和是 max(-4 + nums[1], nums[1]) = nums[1] = 4
     *              i         - [0,2] 区间上的最大后缀和是 max(4 + nums[2], nums[2]) = 2，即 nums[1..2]
     *                 i      - [0,3] 区间上的最大后缀和是 max(2 + nums[3], nums[3]) = 5，即 nums[1..3]
     *                     i  - [0,4] 区间上的最大后缀和是 max(5 + nums[4], nums[4]) = 4，即 nums[1..4]
     *   而该遍历过程中最大的一个就是当 i=3 时的 nums[1..3]=5 ∴ 为了求最大区间和，需要求 max(最大后缀和)。
     * - 👉🏻 理解该解法后再回头看解法3，可见 Kadane's algorithm 本质上就 DP，且效率更优（one-pass + 用2个变量代替 dp 数组）。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     */
    public static int maxSubArray4(int[] nums) {
        int maxSubSumEndingHere = 0;  // nums[0..i] 区间上的最大后缀和
        int maxSubSum = Integer.MIN_VALUE;

        for (int n : nums) {
            maxSubSumEndingHere = Math.max(maxSubSumEndingHere + n, n);
            maxSubSum = Math.max(maxSubSum, maxSubSumEndingHere);
        }

        return maxSubSum;
    }

    /**
     * 解法5：
     * - 思路：与解法3、4思路相反 —— 要求 [0,n) 上的最大子数组之和，相当于求 sum[0,i] - min(sum[0,i-1])，即：
     *   不断累积的 sum - 以0为起始下标的所有子数组中最小的数组和。这就将问题就转化为如何找到前 i-1 个元素的最小数组和（之所以
     *   是 i-1 是 ∵ 若不-1则可能出现 sum[0->i] - sum[0->i] 的情况，最后得到的是空数组而非子数组）。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     */
    public static int maxSubArray5(int[] nums) {
        int sum = 0, minSumFrom0 = Integer.MAX_VALUE, maxSubSum = Integer.MIN_VALUE;

        for (int n : nums) {
            sum += n;
            minSumFrom0 = Math.min(minSumFrom0, sum - n);  // -n 是为了求 [0,i-1] 上以0为起始下标的所有子数组中最小的数组和
            maxSubSum = Math.max(maxSubSum, sum - minSumFrom0);
        }

        return maxSubSum;
    }





    //      [-4, 4, -2, 3, -1]
    //      [-4, 0, -2, 1, 0]
    //       i                 - sum=-4, minSum=-4
    //           i             - sum=0,
    //               i         - sum=-2
    //                  i      - sum=1
    //                      i  - sum=0

    public static int maxSubArray_(int[] nums) {
        int sum = 0;
        int minSum = Integer.MAX_VALUE;
        int maxSum = Integer.MIN_VALUE;

        for (int n : nums) {
            sum += n;
            minSum = Math.min(minSum, sum - n);
            maxSum = Math.max(maxSum, sum - minSum);
        }

        return maxSum;
    }
    public static void main(String[] args) {
        log(maxSubArray_(new int[]{-4, 4, -2, 3, -1}));  // expects 5. (4-2+3)
        log(maxSubArray_(new int[]{4, -1, 2, -3, 1}));   // expects 5. (4-1+2)
        log(maxSubArray_(new int[]{4, -4, 2}));          // expects 4. (4)
        log(maxSubArray_(new int[]{4, 1, -2, -2, 8}));   // expects 9. (4+1-2-2+8)
        log(maxSubArray_(new int[]{5, 4, -1, 7, 8}));    // expects 23. (5+4-1+7+8)
        log(maxSubArray_(new int[]{-1}));                // expects -1. (-1)
    }
}
