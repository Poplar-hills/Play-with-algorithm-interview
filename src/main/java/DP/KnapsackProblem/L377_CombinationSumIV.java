package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Combination Sum IV
*
* - Given an integer array with all positive numbers and no duplicates, find the number of possible
*   combinations that add up to a positive integer target. (注：1.数组中的整数可被重复使用；2.结果是顺序相关的）
* */

public class L377_CombinationSumIV {
    /*
    * - 分析：Think about the recurrence relation first. How does the # of combinations of the target related
    *   to the # of combinations of numbers that are smaller than the target? We know that target is the sum
    *   of some numbers in the array. Imagine we only need one more number to reach target, this number can
    *   be any one in the array. So the # of combinations of target, f(t) = sum(f(t - nums[i])),
    *   where 0 <= i < nums.length && t >= nums[i]（这就是该问题的状态转移方程）.
    *
    *   Therefore, f(4) = f(4-1) + f(4-2) + f(4-3)
    *                   = f(3) + f(2) + f(1)
    *                   = f(3-1) + f(3-2) + f(3-3) + f(2-1) + f(2-2) + f(1-1)
    *                   = f(2) + f(1) + f(0) + f(1) + f(0) + f(0)
    *                   = 3 * f(1) + 4 * f(0)
    *   Let f(0) = 1    = 7 * f(0)
    *                   = 7
    *
    *   基于以上分析，可写出下面的 naive 递归解，时间复杂度为 O(n^n)。
    * */
    public static int combinationSum(int[] nums, int target) {
        if (target < 0 || nums == null || nums.length == 0) return 0;
        if (target == 0) return 1;
        int res = 0;
        for (int num : nums)
            if (num <= target)
                res += combinationSum(nums, target - num);
        return res;
    }

    /*
    * 解法1：Recursion + Memoization
    * - 思路：基于以上分析，该问题满足最优子结构、重叠子问题性质，因此可以采用 Memoization 优化上面的地归解。
    * - 时间复杂度 O(n*target)，空间复杂度 O(target)。
    * */
    public static int combinationSum1(int[] nums, int target) {
        if (target < 0 || nums == null || nums.length == 0) return 0;
        int[] cache = new int[target + 1];
        Arrays.fill(cache, -1);
        cache[0] = 1;
        return combinationSum1(nums, target, cache);
    }

    public static int combinationSum1(int[] nums, int t, int[] cache) {
        if (t == 0) return 1;
        if (cache[t] != -1) return cache[t];

        int res = 0;
        for (int num : nums)
            if (t >= num)
                res += combinationSum1(nums, t - num, cache);

        return cache[t] = res;
    }

    public static void main(String[] args) {
        log(combinationSum1(new int[]{1, 2, 3}, 4));  // expects 7. (1111, 112, 211, 121, 13, 31, 22)
    }
}