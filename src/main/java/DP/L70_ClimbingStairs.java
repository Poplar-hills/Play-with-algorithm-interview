package DP;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.log;

/*
* Climbing Stairs
*
* - You are climbing a stair case. It takes n steps to reach to the top. Each time you can either climb 1 or 2 steps.
*   In how many distinct ways can you climb to the top? Note: Given n will be a positive integer.
* */

public class L70_ClimbingStairs {
    /*
    * 解法1：找规律 -> Fibonacci
    * - 思路：该问题非常类似 L279_PerfectSquares，因此同样可以用图论建模：
    *        0 <---- 2 <---- 4
    *          ↖   ↙   ↖   ↙   ↖
    *            1 <---- 3 <---- 5
    *   当可视化之后发现每个顶点 n（除了0和1之外）都与 n-1 和 n-2 直接相连，因此可知：顶点 n 到达0的路径数 = 顶点 n-1 到达0的路径数
    *   + 顶点 n-2 到达0的路径数，即 f(n) = f(n-1) + f(n-2)，即 Fibonacci sequence。因此该题目转化为求第 n 个 Fibonacci 数。
    * - 实现：采用 DP/Fibonacci 中的解法3（bottom-up 方式的 DP）。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int climbStairs1(int n) {
        int[] cache = new int[n + 2];
        cache[0] = 0;
        cache[1] = 1;
        for (int i = 2; i <= n + 1; i++)
            cache[i] = cache[i - 1] + cache[i - 2];
        return cache[n + 1];
    }

    public static void main(String[] args) {
        log(climbStairs1(2));  // expects 2 (1+1, 2 in one go)
        log(climbStairs1(3));  // expects 3 (1+1, 1+2, 2+1)
    }
}
