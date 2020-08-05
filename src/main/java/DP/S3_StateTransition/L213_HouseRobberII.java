package DP.S3_StateTransition;

import static Utils.Helpers.log;

/*
 * House Robber II
 *
 * - 基本与 L198_HouseRobber 中的条件一样，只是本题中的街道为环形，即给定的数组 nums 中的最后一个元素和第一个元素相邻。问在该
 *   街道中，在不触发警报的情况下，最多能盗取多少财产。
 * */

public class L213_HouseRobberII {
    /*
     * 解法1：DP
     * - 思路：This problem can simply be decomposed into two L198_HouseRobber problems. Since house 0 and n-1
     *   are now neighbors and we can't rob them together, the solution is now the maximum of robbing houses
     *   [0..n-2] and robbing houses [1..n-1].
     * - 实现：采用 L198_HouseRobber 解法4：
     *   - 定义子问题：f(i) 表示“从前 i 所房子中所能得到的最大收获”；
     *   - 递推表达式：f(i) = max(nums[i] + f(i-2), f(i-1))。
     * - 💎套路：环形 DP 题目可以用这个套路。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int rob(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int n = nums.length;
        if (n == 1) return nums[0];
        return Math.max(rob(nums, 0, n - 2), rob(nums, 1, n - 1));
    }

    private static int rob(int[] nums, int l, int r) {  // 从 [l,r] 范围内的房子里能抢到的最大收益
        int n = r - l + 1;
        if (n == 0) return 0;
        if (n == 1) return nums[l];

        int[] dp = new int[n];
        dp[0] = nums[l];
        dp[1] = Math.max(nums[l], nums[l + 1]);

        for (int i = 2; i < n; i++)
            dp[i] = Math.max(nums[i + l] + dp[i - 2], dp[i - 1]);  // 注意是 nums[i+l] 里的 l 是偏移量

        return dp[n - 1];
    }

    /*
     * 解法2：DP（双路递推）
     * - 思路：与 L198_HouseRobber 解法8一致，递推表达式为：
     *   - y(i) = nums[i] + n(i - 1)；
     *   - n(i) = max(y(i - 1), n(i - 1))。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int rob2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int n = nums.length;
        if (n == 1) return nums[0];     // 注意特殊情况的处理
        return Math.max(rob2(nums, 0, n - 2), rob2(nums, 1, n - 1));
    }

    private static int rob2(int[] nums, int l, int r) {
        int skipPrev = 0, robPrev = 0;
        int skipCurr = 0, robCurr = 0;

        for (int i = l; i <= r; i++) {  // 遍历有效范围内的房子
            robCurr = skipPrev + nums[i];
            skipCurr = Math.max(skipPrev, robPrev);
            skipPrev = skipCurr;
            robPrev = robCurr;
        }

        return Math.max(skipCurr, robCurr);
    }

    public static void main(String[] args) {
        log(rob2(new int[]{2, 3, 2}));     // expects 3. (Cannot rob house 1 and 3 as they are adjacent)
        log(rob2(new int[]{1, 2, 3, 1}));  // expects 4. [(1), 2, (3), 1]
        log(rob2(new int[]{0, 0}));        // expects 0.
    }
}
