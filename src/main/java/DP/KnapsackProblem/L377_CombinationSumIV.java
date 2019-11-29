package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
 * Combination Sum IV
 *
 * - Given an integer array with all positive numbers and no duplicates, find the number of possible
 *   combinations that add up to a positive integer target.

 * - 注意：
 *   1. 数组中的整数可被重复使用；
 *   2. 结果是顺序相关的，即 112 和 211 是两种不同组合。
 * */

public class L377_CombinationSumIV {
    /*
      * 超时解：DFS
      * - 思路：要用 nums 中的元素凑出 target，可以采用 DFS 的方法。例如对于 nums=[2,3,4], target=6 来说，凑出6有几种方法呢？
      *        1. 先用 nums 中的元素2来凑，这样只需再凑6-2=4就好了。那么凑4有几种方法呢？
      *           a. 先从 nums 中的元素2来凑，这样只需再凑4-2=2就好了。那么凑2有几种方法呢？
      *             I. 先用 nums 中的元素2来凑，这样只需再凑2-2=0就好了。0不需要任何元素凑 ∴ 返回1。
      *             II. 再用 nums 中的3来凑 ∵ 3 > 2，凑不出来 ∴ 返回0。
      *             III. ...
      *           b. ...
      *        2. 再用 nums 中的元素3来凑，这样只需再凑6-3=3就好了。那么凑3有几种方法呢？
      *           a. ...
      *        3. ...
      *   可见这是一个递归分解过程：f(6) = f(6-2) + f(6-3) + f(6-4)
      *                              = f(4) + f(3) + f(2)
      *                              = (f(4-2) + f(4-3) + f(4-4)) + (f(3-2) + f(3-3)) + f(2-2)
      *                              = f(2) + 2*f(1) + 3*f(0)     - 1无法由任何 nums 中的元素组成 ∴ f(1) = 0
      *                              = f(2-2) + 3*f(0)
      *                              = 4*f(0)                     - 0不需要任何 nums 中的元素就可以组成 ∴ f(0) = 1
      *                              = 4
      * - 时间复杂度为 O(n^n)，空间复杂度 O(target)。
      * */
    public static int combinationSum(int[] nums, int target) {
        if (target == 0) return 1;
        int count = 0;
        for (int n : nums)
            if (target - n >= 0)
                count += combinationSum(nums, target - n);
        return count;
    }

    /*
     * 解法1：Recursion + Memoization (DFS with cache)
     * - 思路：基于以上分析，该问题满足最优子结构、重叠子问题性质，因此可以采用 Memoization 优化上面的地归解。
     * - 时间复杂度 O(n*target)，空间复杂度 O(target)。
     * */
    public static int combinationSum1(int[] nums, int target) {
        if (target <= 0 || nums == null || nums.length == 0) return 0;
        int[] cache = new int[target + 1];
        Arrays.fill(cache, -1);
        return combinationSum1(nums, target, cache);
    }

    public static int combinationSum1(int[] nums, int i, int[] cache) {
        if (i == 0) return 1;    // The base case 最基本问题，即上面的 f(0) = 1
        if (cache[i] != -1) return cache[i];

        int count = 0;
        for (int n : nums)
            if (i - n >= 0)
                count += combinationSum1(nums, i - n, cache);

        return cache[i] = count;
    }

    /*
     * 解法2：DP
     * - 思路：基于超时解中的递归过程，可总结出：
     *   - 定义子问题：f(i) 表示“用 nums 中的元素凑出 i 的方法数”；
     *   - 状态转移方程：f(i) = sum(f(i - nums[j]))，其中 j ∈ [0, nums.length) && i >= nums[j]。
     * - 时间复杂度 O(n*target)，空间复杂度 O(target)。
     * */
    public static int combinationSum2(int[] nums, int target) {
        if (target <= 0 || nums == null || nums.length == 0) return 0;

        int[] dp = new int[target + 1];
        dp[0] = 1;                         // 解决 i=0 时的 base case

        for (int i = 1; i <= target; i++)  // 循环从 i=1 开始，对每个 i 都遍历一遍 nums 中的元素
            for (int n : nums)
                if (i >= n)
                    dp[i] += dp[i - n];    // dp[i-n] 表示能凑成 i-n 的方法数

        return dp[target];
    }

    public static void main(String[] args) {
        log(combinationSum2(new int[]{2, 3, 4}, 6));   // expects 4. (2+2+2, 2+4, 4+2, 3+3)
        log(combinationSum2(new int[]{1, 2, 3}, 4));   // expects 7. (1+1+1+1, 1+1+2, 2+1+1, 1+2+1, 1+3, 3+1, 2+2)
        log(combinationSum2(new int[]{4, 1, 2}, 32));  // expects 39882198.
    }
}