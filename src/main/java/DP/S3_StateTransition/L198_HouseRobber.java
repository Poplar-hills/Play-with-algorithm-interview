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
 *
 * - ⭐ 总结：函数定义、状态、状态转移：
 *   1. 解法1采用了递归，其 tryToRob 方法用于“计算从某个范围内的房子中能抢得的最大收获”，这就是递归中的“函数定义”。明确合理的函数
 *      定义对于写出正确的递归逻辑至关重要。
 *   2. 解法2、3采用了 DP，而“函数定义”在 DP 中的对应概念是“状态”，例如“[0..n)内的最大收获”就是该问题的顶层状态，由于在该状态
 *      下采取了不同的行动（抢0号、抢1号……），该问题的状态发生了转移，产生了其他3个可能的状态。而描述清楚这些状态之间的转移方式（即
 *      明确的“状态转移方程”）对于写出正确的 DP 逻辑至关重要。例如，该问题的状态转移方程：
 *      f(0..n-1) = max(v(0)+f(1..n-1), v(1)+f(3..n-1), v(3)+f(5..n-1), ..., v(n-1))，其中 f 为“某区间内的最大收获”，
 *      v 为某房子的收获。
 * */

public class L198_HouseRobber {
    /*
     * 超时解：Brute Force
     * - 思路：该题的本质是一个组合优化问题 —— 在所有房子中，哪几个房子的组合能满足条件：1.房子之间各不相邻 2.收获最大化。
     *   因此可以遍历所有房子的组合，从符合条件的组合中找到最大的收获。
     * - 时间复杂度 O((2^n)*n)。∵ 每个房子有抢/不抢2种可能，n 个房子共有 2^n 种组合 ∴ 遍历所有组合就是 O(2^n) 操作；从所有
     *   组合中筛出符合条件1的组合是 O(n) 操作 ∴ 整体是 O((2^n)*n)。
     * */

    /*
     * 解法1：Recursion + Memoization (DFS with cache)
     * - 思路：类似 L343_IntegerBreak 解法1的思路，对问题进行分解：f(i) 表示“从 [i,n) 区间内的房子中所能得到的最大收获”，
     *   则对于 nums=[3, 4, 1, 2] 来说：
     *                                            f(0)
     *                       抢[0]/       抢[1]/     抢[2]\      抢[3]\
     *                        f(2)          f(3)       nums[2]    nums[3]
     *                  抢[2]/   \抢[3]   抢[3]|
     *                 nums[2]  nums[3]    nums[3]
     *
     *   公式表达：f(0) = max(nums[0]+f(2), nums[1]+f(3), nums[2], nums[3])
     *                = max(nums[0]+nums[2], nums[1]+nums[3]), nums[2], nums[3])
     *                = max(3+2, 4+2, 2, 2)
     *                = 6
     *   总结一下：
     *     - 子问题定义：f(i) 表示“从 [i,n) 区间内的房子中所能得到的最大收获”；
     *     - 递推表达式：f(i) = max(num[j] + f(j + 2))，其中 i ∈ [0,n)，j ∈ [i,n-2)。
     *   这样的分解可以很自然的使用递归实现，又 ∵ 分解过程中存在重叠子问题 ∴ 可使用 memoization 进行优化。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int rob1(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int[] cache = new int[nums.length + 1];
        Arrays.fill(cache, -1);           // ∵ 收获可能为0 ∴ 初始化为-1
        return tryToRob(nums, 0, cache);
    }

    private static int tryToRob(int[] nums, int i, int[] cache) {  // 计算 [i..n) 内的最大收获
        if (i >= nums.length) return 0;
        if (cache[i] != -1) return cache[i];

        int res = 0;
        for (int j = i; j < nums.length; j++)  // 范围固定的情况下，看哪种方案收获最大，例：在 [2,4,5] 内，是抢 2+4 收获大，还是 3+5，还是 2+5
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
        dp[n - 1] = nums[n - 1];          // 先解答最后一个问题，即抢 n-1 号的收获

        for (int i = n - 2; i >= 0; i--)  // 计算 [i..n) 内的最大收获
            for (int j = i; j < n; j++)   // 范围固定的情况下，看哪种方案收获最大
                dp[i] = Math.max(dp[i], nums[j] + (j + 2 < n ? dp[j + 2] : 0));

        return dp[0];
    }

    /*
     * 解法3：DP
     * - 思路：另一种思路是：∵ 每个房子都有抢/不抢两种选择 ∴ 只要取其中收益最大的选择即可。对于 nums=[3, 4, 1, 2] 来说：
     *     f(0) = max(nums[0] + f(2), f(1))，其中 f(2) = nums[2] = 2；
     *     f(1) = max(nums[1] + f(3), f(2))，其中 f(3) = nums[3] = 2；∴ f(0) = max(3+2, 4+2) = 6。
     *   总结一下：
     *     - 子问题定义：f(i) 表示“从 [i,n) 区间内的房子中所能得到的最大收获”；
     *     - 递推表达式：f(i) = max(num[i] + f(i+2), f(i+1))。
     * - 💎总结：可见该思路与解法1、2的不同之处在于不用遍历所有可能性，只考虑第一个房子抢/不抢即可。
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
     *   - 定义子问题：f(i) 表示“从前 i 所房子中所能得到的最大收获”；
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
     * 解法5：DP（解法4的另一种写法）
     * - 思路：与解法4一致。
     * - 实现：dp 数组只开辟 n 大小，dp[0] 不再赋0。
     * - 👉注意：与解法4比较，加深理解解法4中 dp[0]=0 的作用）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int rob5(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);

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

        for (int i = 1; i < nums.length; i++) {
            int next = Math.max(nums[i] + prev, curr);
            prev = curr;
            curr = next;
        }

        return curr;
    }

    /*
     * 解法7：DP（双路递推）
     * - 思路：解法3的思路是将 f(i) 定义为“从前 i 所房子中所能得到的最大收获” —— 这里同时包含了抢以及不抢 i 这2种情况。
     *   另一种思路是通过梳理不同行为对状态的影响写出不同状态的递推表达式（将抢/不抢这2种行为对应的状态分开进行递推）：
     *   - y(i) 表示“若抢第 i 所房子，则从前 i 所房子中能获得的最大收获”：y(i) = nums[i] + n(i-1)；
     *   - n(i) 表示“若不抢第 i 所房子，则从前 i 所房子中能获得的最大收获”：n(i) = max(y(i-1), n(i-1))。
     *   - f(i) = max(y(i), n(i)) 即得到原问题的解。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int rob7(int[] nums) {
        int skipPrev = 0;                    // 抢前一间的最大收获
        int robPrev = 0;                     // 不抢前一间的最大收获

        for (int n : nums) {                 // 从第一间房子开始对计算每间房子在抢/不抢时的最大收获
            int robCurr = n + skipPrev;                  // y(i) = nums[i] + n(i-1)
            int skipCurr = Math.max(robPrev, skipPrev);  // n(i) = max(y(i-1), n(i-1))
            skipPrev = skipCurr;             // 前阵变后阵
            robPrev = robCurr;
        }

        return Math.max(skipPrev, robPrev);  // f(i) = max(y(i), n(i))
    }

    public static void main(String[] args) {
        log(rob6(new int[]{3, 4, 1, 2}));     // expects 6.  [3, (4), 1, (2)]
        log(rob6(new int[]{4, 3, 1, 2}));     // expects 6.  [(4), 3, 1, (2)]
        log(rob6(new int[]{1, 2, 3, 1}));     // expects 4.  [(1), 2, (3), 1].
        log(rob6(new int[]{2, 7, 9, 3, 1}));  // expects 12. [(2), 7, (9), 3, (1)]
    }
}