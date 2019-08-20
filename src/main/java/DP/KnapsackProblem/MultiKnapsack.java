package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Multi Knapsack Problem
*
* - 在0/1背包问题基础上改变一点 —— 每种物品不再又有一个，而是有固定数量个。
*
* - 分析：
*
* */

public class MultiKnapsack {
    /*
    * 解法1：Recursion + Memoization
    * - 思路：top-down 方式。
    * - 对比：
    * - 时间复杂度 O()，空间复杂度 O()。
    * */
    public static int knapsack(int[] w, int[] v, int[] q, int c) {
        return 0;
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
