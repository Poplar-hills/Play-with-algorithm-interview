package DP;

import static Utils.Helpers.log;

import java.util.Arrays;

/*
* Integer Break
*
* - Given a positive integer n, break it into the sum of at least two positive integers and maximize the
*   product of those integers. Return the maximum product you can get.
* */

public class L343_IntegerBreak {
    /*
    * 解法1：top-down DP
    * - 思路：
    *   1. 因为需要将 n 分割成几份是未知的，因此很难使用循环解决（不知道需要几重循环），需要使用递归解决（只要设置好终止条件，
    *      其余的就交给递归即可）。
    *   2. 因此可以对 n 进行递归分割（动画讲解 SEE: https://coding.imooc.com/lesson/82.html#mid=2953，2'38''）：
    *                            分割4
    *                    1+?/    2+?|    3+?\
    *                  分割3      分割2      分割1
    *              1+?/  2+?\   1+?|
    *             分割2    分割1   分割1
    *            1+?|
    *             分割1
    *      - 要分割4有三种方案：1+?、2+?、3+?（其中?代表分割出来的一个或多个整数），最后只需从其中选出各整数乘积最大的即可；
    *      - 对于方案 1+? 来说，?中的整数之和为3，而3又有两种分割方案：1+?、2+?；
    *      - 这样下去直到每部分都分割成最小整数1时递归结束。
    *   3. 在不断分割过程中出现了很多重叠子问题（如分割2），这就可以使用 memoization 或 tabulation 进行优化：
    *      - 递归分解问题 + memoization 其实就是 top-down DP；
    *      - 从小到大解决问题（即 tabulation）其实就是 bottom-up DP。
    *
    * - 原理：之所以可以使用这样的递归结构解决问题，是因为该题是一个求最优解的问题，而求最优解的问题只要可以分解，它就符合
    *   “最优子结构”性质，即“通过求子问题的最优解，可以获得原问题的最优解”。
    * - 时间复杂度 O()，空间复杂度 O()。
    * */
    public static int integerBreak(int n) {
        assert n >= 2;               // ∵ 题中要求 n 至少分要被割成两部分 ∴ 要 >= 2
        int[] cache = new int[n + 1];
        Arrays.fill(cache, -1);
        return integerBreak(n, cache);
    }

    private static int integerBreak(int n, int[] cache) {
        if (n == 1) return 1;        // 递归终止条件
        if (cache[n] != -1) return cache[n];

        int res = -1;
        for (int i = 1; i < n; i++)  // 将 n 按照 1+?、2+?、3+?...几种方案进行分割，并对子问题（n-i 部分）进行进一步分割
            res = Math.max(res, Math.max(i*(n-i), i*integerBreak(n-i, cache)));  // 求几种分割方案中的最优解（即各整数乘积最大者）
                                     // i*(n-i) 是将 n 分成2部分的解；i*integerBreak(n-i) 是将 n 分成更多份的解中的最优解
        return cache[n] = res;
    }

    /*
    * 解法2：bottom-up DP
    * - 思路：
    *
    *
    * */
    public static int integerBreak2(int n) {
        assert n >= 2;
        int[] cache = new int[n + 1];
        cache[1] = 1;                  // 先解答最小问题

        for (int i = 2; i <= n; i++)
            for (int j = 1; j < i; j++)
                cache[i] = Math.max(cache[i], Math.max(j*(i-j), j*cache[i-j]));

        return cache[n];
    }

    public static void main(String[] args) {
        log(integerBreak2(4));   // expects 4.  (4 = 2 + 2, 2 × 2 = 4)
        log(integerBreak2(2));   // expects 1.  (2 = 1 + 1, 1 × 1 = 1)
        log(integerBreak2(10));  // expects 36. (10 = 3 + 3 + 4, 3 × 3 × 4 = 36)
    }
}