package DP.S4_KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
 * Combination Sum IV
 *
 * - Given an integer array with all positive numbers and no duplicates, find the number of possible
 *   combinations that add up to a positive integer target. 该题与 L39_CombinationSum 的不同之处在于：
 *     1. 只返回解的个数即可；
 *     2. 解中元素是顺序相关的，见 Notes 第2点。
 *
 * - Notes：
 *   1. 数组中的整数可被重复使用；
 *   2. 结果是顺序相关的，即 1+1+2 和 2+1+1 是两种不同组合。
 * */

public class L377_CombinationSumIV {
    /*
     * 超时解：Recursion + Backtracking
     * - 思路：进行递归，每次递归中尝试使用 nums 中的每个元素来分解 target。例如对于 nums=[2,3,4], target=6 来说：
     *                              6
     *                   2/        3|       4\
     *                   4          3         2
     *               2/ 3| 4\    2/  3\      2|
     *               2   1   0   1    0       0        - 找到解 2+4, 3+3, 4+2
     *              2|
     *               0                                 - 找到解 2+2+2
     *
     *   形式化的表达：f(6) = f(6-2) + f(6-3) + f(6-4)
     *                    = f(4) + f(3) + f(2)
     *                    = (f(4-2) + f(4-3) + f(4-4)) + (f(3-2) + f(3-3)) + f(2-2)
     *                    = f(2) + 2*f(1) + 3*f(0)     - 1无法由任何 nums 中的元素组成 ∴ f(1)=0
     *                    = f(2-2) + 3*f(0)
     *                    = 4*f(0)                     - 0不需要任何 nums 中的元素就可以组成 ∴ f(0)=1
     *                    = 4
     * - 时间复杂度为 << O(n^n)，空间复杂度 O(target)。
     * */
    public static int combinationSum(int[] nums, int target) {
        if (target == 0) return 1;
        int count = 0;
        for (int n : nums)
            if (n <= target)
                count += combinationSum(nums, target - n);
        return count;
    }

    /*
     * 解法1：Recursion + Memoization (DFS with cache)
     * - 思路：在超时解中，可以看到其中存在重叠子问题（如 f(2) 被计算了两次）∴ 可以采用 Memoization 进行优化。
     * - 时间复杂度 O(n*target)，空间复杂度 O(target)。
     * */
    public static int combinationSum1(int[] nums, int target) {
        if (target <= 0 || nums == null || nums.length == 0) return 0;
        int[] cache = new int[target + 1];
        Arrays.fill(cache, -1);
        return helper1(nums, target, cache);
    }

    public static int helper1(int[] nums, int target, int[] cache) {
        if (target == 0) return 1;                 // The base case 最基本问题，即上面的 f(0) = 1
        if (cache[target] != -1) return cache[target];

        int count = 0;
        for (int n : nums)
            if (target - n >= 0)
                count += helper1(nums, target - n, cache);

        return cache[target] = count;
    }

    /*
     * TODO: 未完成的解法2：DP
     * - 思路：
     *   - 定义子问题：f(i,j) 表示“用 nums 中的前 i 个元素凑出 j 的方法个数”；
     *   - 递推表达式：f(i,j) = sum(f(i - nums[j]))，其中 j ∈ [0, n) && i >= nums[j]。
     *
     *   n | i\t  0  1  2  3  4  5  6
     *   2 |  0   1  -  1  -  1  -  1
     *   3 |  1   1  -  1  1  1  2  2
     *   4 |  2   1  -  1  1  2  2  4
     *
     * - 时间复杂度 O(n*target)，空间复杂度 O(target)。
     * */
    public static int combinationSum2(int[] nums, int target) {
        if (target <= 0 || nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[][] dp = new int[n][target + 1];

        for (int j = 0; j <= target; j++)
            dp[0][j] = (j % nums[0] == 0) ? 1 : -1;

        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= target; j++) {
                if (j >= nums[i])
                    dp[i][j] = dp[i - 1][j] + dp[i - 1][j - nums[i]];
            }
        }

        return dp[n - 1][target];
    }

    /*
     * 解法3：DP
     * - 思路：基于超时解中的递归过程，可知：
     *   - 定义子问题：f(i) 表示“用 nums 中的元素凑出 i 的方法个数”；
     *   - 递推表达式：f(i) = sum(f(i - nums[j]))，其中 j ∈ [0, n) && i >= nums[j]。
     * - 时间复杂度 O(n*target)，空间复杂度 O(target)。
     * */
    public static int combinationSum3(int[] nums, int target) {
        if (target <= 0 || nums == null || nums.length == 0) return 0;

        int[] dp = new int[target + 1];    // dp[i] 表示用 nums 中的元素凑出 i 的方法个数（∵ 最后要取 dp[target] ∴ 这里开辟 target+1 的空间）
        dp[0] = 1;                         // 解决 i=0 时的 base case

        for (int i = 1; i <= target; i++)  // 循环从1开始，为每个 i 都遍历一遍 nums 中的元素
            for (int n : nums)
                if (i >= n)
                    dp[i] += dp[i - n];

        return dp[target];
    }

    public static void main(String[] args) {
        log(combinationSum2(new int[]{2, 7, 3, 6}, 7));  // expects 4. (7, 2+2+3, 3+2+2, 2+3+2)
        log(combinationSum2(new int[]{2, 3, 4}, 6));     // expects 4. (2+2+2, 2+4, 4+2, 3+3)
        log(combinationSum2(new int[]{1, 2, 3}, 4));     // expects 7. (1+1+1+1, 1+1+2, 2+1+1, 1+2+1, 1+3, 3+1, 2+2)
        log(combinationSum2(new int[]{4, 1, 2}, 32));    // expects 39882198.
    }
}