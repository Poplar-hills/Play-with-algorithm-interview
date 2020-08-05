package DP.S3_StateTransition;

import static Utils.Helpers.log;

import java.util.Arrays;

/*
 * House Robber
 *
 * - You are a professional robber planning to rob houses along a street where each house has a certain amount
 *   of money stashed. However if two adjacent houses were broken into on the same night, the security system
 *   will go off and the police will be alerted.
 *
 * - Given a list of non-negative integers representing the amount of money of each house, determine the
 *   maximum amount of money you can rob tonight without alerting the police.
 * */

public class L198_HouseRobber {
    /*
     * 超时解：Brute Force
     * - 思路：该题的本质是一个组合优化问题 —— 在所有房子中，哪几个房子的组合能满足条件：1.房子之间各不相邻 2.收益最大化。
     *   因此可以遍历所有房子的组合，从符合条件的组合中找到最大的收益。
     * - 时间复杂度 O((2^n)*n)。∵ 每个房子有抢/不抢2种可能，n 个房子共有 2^n 种组合 ∴ 遍历所有组合就是 O(2^n) 操作；从所有
     *   组合中筛出符合条件1的组合是 O(n) 操作 ∴ 整体是 O((2^n)*n)。
     * */

    /*
     * 解法1：Recursion + Memoization (DFS with cache)
     * - 思路：类似 L343_IntegerBreak 解法1的思路，对问题进行自顶向下的分解：f(0) 表示“从 [0,n) 区间内的房子中所能抢到的
     *   最大收益”。对于 nums=[3,4,1,2] 来说：
     *                                        f(0)
     *                          抢3/     抢4/     抢1\     抢2\
     *                       3+f(2)    4+f(3)      1        2
     *                      抢1/  \抢2   抢2|
     *                        1    2       2
     *   公式表达：f(0) = max(3+f(2), 4+f(3), 1, 2)
     *                = max(3+max(1,2), 4+2, 1, 2)
     *                = max(3+2, 4+2, 1, 2)
     *                = 6
     *   总结一下：
     *     - 子问题定义：f(i) 表示“从 [i,n) 区间内的房子中所能得到的最大收益”；
     *     - 递推表达式：f(i) = max(num[j] + f(j+2))，其中 i ∈ [0,n)，j ∈ [i,n-2)。
     *   这样的分解可以很自然的使用递归实现，又 ∵ 分解过程中存在重叠子问题 ∴ 可使用 memoization 进行优化。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int rob1(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int[] cache = new int[nums.length + 1];
        Arrays.fill(cache, -1);           // ∵ 收益可能为0 ∴ 初始化为-1
        return tryToRob(nums, 0, cache);
    }

    private static int tryToRob(int[] nums, int i, int[] cache) {  // 计算 [i..n) 内的最大收益
        if (i >= nums.length) return 0;        // ∵ 下面会 +2 ∴ 这里要 i >= nums.length
        if (cache[i] != -1) return cache[i];

        int res = 0;
        for (int j = i; j < nums.length; j++)  // 范围固定的情况下，看哪种方案收益最大，例：在 [2,4,5] 内，是抢 2+4 收益大，还是 3+5，还是 2+5
            res = Math.max(res, nums[j] + tryToRob(nums, j + 2, cache));  // 这里不用管 j+2 越界问题 ∵ 上面 i >= nums.length 已经处理了

        return cache[i] = res;
    }

    /*
     * 解法2：DP
     * - 思路：直接将解法1转换为 DP 写法（仍然是从上到下分解任务的思路）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int rob2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] dp = new int[n + 1];
        dp[n - 1] = nums[n - 1];          // 先解答最后一个问题，即抢 n-1 号的收益

        for (int i = n - 2; i >= 0; i--)  // 计算 [i..n) 内的最大收益
            for (int j = i; j < n; j++)   // 范围固定的情况下，看哪种方案收益最大
                dp[i] = Math.max(dp[i], nums[j] + (j + 2 < n ? dp[j + 2] : 0));

        return dp[0];
    }

    /*
     * 解法3：DP
     * - 💎思路：这类题可以从最基本问题（最后一个问题）开始思考，例如对于 nums=[3,4,1,2]：若只有1栋房子 [2] 则收益就是2；
     *   若有2栋房子 [1,2] 则收益是 max(1,2)；若有3栋房子 [4,1,2] 则收益是 max(4+2, 1)；若有4栋房子 [3,4,1,2] 则收益是
     *   max(3+f(2), f(1)) —— 从中可得：
     *     - 子问题定义：f(i) 表示“从 [i,n) 区间内的房子中所能得到的最大收益”；
     *     - 递推表达式：f(i) = max(nums[i] + f(i+2), f(i+1))。
     * - 本质：∵ 每个房子都有抢/不抢两种选择 ∴ 只要取其中收益最大的选择即可，而不用像解法1一样在每个范围内尝试所有可能的抢法。
     *   f(0) = max(nums[0]+f(2), f(1)) 中的 nums[0]+f(2) 就是“抢0号房的最大收益”，f(1) 是“不抢0号房的最大收益”。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int rob3(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] dp = new int[n + 1];
        dp[n] = 0;
        dp[n - 1] = nums[n - 1];

        for (int i = n - 2; i >= 0; i--)
            dp[i] = Math.max(nums[i] + dp[i + 2], dp[i + 1]);

        return dp[0];
    }

    /*
     * 解法4：DP
     * - 思路：不同于解法3，该解法采用自下而上的 DP 思路，先解决基本问题，再递推出高层次问题的解。
     *   - 定义子问题：f(i) 表示“从前 i 所房子中所能得到的最大收益”；
     *   - 递推表达式：f(i) = max(nums[i] + f(i-2), f(i-1))。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int rob4(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = nums[0];

        for (int i = 2; i <= n; i++)
            dp[i] = Math.max(nums[i - 1] + dp[i - 2], dp[i - 1]);

        return dp[n];
    }

    /*
     * 解法5：DP（解法4的另一种写法，更直观）
     * - 思路：基本思路与解法4一致，但定义子问题稍有区别：f(i) 表示“从前 i 所房子里能抢到的最大收益”。于是递推表达式变成：
     *   f(i) = max(nums[i] + f(i-2), f(i-1))
     * - 实现：dp 数组只开辟 n 大小、dp[0] 不再赋0。
     * - 👉注意：与解法4比较，加深理解解法4中 dp[0]=0 的作用）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int rob5(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];                     // 到第0所房子为止的最大收益
        dp[1] = Math.max(nums[0], nums[1]);  // 到底1所房子为止的最大收益

        for (int i = 2; i < n; i++)
            dp[i] = Math.max(nums[i] + dp[i - 2], dp[i - 1]);

        return dp[n - 1];
    }

    /*
     * 解法6：DP（解法5的空间优化版）
     * - 思路：与解法5一致。
     * - 实现：∵ 在解法5中 f(i) = max(nums[i] + f(i-2), f(i-1))，即第 i 上的解只与 i-2、i-1 上的解有关 ∴ 不需要维护
     *   整个 dp 数组 ∴ 可以采用与 L509_FibonacciNumber 解法5类似的方式，只维护两个状态来优化空间复杂度。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int rob6(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int prev = 0, curr = nums[0];

        for (int num : nums) {
            int next = Math.max(num + prev, curr);
            prev = curr;
            curr = next;
        }

        return curr;
    }

    /*
     * 解法7：DP（双路递推）
     * - 思路：与 L123_BestTimeToBuyAndSellStockIII 解法2一致。可得：
     *   1. 子问题定义：
     *      - f(i,0) 表示“不抢第 i 所房子时从前 i 所房子里能抢到的最大收益”；
     *      - f(i,1) 表示“抢第 i 所房子时从前 i 所房子里能抢到的最大收益”；
     *   2. 递推表达式：
     *      - f(i,0) = max(f(i-1,1), f(i-1,0))
     *      - f(i,1) = f(i-1,0) + nums[i]
     *      ∴ f(i) = max(f(i,0), f(i,1))
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int rob7(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[][] dp = new int[n][2];
        dp[0][1] = nums[0];

        for (int i = 1; i < n; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1]);
            dp[i][1] = dp[i - 1][0] + nums[i];
        }

        return Math.max(dp[n - 1][0], dp[n - 1][1]);
    }

    /*
     * 解法8：DP（解法7的空间优化版）
     * - 思路：与解法7一致。
     * - 实现：
     *   1. 解法7中第 dp[i][0]、dp[i][1] 只与 dp[i-1][0]、dp[i-1][1] 相关 ∴ 该维度不需要维护数组，可使用两个变量代替：
     *      dp[i][0] -> curr[0]    dp[i-1][0] -> prev[0]
     *      dp[i][1] -> curr[1]    dp[i-1][1] -> prev[1]
     *   2. 第二个维度（抢/不抢）同样可以使用两个独立变量维护，从而更具语义：
     *      curr[0] -> skipCurr    prev[0] -> skipPrev
     *      curr[1] -> robCurr     prev[1] -> robPrev
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int rob8(int[] nums) {
        int skipPrev = 0;                  // 不抢前一间的最大收益
        int robPrev = 0;                   // 抢前一间的最大收益
        int skipCurr = 0;                  // 不抢当前房子的最大收益
        int robCurr = 0;                   // 抢当前房子的最大收益

        for (int num : nums) {             // 从第0间房子开始，计算在抢/不抢第 i 间房子时的最大收益
            robCurr = num + skipPrev;                // y(i) = nums[i] + n(i-1)
            skipCurr = Math.max(robPrev, skipPrev);  // n(i) = max(y(i-1), n(i-1))
            skipPrev = skipCurr;
            robPrev = robCurr;
        }

        return Math.max(skipCurr, robCurr);  // f(i) = max(y(i), n(i))
    }

    public static void main(String[] args) {
        log(rob1(new int[]{3, 4, 1, 2}));     // expects 6.  [3, (4), 1, (2)]
        log(rob1(new int[]{4, 3, 1, 2}));     // expects 6.  [(4), 3, 1, (2)]
        log(rob1(new int[]{1, 2, 3, 1}));     // expects 4.  [(1), 2, (3), 1].
        log(rob1(new int[]{2, 7, 9, 3, 1}));  // expects 12. [(2), 7, (9), 3, (1)]
    }
}