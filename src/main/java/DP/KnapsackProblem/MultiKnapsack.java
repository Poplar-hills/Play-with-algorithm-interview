package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Multi Knapsack Problem
*
* - 在完全背包问题基础上改变一点 —— 每种物品不再又有无限多个，而是只有固定数量个。
*
* - 分析：
*   - DP:
*     DP 的关键是寻找原问题的子问题，并写出状态转移方程：
*     - 子问题与0/1背包、完全背包一样，仍然是：f(i, j) 表示“当背包剩余容量为 j 时，从前 i 个物品中能得到的最大价值”。
*     - 状态转移方程：∵ 每件物品可以放入无数个 ∴ 对每件物品来说，策略已经不再是放或不放的问题了，而是放入多少件的问题。设能放入的
*       件物品 i 的件数为 k，则约束条件为：0 <= k <= q[i] 且 0 <= w[i]*k <= j。因此状态转移方程为：
*       f(i, j) = max(v[i]*k + f(i-1, j-w[i]*k))，其中 0 <= k <= q[i] 且 0 <= w[i]*k <= j。
*
* */

public class MultiKnapsack {
    /*
    * 解法1：Recursion + Memoization
    * - 思路：top-down 方式。
    * - 时间复杂度 O(n*c*k)，空间复杂度 O(n*c)，其中 k 为每种物品的最多件数。
    * */
    public static int knapsack(int[] w, int[] v, int[] q, int c) {
        int n = w.length;
        int[][] cache = new int[n][c + 1];
        for (int[] row : cache)
            Arrays.fill(row, -1);
        return largestValue(n - 1, c, w, v, q, cache);
    }

    private static int largestValue(int i, int j, int[] w, int[] v, int[] q, int[][] cache) {
        if (i < 0 || j == 0) return 0;
        if (cache[i][j] != -1) return cache[i][j];

        int res = largestValue(i - 1, j, w, v, q, cache);
        if (j >= w[i])
            for (int k = 1; k <= q[i] && w[i] * k <= j; k++)  // 这里多一个约束条件（唯一与 CompleteKnapsack 解法1不同的地方）
                res = Math.max(res, v[i] * k + largestValue(i - 1, j - w[i] * k, w, v, q, cache));

        return cache[i][j] = res;
    }

    public static void main(String[] args) {
        log(knapsack(
          new int[]{0, 3, 4, 5},  // weight
          new int[]{0, 2, 3, 4},  // value
          new int[]{0, 4, 3, 2},  // quantity
          15                      // knapsack capacity
        ));                       // expects 11. ()
    }
}
