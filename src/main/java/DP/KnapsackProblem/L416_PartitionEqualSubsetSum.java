package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Partition Equal Subset Sum
*
* - Given a non-empty array containing only positive integers, find if the array can be partitioned into two
*   subsets such that the sum of elements in both subsets is equal.
*
* - 分析：该题是个经典的背包问题，只需稍稍转化一下即可看出本质 —— 要求将 n 个数字分成和相等的两部分，那么这两部分各自的和就一定都
*   是 sum/2，因此该题可以转化为：从 n 个物品中选出能填满 sum/2 容量的背包的物品。转化后的问题与0/1背包问题还稍有不同：
*     1. 不涉及物品价值，所以只有一个变量；
*     2. 背包需要完全填满，不允许有剩余空间。
*   由此可定义：
*     1. 子问题：f(i, s) 表示“前 i 个整数的组合是否能填满 s”；
*     2. 状态转移方程：f(i, s) = f(i-1, s) || f(i-1, s-v[i])。解释：(前 i-1 个整数的组合就能填满 s) || (前 i-1 个整数的
*        组合能填满 s-v[i]，再加上整数 i 的值刚好填满)。
* */

public class L416_PartitionEqualSubsetSum {
    /*
    * 解法1：Recursion + Memoization
    * - 思路：top-down。
    * - 时间复杂度 O(n*sum)，空间复杂度 O(n*sum)。
    * */
    public static boolean canPartition(int[] nums) {
        int sum = Arrays.stream(nums).reduce(0, Integer::sum);  // 相当于背包容量
        if (sum % 2 == 1) return false;   // 若总和为奇数则一定无解

        int n = nums.length;
        int halfSum = sum / 2;
        int[][] cache = new int[n][halfSum + 1];
        for (int[] row : cache)
            Arrays.fill(row, -1);  // cache 中 -1表示未计算；0表示 false；1表示 true

        return canPartition(n - 1, halfSum, nums, cache);
    }

    private static boolean canPartition(int i, int s, int[] nums, int[][] cache) {
        if (s == 0) return true;
        if (s < 0 || i < 0) return false;
        if (cache[i][s] != -1) return cache[i][s] == 1;

        boolean res = canPartition(i - 1, s, nums, cache) || canPartition(i - 1, s - nums[i], nums, cache);
        cache[i][s] = res ? 1 : 0;
        return res;
    }

    /*
    * 解法1：DP
    * - 思路：bottom-up。
    * - 时间复杂度 O(n*sum)，空间复杂度 O(n*sum)。
    * */
    public static boolean canPartition2(int[] nums) {
        int sum = Arrays.stream(nums).reduce(0, Integer::sum);
        if (sum % 2 == 1) return false;

        int n = nums.length;
        int halfSum = sum / 2;
        int[][] cache = new int[n][halfSum + 1];
        for (int[] row : cache)
            Arrays.fill(row, -1);

        for (int j = 0; j <= halfSum; j++)
            cache[0][j] = nums[0] == j ? 1 : 0;  // 解决最基本问题：只有当 nums[0] == j（即 nums[0] 刚好填满容量 j 时）返回 true

        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= halfSum; j++) {
                cache[i][j] = cache[i - 1][j];
                if (j >= nums[i])
                    cache[i][j] = cache[i][j] | cache[i - 1][j - nums[i]];  // bitwise operation: 1 | 0 = 1; 1 & 0 = 0
            }
        }

        return cache[n - 1][halfSum] == 1;
    }

    public static void main(String[] args) {
        log(canPartition(new int[]{1, 5, 11, 5}));    // expects true. ([1, 5, 5] and [11])
        log(canPartition(new int[]{1, 2, 3, 4}));     // expects true. ([1, 4] and [2, 3])
        log(canPartition(new int[]{1, 2, 3, 5}));     // expects false
        log(canPartition(new int[]{1, 5, 3}));        // expects false
        log(canPartition(new int[]{1, 100, 5, 3}));   // expects false
        log(canPartition(new int[]{1}));              // expects false
    }
}
