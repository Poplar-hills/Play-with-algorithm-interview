package DP.DPBasics;

import static Utils.Helpers.log;

/*
 * Subarray Sum Equals K
 *
 * - Given an array of integers and an integer k, you need to find the total number of continuous subarrays
 *   whose sum equals to k.
 *
 * - 分析：若该题中的数组元素都是正数，则可以通过滑动窗口方式简单解决。但该题中数组元素既可以是正也可以是负（如 test case 5），
 *   当 window sum > k 时无法判断是应该右移左边界还是右移右边界，因此需要进行动态规划。
 * */

public class L560_SubarraySumEqualsK {
    /*
     * 解法1：DP (Saving cummulative sums)
     * - 思路：DP 可以从暴力解法开始思考，即先看看一共有多少种 sum 组合：
     *   [4, 2, -1, 5]
     *    -
     *    ----
     *    --------
     *    -----------
     *       -
     *       -----
     *       --------
     *          --
     *          -----
     *              -
     *   像这样 n 个元素，从每个元素开始往后累加一遍，是 O(n^n) 的复杂度。∵ 累加的过程存在大量重复计算 ∴ 可以通过缓存
     *   cummulative sums 的方式进行优化，即对数组的每个位置 i 缓存 nums[0..i] 之和。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int subarraySum(int[] nums, int k) {
        int count = 0, len = nums.length;
        int[] sums = new int[len + 1];  // sums[i] 上存储 nums[0..i] 之和
        sums[0] = 0;                    // 前0个元素的和为0（之所以要多出 sums[0]=0 这个元素是因为在后面遍历过程中每次要
                                        // 减去 sums[j]，若没有 sums[0]=0，则无法遍历到单个元素。
        for (int i = 1; i <= len; i++)
            sums[i] = sums[i - 1] + nums[i - 1];  // 填充 sums 数组

        for (int i = 1; i <= len; i++)
            for (int j = 0; j < i; j++)
                if (sums[i] - sums[j] == k)
                    count++;

        return count;
    }

    public static void main(String[] args) {
        log(subarraySum(new int[]{1, 1, 1}, 2));                 // expects 2. (1+1, 1+1)
        log(subarraySum(new int[]{1, 2, 3}, 3));                 // expects 2. (1+2, 3)
        log(subarraySum(new int[]{4, 2, 1, 5, 2, 6, 8, 7}, 8));  // expects 4. (2+1+5, 1+5+2, 2+6, 8)
        log(subarraySum(new int[]{-1, -1, 1}, 0));               // expects 1. (-1+1)
        log(subarraySum(new int[]{4, 2, -1, 5, -5}, 5));         // expects 3. (4+2-1, 4+2-1+5-5, 5)
    }
}
