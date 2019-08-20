package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Complete Knapsack Problem
*
* - 在0/1背包问题基础上改变一点 —— 每种物品不再又有一个，而是可以添加任意多个。
*
* - 分析：
*   - Greedy Algorithm：
*     采用贪心算法看上去是可行的：∵ 每种物品有无限个 ∴ 先不断放入性价比最高的物品，直到背包容量不足以再放时尝试放入性价比第二高的
*     物品，若仍旧无法填满，则再用性价比第三高的物品来填充…… 这样看上去可行，但仍然存在反例：
*                           Item       0     1
*                           Weight     5     7
*                           Value      5     8
*                           v / w      1    1.14
*     若背包容量为10，∵ 物品1的性价比 > 物品0 ∴ 先放物品1，之后的剩余空间已放不下任何一种物品了，所以背包里的价值就是8。而实际上
*     放入两个物品0即可得到价值10，因此贪心算法并不适用。
*
*   - DP：
*     DP 的关键是寻找原问题的子问题，并写出状态转移方程：
*     - 子问题与0/1背包一样，仍然是：f(i, j) 表示“当背包剩余容量为 j 时，从前 i 个物品中能得到的最大价值”。
*     - 状态转移方程：∵ 每件物品可以放入无数个 ∴ 对每件物品来说，策略已经不再是放或不放的问题了，而是放入多少件的问题。设能放入的
*       件数为 k，则约束条件为：0 <= w[i]*k <= j，即在剩余容量 j 内可以放入0, 1, 2, ... k 件物品 i。因此状态转移方程为：
*       f(i, j) = max(v[i]*k + f(i-1, j-w[i]*k))，其中 0 <= w[i]*k <= j。
* */

public class CompleteKnapsack {
    /*
    * 解法1：Recursion + Memoization
    * - 思路：top-down 方式。
    * - 时间复杂度 O(n*c)，空间复杂度 O(n*c)。
    * */
    public static int knapsack(int[] w, int[] v, int c) {
        int n = w.length;
        int[][] cache = new int[n][c + 1];
        for (int[] row : cache)
            Arrays.fill(row, -1);
        return largestValue(n - 1, c, w, v, cache);
    }

    private static int largestValue(int i, int j, int[] w, int[] v, int[][] cache) {
        if (i < 0 || j == 0) return 0;
        if (cache[i][j] != -1) return cache[i][j];

        int res = largestValue(i - 1, j, w, v, cache);
        if (j >= w[i])
            for (int k = 1; w[i] * k <= j; k++)
                res = Math.max(res, v[i] * k + largestValue(i - 1, j - w[i] * k, w, v, cache));

        return cache[i][j] = res;
    }

    public static void main(String[] args) {
        log(knapsack(new int[]{5, 7}, new int[]{5, 8}, 10));  // expects 10. (5 + 5)
    }
}
