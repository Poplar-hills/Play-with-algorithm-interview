package DP.KnapsackProblem;

import static Utils.Helpers.*;

/*
* Complete Knapsack Problem
*
* - 在0/1背包问题基础上改变一点 —— 每种物品不再又有一个，而是可以添加任意多个。
*
* - 分析：对每件物品来说，策略已经不再是放或不放的问题了，而是放入多少件的问题。
*   - Greedy Algorithm：
*
*
* */
public class CompleteKnapsack {
    public static int knapsack(int[] w, int[] v, int c) {
        return 0;
    }

    public static void main(String[] args) {
        log(knapsack(new int[]{0, 5, 7}, new int[]{0, 5, 8}, 10));  // expects 10.
    }
}
