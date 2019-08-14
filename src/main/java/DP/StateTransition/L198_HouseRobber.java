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
*   1. 解法1采用了递归，其 tryToRob 方法用于“计算从某个范围内的房子中能偷得的最大所得”，这就是递归中的“函数定义”。明确合理的函数
*      定义对于写出正确的递归逻辑至关重要。
*   2. 解法2、3采用了 DP，而“函数定义”在 DP 中的对应概念是“状态”，例如“[0..n-1]内的最大所得”就是该问题的顶层状态，由于在该状态
*      下采取了不同的行动（偷0号、偷1号……），该问题的状态发生了转移，产生了其他3个可能的状态。而描述清楚这些状态直接的转移方式（即
*      明确的“状态转移方程”）对于写出正确的 DP 逻辑至关重要。例如，该问题的状态转移方程：
*      f(0..n-1) = max(v(0)+f(1..n-1), v(1)+f(3..n-1), v(3)+f(5..n-1), ..., v(n-1))，其中 f 为“某区间内的最大所得”，
*      v 为某房子的所得。
* */

public class L198_HouseRobber {
    /*
    * 超时解：暴力破解
    * - 思路：该题的本质是一个组合优化问题 —— 在所有房子中，哪几个房子的组合能满足条件：1.房子之间都不相邻 2.收益最大化。因此可以
    *   遍历所有组合，从中筛出所有符合条件的组合，并从中找到最大的收益。
    * - 时间复杂度 O((2^n)*n)，空间复杂度 O()。每个房子有2种可能（偷或不偷），n 个房子共有 2^n 种组合，因此遍历所有组合是
    *   O(2^n) 操作；在所有组合中进行筛选，检查每种组合其是否符合"房子之间不相邻"的条件，这是 O(n) 操作，因此整体是 O((2^n)*n)。
    * */

    /*
    * 解法1：Recursion + Memoization (DFS)
    * - 思路：∵ 该题的本质是一个组合优化问题 ∴ 并不需要求出所有的组合，只需要像 L91_DecodeWays 解法1那样对问题进行分解：
    *                                            [0..n-1]内的最大所得
    *                       偷0号/                    偷1号|            ...   偷n-1号\
    *                  [2..n-1]内的最大所得        [3..n-1]内的最大所得            []内的最大所得
    *             偷2号/   偷3号|     \           偷3号/         \
    *     [4..n-1]内的最大所得   ...    ...  [5..n-1]内的最大所得   ...
    *   这样的分解的含义：[0..n-1]内的最多所得 = Max((偷0号所得 + [2..n-1]内的最多所得), (偷1号所得 + [3..n-1]内的最多所得), ..., 偷n-1号所得)
    *   这样的分解可以很自然的使用递归实现，而又因为分解过程中存在重叠子问题，可以使用 memoization 进行优化。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int rob1(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int[] cache = new int[nums.length + 1];
        Arrays.fill(cache, -1);
        return tryToRob(nums, 0, cache);
    }

    private static int tryToRob(int[] nums, int start, int[] cache) {     // 计算 [start..n-1] 内的最大所得
        if (start >= nums.length) return 0;
        if (cache[start] != -1) return cache[start];

        int res = 0;
        for (int i = start; i < nums.length; i++)
            res = Math.max(res, nums[i] + tryToRob(nums, i + 2, cache));  // 例：res = 1号所得 + [2..n-1]内的最大所得
                                                                          // 这里不用管 i+2 越界问题 ∵ 上面 start >= nums.length 已经覆盖过了
        return cache[start] = res;
    }

    /*
    * 解法2：DP
    * - 思路：类似 L91_DecodeWays 解法2。recursion 是从前往后递归，DP 是从后往前递推，前一个问题的解是建立在后面问题的解的基础上。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int rob2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] cache = new int[n];
        Arrays.fill(cache, -1);
        cache[n - 1] = nums[n - 1];  // 先解答最后一个问题，即偷 n-1 号的所得

        for (int start = n - 2; start >= 0; start--)  // 计算 [start..n-1] 内的最大所得
            for (int i = start; i < n; i++)           // 范围固定的情况下，看哪种方案所得最多，例：求[2..5]内的最大所得，是偷2、4所得多还是偷3、5所得多
                cache[start] = Math.max(cache[start], nums[i] + (i + 2 < n ? cache[i + 2] : 0));

        return cache[0];
    }

    /*
    * 解法3：更自然的 DP
    * - 思路：解法2的 DP 思路是先经过解法1的递归然后再反向思考后得到的。另一种思考方式是直接采用 DP 思路 —— 先解决小问题后，然后
    *   推导出通用逻辑。5分钟视频讲解 SEE: https://www.youtube.com/watch?v=xlvhyfcoQa4。
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static int rob3(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        if (n == 1) return nums[0];                     // 只有1个房子的情况
        if (n == 2) return Math.max(nums[0], nums[1]);  // 只有2个房子的情况（这两种情况体现的是自然的思考过程，最终代码里可以不要，因为在后面的通用逻辑中已覆盖）

        int[] dp = new int[n];                          // 有多于2个房子的情况
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);

        for (int i = 2; i < dp.length; i++)
            dp[i] = Math.max(nums[i] + dp[i - 2], dp[i - 1]);  // nums[i] + dp[i-2] 是偷房子 i 的最大所得；而 dp[i-1] 是不偷房子 i 的最大所得

        return dp[n - 1];
    }

    /*
    * 解法4：
    * - 思路：∵ 每个房子有2种可能（偷或不偷）
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static int rob4(int[] nums) {
        int prevNo = 0;   // 偷了前一间的最大所得
        int prevYes = 0;  // 没偷前一间的最大所得

        for (int n : nums) {
            int currYes = prevNo + n;                // 偷当前这间的最大所得 = 没偷前一间的最大所得 + 当前房子里的钱
            int currNo = Math.max(prevNo, prevYes);  // 不偷当前这间的最大所得 = max(没偷前一间的最大所得, 偷了前一间的最大所得)
            prevNo = currNo;
            prevYes = currYes;
        }

        return Math.max(prevNo, prevYes);
    }

    public static void main(String[] args) {
        log(rob4(new int[]{3, 4, 1, 2}));     // expects 6. [3, (4), 1, (2)]
        log(rob4(new int[]{4, 3, 1, 2}));     // expects 6. [(4), 3, 1, (2)]
        log(rob4(new int[]{1, 2, 3, 1}));     // expects 4. [(1), 2, (3), 1].
        log(rob4(new int[]{2, 7, 9, 3, 1}));  // expects 12. [(2), 7, (9), 3, (1)]
    }
}