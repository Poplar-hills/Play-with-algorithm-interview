package DP.StateTransition;

import static Utils.Helpers.log;

import java.util.Arrays;

/*
 * House Robber
 *
 * - You are a professional robber planning to rob houses along a street where each house has a certain amount
 *   of money stashed. However if two adjacent houses were broken into on the same night, the security system
 *   will go off and the police will be alerted.
 * - Given a list of non-negative integers representing the amount of money of each house, determine the maximum
 *   amount of money you can rob tonight without alerting the police.
 *
 * - ⭐ 总结：函数定义、状态、状态转移：
 *   1. 解法1采用了递归，其 tryToRob 方法用于“计算从某个范围内的房子中能偷得的最大收获”，这就是递归中的“函数定义”。明确合理的函数
 *      定义对于写出正确的递归逻辑至关重要。
 *   2. 解法2、3采用了 DP，而“函数定义”在 DP 中的对应概念是“状态”，例如“[0..n)内的最大收获”就是该问题的顶层状态，由于在该状态
 *      下采取了不同的行动（偷0号、偷1号……），该问题的状态发生了转移，产生了其他3个可能的状态。而描述清楚这些状态之间的转移方式（即
 *      明确的“状态转移方程”）对于写出正确的 DP 逻辑至关重要。例如，该问题的状态转移方程：
 *      f(0..n-1) = max(v(0)+f(1..n-1), v(1)+f(3..n-1), v(3)+f(5..n-1), ..., v(n-1))，其中 f 为“某区间内的最大收获”，
 *      v 为某房子的收获。
 * */

public class L198_HouseRobber {
    /*
     * 超时解：Brute Force
     * - 思路：该题的本质是一个组合优化问题 —— 在所有房子中，哪几个房子的组合能满足条件：1.房子之间各不相邻 2.收获最大化。
     *   因此可以遍历所有房子的组合，从符合条件的组合中找到最大的收获。
     * - 时间复杂度 O((2^n)*n)。∵ 每个房子有偷/不偷2种可能，n 个房子共有 2^n 种组合 ∴ 遍历所有组合就是 O(2^n) 操作；从所有
     *   组合中筛出符合条件1的组合是 O(n) 操作 ∴ 整体是 O((2^n)*n)。
     * */

    /*
     * 解法1：Recursion + Memoization (DFS with cache)
     * - 思路：类似 L343_IntegerBreak 解法1那样对问题进行分解：设 max[0..n) 表示“从第 0~n-1 所房子中所能得到的最大收获”，则：
     *                                     max[0..n)
     *                       偷0号/         偷1号|     ...    偷n-1号\
     *                  max[2..n)          max[3..n)             max[]
     *             偷2号/   偷3号|    \    偷3号/    \
     *           max[4..n)    ...   ...  max[5..n)  ...
     *
     *   这样的分解的含义：max[0..n) = max((nums[0] + max[2..n)), (nums[1] + max[3..n)), ..., max[n-1])
     *   这样的分解可以很自然的使用递归实现，而又因为分解过程中存在重叠子问题，可以使用 memoization 进行优化。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int rob1(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int[] cache = new int[nums.length + 1];
        Arrays.fill(cache, -1);           // ∵ 收获可能为0 ∴ 初始化为-1
        return tryToRob(nums, 0, cache);
    }

    private static int tryToRob(int[] nums, int i, int[] cache) {     // 计算 [start..n) 内的最大收获
        if (i >= nums.length) return 0;
        if (cache[i] != -1) return cache[i];

        int res = 0;
        for (int j = i; j < nums.length; j++)
            res = Math.max(res, nums[j] + tryToRob(nums, j + 2, cache));  // 例：res = nums[1] + [2..n)内的最大收获
                                                                          // 这里不用管 i+2 越界问题 ∵ 上面 start >= nums.length 已经覆盖过了
        return cache[i] = res;
    }

    /*
     * 解法2：DP
     * - 思路：类似 L91_DecodeWays 解法2。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int rob2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, -1);
        dp[n - 1] = nums[n - 1];  // 先解答最后一个问题，即偷 n-1 号的收获

        for (int i = n - 2; i >= 0; i--)  // 计算 [i..n) 内的最大收获
            for (int j = i; j < n; j++)   // 范围固定的情况下，看哪种方案收获最大，例：求[2..5]内的最大收获，是偷2、4收获多还是偷3、5收获多
                dp[i] = Math.max(dp[i], nums[j] + (j + 2 < n ? dp[j + 2] : 0));

        return dp[0];
    }

    /*
     * 解法3：更自然的 DP
     * - 思路：解法2的 DP 思路是由解法1再反向思考后得到的。而更自然的 DP 思路是：∵ 每个房子都有偷/不偷两种选择 ∴ 可以有：
     *   - 定义子问题：f(i) 表示“从前 i 所房子中所能得到的最大收获”；
     *   - 状态转移方程：f(i) = max(nums[i] + f(i-2), f(i-1))。其中 nums[i] + f(i-2) 为偷第 i 所房子的最大收获，f(i-1)
     *     为不偷第 i 所房子的最大收获。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int rob3(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        if (n == 1) return nums[0];                     // 只有1个房子的情况
        if (n == 2) return Math.max(nums[0], nums[1]);  // 只有2个房子的情况

        int[] dp = new int[n];                          // 有多于2个房子的情况
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);

        for (int i = 2; i < dp.length; i++)
            dp[i] = Math.max(nums[i] + dp[i - 2], dp[i - 1]);

        return dp[n - 1];
    }

    /*
     * 解法4：DP（多状态递推思路）
     * - 思路：解法3的思路是将 f(i) 定义为“从前 i 所房子中所能得到的最大收获”，这里同时包含了偷以及不偷 i 这2种情况。另一种思路
     *   是通过梳理不同行为对状态的影响写出不同状态的递推表达式（将偷/不偷这2种行为对应的状态分开进行递推）：
     *   - y(i) 表示“若偷第 i 所房子，则从前 i 所房子中能获得的最大收获”：y(i) = n(i-1) + nums[i]；
     *   - n(i) 表示“若不偷第 i 所房子，则从前 i 所房子中能获得的最大收获”：n(i) = max(y(i-1), n(i-1))。
     *   最后只要取 max(y(i), n(i)) 即得到原问题的解。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int rob4(int[] nums) {
        int prevNo = 0;           // 偷前一间的最大收获
        int prevYes = 0;          // 不偷前一间的最大收获

        for (int n : nums) {      // 从第一间房子开始对计算每间房子在偷/不偷时的最大收获
            int currYes = prevNo + n;
            int currNo = Math.max(prevNo, prevYes);  // 不偷这间并不意味着一定要偷前一间
            prevNo = currNo;      // 前阵变后阵
            prevYes = currYes;
        }

        return Math.max(prevNo, prevYes);
    }

    public static void main(String[] args) {
        log(rob4(new int[]{3, 4, 1, 2}));     // expects 6.  [3, (4), 1, (2)]
        log(rob4(new int[]{4, 3, 1, 2}));     // expects 6.  [(4), 3, 1, (2)]
        log(rob4(new int[]{1, 2, 3, 1}));     // expects 4.  [(1), 2, (3), 1].
        log(rob4(new int[]{2, 7, 9, 3, 1}));  // expects 12. [(2), 7, (9), 3, (1)]
    }
}