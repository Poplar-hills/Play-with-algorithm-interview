package DP.S4_KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Partition Equal Subset Sum
*
* - Given a non-empty array containing only positive integers, find if the array can be partitioned into two
*   subsets such that the sum of elements in both subsets is equal.
*
* - 分析：该题本质上是个0/1背包问题，稍转化一下即可看出本质 —— 要求将 n 个数字分成和相等的两部分，那么这两部分各自的和就一定都
*   是 sum/2 ∴ 该题可以转化为“从 n 个物品中选出刚好能填满 sum/2 容量的物品”。由此可：
*   1. 定义子问题：f(i, s) 表示“前 i 个整数的组合是否能填满 s”；
*   2. 状态转移方程：f(i, s) = f(i-1, s) || f(i-1, s-nums[i])。
*      解释：(前 i-1 个整数刚好填满 s) || (前 i-1 个整数刚好填满 s-v[i]，再加上整数 i 的值刚好填满 s)。
* */

public class L416_PartitionEqualSubsetSum {
    /*
     * 超时解：
     * - 思路：top-down。
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
     * */
    public static boolean canPartition(int[] nums) {
        int sum = Arrays.stream(nums).reduce(0, Integer::sum);  // 相当于背包容量
        if (sum % 2 == 1) return false;                         // 若总和为奇数则一定无解
        return helper(nums, nums.length - 1, sum / 2);
    }

    private static boolean helper(int[] nums, int i, int s) {
        if (i < 0 || s < 0) return false;
        if (s == 0) return true;           // 只有完全填满时才是有效解（见分析中的第2条）
        return helper(nums, i - 1, s) || helper(nums, i - 1, s - nums[i]);
    }

    /*
     * 解法1：Recursion + Memoization
     * - 思路：在超时解的基础上加入 Memoization。
     * - 时间复杂度 O(n*sum)，空间复杂度 O(n*sum)。
     * */
    public static boolean canPartition1(int[] nums) {
        int sum = Arrays.stream(nums).reduce(0, Integer::sum);
        if (sum % 2 == 1) return false;

        int n = nums.length;
        Boolean[][] cache = new Boolean[n][sum / 2 + 1];  // Boolean 数组默认值都为 null，便于表达3种状态

        return helper1(nums, n - 1, sum / 2, cache);
    }

    private static boolean helper1(int[] nums, int i, int s, Boolean[][] cache) {
        if (s == 0) return true;
        if (s < 0 || i < 0) return false;
        if (cache[i][s] != null) return cache[i][s];
        return cache[i][s] = helper1(nums, i - 1, s, cache) || helper1(nums, i - 1, s - nums[i], cache);
    }

    /*
     * 解法2：DP
     * - 时间复杂度 O(n*sum)，空间复杂度 O(n*sum)。
     * */
    public static boolean canPartition2(int[] nums) {
        int sum = Arrays.stream(nums).reduce(0, Integer::sum);
        if (sum % 2 == 1) return false;

        int n = nums.length;
        int halfSum = sum / 2;
        int[][] dp = new int[n][halfSum + 1];
        for (int[] row : dp)
            Arrays.fill(row, -1);

        for (int j = 0; j <= halfSum; j++)
            dp[0][j] = nums[0] == j ? 1 : 0;  // 解决最基本问题：若只考虑 nums[0]，则只有当 nums[0] 刚好填满 j 时才是 true

        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= halfSum; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j >= nums[i])
                    dp[i][j] = dp[i][j] | dp[i - 1][j - nums[i]];  // bitwise operation: 1 | 0 = 1; 1 & 0 = 0
            }
        }

        return dp[n - 1][halfSum] == 1;
    }

    /*
     * 解法3：DP + 一位数组（解法2的空间优化版）
     * - 思路：类似 _ZeroOneKnapsack 的解法4。
     * - 时间复杂度 O(n*sum)，空间复杂度 O(sum)。
     * */
    public static boolean canPartition3(int[] nums) {
        int sum = 0;
        for (int num : nums) sum += num;  // for 循环比 stream 的方式快很多
        if (sum % 2 == 1) return false;

        boolean[] dp = new boolean[sum / 2 + 1];
        for (int j = 0; j < dp.length; j++)
            dp[j] = j == nums[0];

        for (int i = 1; i < nums.length; i++)         // 注意 i 从1开始遍历
            for (int j = sum / 2; j >= nums[i]; j--)  // 倒序遍历/覆盖
                dp[j] = dp[j] || dp[j - nums[i]];

        return dp[sum / 2];
    }

    public static void main(String[] args) {
        log(canPartition3(new int[]{1, 5, 11, 5}));   // expects true. ([1, 5, 5] and [11])
        log(canPartition3(new int[]{1, 2, 3, 4}));    // expects true. ([1, 4] and [2, 3])
        log(canPartition3(new int[]{1, 2, 3, 5}));    // expects false
        log(canPartition3(new int[]{1, 5, 3}));       // expects false
        log(canPartition3(new int[]{1, 2, 5}));       // expects false
        log(canPartition3(new int[]{1, 100, 5, 3}));  // expects false
        log(canPartition3(new int[]{1}));             // expects false
    }
}
