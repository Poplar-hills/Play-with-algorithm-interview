package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Knapsack Problem
*
* - 有个背包，容量为 C, 现有 n 个不同种物品，编号为 0..n-1，其中每件物品的重量为 w(i)，价值为 v(i)。求可以像这个背包中放入哪些
*   物品，使得在不超过容量的基础上，背包中的物品价值最大。
*
* - 分析：该问题本质上是个最优组合问题，尝试以下思路：
*   - Brute Force：
*     1. n 个不同种物品，每种物品都可以选择放入或不放入背包，因此共有 2^n 种组合；
*     2. 计算每种组合的总重，并过滤掉所有总重超过 C 的组合；
*     3. 再对剩下的每种组合遍历计算总价值，并选出最大者。
*     - 总时间复杂度为 O((2^n)*n)。
*
*   - Greedy Algorithm：
*     采用贪心算法（优先放入价值最高的物品）对于解决这类问题存在缺陷：∵ 本问题是要求的是一个“全局最优解”，而贪心算法是只顾眼前的选
*     择策略 ∴ 很容易举出反例：
*                           Item       0     1     2
*                           Weight     1     2     3
*                           Value      6     10    12
*                           v / w      6     10    12
*     若背包容量为5，此时使用贪心算法优先放入性价比（v/w）最高的物品，则只能放入0、1号物品（价值16，占用3的容量），而剩余的2的容
*     量就被浪费掉了。而事实上，全局最优解应该是放入1、2号物品（价值22，占用5的容量），因此贪心算法对本问题不可行。
*
*   - DP：
*     若采用 DP 则需要认清该问题中的状态有哪些，以及它们之间是如何转移的，即写出状态转移方程：
*     1. 首先状态可以通过题中的限制条件来识别 —— 1. 选择第 i 个物品处理； 2. 背包的剩余容量 c；这两个限制条件是该问题中的变量，
*        不同的变量组合就意味着该问题的不同状态，因此这两个变量也就是状态转移方程的输入参数。
*     2. 确定状态转移方程中的函数定义 —— f(i, c) 表示“在背包剩余容量为 c 的情况下处理第 i 个物品所能得到的最大价值”（注意“处理”
*        意味着可能放入也可能不放入）。
*     3. 推导状态转移方程：
*        - 因为对每种物品都有放入/不放入两种选择，因此 f 所求的“最大价值”一定来自于对这两种选择的结果求最大值；
*        - 其中，不放入 i 物品后的价值为 f(i-1, c)，即等于上一次处理完物品 i-1 后的价值；
*        - 其中，放入 i 物品后的价值为 v(i) + f(i-1, c-w(i))，即等于；
*        - 完整方程为 f(i, c) = max(f(i-1, c), v(i) + f(i-1, c-w(i)))
*
* */

public class Knapsack {
    /*
    * 解法1：Recursion + Memoization
    * - 思路：
    * - 时间复杂度 O()，空间复杂度 O()。
    * */
    public static int knapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] cache = new int[n][capacity + 1];
        for (int[] arr : cache)
            Arrays.fill(arr, -1);
        return largestValue(n - 1, weights, values, capacity, cache);
    }

    private static int largestValue(int i, int[] weights, int[] values, int capacity, int[][] cache) {
        if (i < 0 || capacity == 0) return 0;
        if (cache[i][capacity] != -1) return cache[i][capacity];

        int res = largestValue(i - 1, weights, values, capacity, cache);
        if (capacity >= weights[i])
            res = Math.max(res, values[i] + largestValue(i - 1, weights, values, capacity - weights[i], cache));
        return cache[i][capacity] = res;
    }

    /*
    * 解法2：DP
    * - 思路：
    * - 时间复杂度 O()，空间复杂度 O()。
    * */
    public static int knapsack2(int[] weights, int[] values, int capacity) {
        return 0;
    }

    public static void main(String[] args) {
        log(knapsack(new int[]{1, 2, 3}, new int[]{6, 10, 12}, 5));       // expects 22.
        log(knapsack(new int[]{1, 3, 4, 2}, new int[]{3, 9, 12, 8}, 5));  // expects 17.
    }
}
