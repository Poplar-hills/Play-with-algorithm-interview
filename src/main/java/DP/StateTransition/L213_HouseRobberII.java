package DP.StateTransition;

import static Utils.Helpers.log;

/*
* House Robber II
*
* - 基本与 L198_HouseRobber 中的条件一样，只是该题中的街道为环形，即给定的数组 nums 中的最后一个元素和第一个元素相邻。问在该
*   街道中，在不触发警报的情况下，最多能盗取多少财产。
* */
public class L213_HouseRobberII {
    /*
    * 解法1：
    * - 思路：This problem can simply be decomposed into two L198_HouseRobber problems. Since house 0 and n-1
    *   are now neighbors and we can't rob them together, the solution is now the maximum of robbing houses
    *   [0..n-2] and robbing houses [1..n-1].
    * - 实现：采用 L198_HouseRobber 解法3。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int rob(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int n = nums.length;
        if (n == 1) return nums[0];
        return Math.max(rob(nums, 0, n - 2), rob(nums, 1, n - 1));
    }

    private static int rob(int[] nums, int l, int r) {
        int n = r - l + 1;
        int[] dp = new int[n];
        dp[0] = nums[l];
        dp[1] = Math.max(nums[l], nums[l + 1]);

        for (int i = 2; i < dp.length; i++)
            dp[i] = Math.max(nums[l + i] + dp[i - 2], dp[i - 1]);  // 注意是 nums[l+i]，而非 nums[i]，要加上 l 的偏移量

        return dp[n - 1];
    }

    /*
    * 解法2：
    * - 思路：同解法1。
    * - 实现：采用 L198_HouseRobber 解法4。
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static int rob2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int n = nums.length;
        if (n == 1) return nums[0];
        return Math.max(rob2(nums, 0, n - 2), rob2(nums, 1, n - 1));
    }

    private static int rob2(int[] nums, int l, int r) {
        int prevNo = 0, prevYes = 0;
        for (int i = l; i <= r; i++) {
            int currYes = prevNo + nums[i];
            int currNo = Math.max(prevNo, prevYes);
            prevNo = currNo;
            prevYes = currYes;
        }
        return Math.max(prevNo, prevYes);
    }

    public static void main(String[] args) {
        log(rob(new int[]{2, 3, 2}));     // expects 3. (Cannot rob house 1 and 3 as they are adjacent)
        log(rob(new int[]{1, 2, 3, 1}));  // expects 4. [(1), 2, (3), 1]
    }
}
