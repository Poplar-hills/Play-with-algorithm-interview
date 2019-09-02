package DP.KnapsackProblem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.*;

/*
* Target Sum
*
* - 给定一个非零数字序列，在这些数字前面加上 + 或 - 号，求一共有多少种方式使其计算结果为给定的整数 S。
* */

public class L494_TargetSum {
    /*
    * 解法1：Back-tracking（即 DFS，而没有 memoization 的 DFS 就是 Brute force)
    * - TODO: Brute force 与 Back-tracking 与 DFS 的关系？？？
    * - 思路：∵ nums 中的每个元素都有 + 或 - 两种选择 ∴ 每个选择都会产生两条路径。比如对 nums=[1,2,3], S=0 来说：
    *                          0
    *                    +1/        \-1
    *                 1                -1
    *             +2/  -2\          +2/  -2\
    *            3       -1        1       -3
    *        +3/ -3\  +3/ -3\  +3/ -3\  +3/ -3\
    *        6     0  2    -4   4   -2   0    -6
    *   可见形成了一个树形结构，而树天然具有递归性，因此可采用回溯法（即 DFS）求解。
    * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
    * */
    public static int findTargetSumWays(int[] nums, int S) {
        if (nums == null || nums.length == 0) return 0;
        int sum = Arrays.stream(nums).reduce(0, Integer::sum);
        return dfs(nums, S, 0, sum);
    }

    private static int dfs(int[] nums, int s, int i, int sum) {
        if (i == nums.length) return s == 0 ? 1 : 0;  // i = nums.length 时递归到底，若此时 s 为0则说明找到一个解
        if (s > sum || s < -sum) return 0;            // 若 s 越过 [-sum, sum] 范围时一定无解，可直接返回0

        return dfs(nums, s - nums[i], i + 1, sum - nums[i])   // 给 nums[i] 负号的情况
             + dfs(nums, s + nums[i], i + 1, sum - nums[i]);  // 给 nums[i] 正号的情况
    }

    /*
     * 解法2：Recursion + Memoization
     * - 思路：在选择过程中很可能存在重叠子问题，比如对 nums=[1,1,1], S=2 来说：
     *                           2
     *                    +1/        \-1
     *                  3                1
     *             +1/  -1\          +1/  -1\
     *            4        2        2        0
     *        +1/ -1\  +1/ -1\  +1/ -1\  +1/ -1\
     *        5     3  3     1   3    1   1    -1
     *   💎 当剩余容量经过加/减 nums 中的第二个1后得到两个3，他们的计算结果一致，因此应该被缓存。而说到建立缓存就需要回答4个问题：
     *   1. 内容：缓存的内容就应该是递归函数的返回结果，即"将 s 填满有多少种方式"；
     *   2. 维度：缓存的维度数 = 递归函数入参中的变量个数，即 ∵ 2个条件（索引 i、剩余容量 s）确定一个计算结果 ∴ 该缓存应是一个二维数组；
     *   3. 大小：∵ i ∈ [0, nums.length), s ∈ [-sum, sum] ∴ 缓存空间大小应是 cache[nums.length][2 * sum + 1]；
     *   4. 初值：∵ 缓存的内容是"多少种方式"，一定是 ≥ 0 的 ∴ 初值可设为-1。
     * - 时间复杂度 O(n*sum)，空间复杂度 O(n*sum)。
     * */
    public static int findTargetSumWays2(int[] nums, int S) {
        if (nums == null || nums.length == 0) return 0;
        int sum = Arrays.stream(nums).reduce(0, Integer::sum);

        int[][] cache = new int[nums.length][sum * 2 + 1];
        for (int[] row : cache)
            Arrays.fill(row, -1);

        return dfs2(nums, S, 0, sum, cache);
    }

    private static int dfs2(int[] nums, int s, int i, int sum, int[][] cache) {
        if (i == nums.length) return s == 0 ? 1 : 0;
        if (s > sum || s < -sum) return 0;

        if (cache[i][s + sum] == -1) {  // ∵ s ∈ [-sum, sum] ∴ 在读写 cache 时需要给 s 加上一个偏移量 sum
            cache[i][s + sum] = dfs2(nums, s - nums[i], i + 1, sum - nums[i], cache)
                              + dfs2(nums, s + nums[i], i + 1, sum - nums[i], cache);
        }

        return cache[i][s + sum];
    }

    /*
    * 解法3：解法2的 Map 版
    * - 思路：使用 Map 实现缓存，key 为 "s->i" 的形式，value 为计算结果。时间、空间复杂度与解法2一致。
    * */
    public static int findTargetSumWays3(int[] nums, int S) {
        if (nums == null || nums.length == 0) return 0;
        int sum = Arrays.stream(nums).reduce(0, Integer::sum);
        return dfs3(nums, S, 0, sum, new HashMap<>());
    }

    private static int dfs3(int[] nums, int s, int i, int sum, Map<String, Integer> map) {
        if (i == nums.length) return s == 0 ? 1 : 0;
        if (s > sum || s < -sum) return 0;

        String key = s + "->" + i;
        if (!map.containsKey(key)) {
            map.put(key, dfs3(nums, s - nums[i], i + 1, sum - nums[i], map)
                       + dfs3(nums, s + nums[i], i + 1, sum - nums[i], map));
        }

        return map.get(key);
    }

	/*
    * 解法4：DP
    * - 思路：容量为 S，nums 中的每个元素都有 + 或 - 两种选择
    *   - 定义子问题：f(i, s) 表示"用前 i 个元素填充剩余容量 s 共有几种方式"；
    *   - 状态转移方程：f(i, s) = f(i-1, s-nums[i]) + f(i-1, s+nums[i])。
    *   - 填表验证：对于 nums=[1,1,1,1], S=2 有：
    *          v | i\s -3 -2 -1  0  1  2  3   (其中 s ∈ [-sum, sum])
    *          1 |  0   0  0  1  0  1  0  0
    *          1 |  1   0  1  0  2  0  1  0
    *          1 |  2   1  0  3  0  3  0  1
    *          1 |  3   0  4  0  6  0  4  0
    * - 时间复杂度 O(n*sum)，空间复杂度 O(n*sum)。
    * */
    public static int findTargetSumWays4(int[] nums, int S) {
        if (nums == null || nums.length == 0) return 0;

        int sum = Arrays.stream(nums).reduce(0, Integer::sum);
        if (S > sum || S < -sum) return 0;

        int n = nums.length;
        int[][] dp = new int[n][sum * 2 + 1];    // ∵ s ∈ [-sum, sum] ∴ 开辟 sum*2+1 的空间

        for (int s = -sum; s <= sum; s++) {      // base case
            if (s == 0) dp[0][s] += 1;           // 若容量 s=0，则结果至少为1（nums[i]=0 时是特殊情况 ∵ -0 = +0 = 0 ∴ 结果应为2）
            if (Math.abs(s) == nums[0]) dp[0][s] += 1;
        }

        for (int i = 1; i < n; i++) {
            for (int s = -sum; s <= sum; s++) {
                if (s - nums[i] >= -sum)         // 注意越界情况（比如上面填表中 s=-5 时）
                    dp[i][s + sum] += dp[i - 1][s + sum - nums[i]];
                if (s + nums[i] <= sum)          // 注意越界情况（比如上面填表中 s=5 时）
                    dp[i][s + sum] += dp[i - 1][s + sum + nums[i]];
            }
        }

        return dp[n - 1][S + sum];  // 注意最终要返回的是经过加/减 i 个元素得到 S 的结果 ∴ 应取第 S + sum 个元素，而非最后一个元素
    }

    /*
    * 解法4：DP + 一维数组
    * - 思路：通过一点数学推导转化为0/1背包问题：设 nums 中加 + 的元素之和为 plusSum，加 - 的元素之和为 minusSum，则有：
    *       plusSum + minusSum = sum
    *       plusSum - minusSum = S
    *   两边相加得到：2 * plusSum = S + sum，最终得到：plusSum = (S + sum) / 2，于是问题转化为：
    *   “用 nums 中的元素填充 (S+sum)/2，共有几种选择方式能填满？”，这就是个典型的0/1背包问题了 —— 每个元素有放/不放两种选择。
    * - 时间复杂度 O(n*(sum+S))，空间复杂度 O(S+sum)。
    * */
    public static int findTargetSumWays5(int[] nums, int S) {
        if (nums == null || nums.length == 0) return 0;

        int sum = Arrays.stream(nums).reduce(0, Integer::sum);
        if ((S + sum) % 2 == 1 || S > sum) return 0;  // 若 S+sum 为奇数，则 ÷2 后不是整数，说明无解

        int[] dp = new int[(S + sum) / 2 + 1];  // 从这往下就是标准的 _ZeroOneKnapsack 解法4的实现
        for (int s = 0; s < dp.length; s++) {
            if (s == 0) dp[s] += 1;             // 若容量 s=0，则结果至少为1（nums[0]=0 时为2，nums[0]!=0 时再看下面的条件是否满足）
            if (nums[0] == s) dp[s] += 1;
        }

        for (int i = 1; i < nums.length; i++)
            for (int s = dp.length - 1; s >= nums[i]; s--)
                dp[s] += dp[s - nums[i]];

        return dp[dp.length - 1];
    }

    /*
    * 解法5：解法4的精简版（复杂度一致）
    * */
    public static int findTargetSumWays6(int[] nums, int S) {
        if (nums == null || nums.length == 0) return 0;

        int sum = Arrays.stream(nums).reduce(0, Integer::sum);
        if ((S + sum) % 2 == 1 || S > sum) return 0;

        int[] dp = new int[(S + sum) / 2 + 1];
        dp[0] = 1;            // 若容量 s=0，则结果至少为1，下面再次覆盖后才是 dp[0] 的最终结果

        for (int num : nums)  // 这里从第0个元素开始遍历、覆盖
            for (int s = dp.length - 1; s >= num; s--)
                dp[s] += dp[s - num];

        return dp[dp.length - 1];
    }

    public static void main(String[] args) {
        log(findTargetSumWays(new int[]{1, 1, 1, 1}, 2));
        // expects 4. -1+1+1+1、+1-1+1+1、+1+1-1+1、+1+1+1-1

        log(findTargetSumWays(new int[]{2, 1, 1, 2}, 0));
        // expects 4. +2-1+1-2、-2+1-1+2、+2+1-1-2、-2-1+1+2

        log(findTargetSumWays(new int[]{0, 0, 1}, 1));
        // expects 4. +0+0+1、-0-0+1、+0-0+1、-0+0+1

        log(findTargetSumWays(new int[]{7, 9, 3, 8, 0, 2, 4, 8, 3, 9}, 0));
        // expects 0.
    }
}
