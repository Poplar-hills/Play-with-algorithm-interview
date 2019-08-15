package DP.OverlappingSubproblems;

import static Utils.Helpers.log;
import static Utils.Helpers.maxOfN;

import java.util.Arrays;

/*
* Integer Break
*
* - Given a positive integer n, break it into the sum of at least two positive integers and maximize the
*   product of those integers. Return the maximum product you can get.
*
* - ⭐总结：
*   1. 当一个问题具有递归结构，且这个结构中有重叠子问题 & 满足最优子结构性质时，就可以使用 Memoization 或 DP 解法：
*                                          Memoization（自顶向下）
*                                                ↗
*                递归问题  →  重叠子问题 & 最优子结构
*                                                ↘
*                                             DP（自底向上）
*      - 其中，最优子结构保证了动态规划求最优解的正确性，而重叠子问题保证了高效性。
*      - 若一个问题没有重叠子问题，也是可以使 DP 的，但这样做达不到 DP 本身的提速目的。正是因为有重叠子问题，使得重复状态不需要
*        重复计算，才有 DP 这种方法的优势。
*   2. 对比 Memoization 和 DP 两种方法，从时间、空间效率上来说 DP 更优，但对于大部分情况，递归的消耗近乎可以忽略。
* */

public class L343_IntegerBreak {
    /*
    * 解法1：Recursion + Memoization (也是一种 DFS)
    * - 思路：与 L279_PerfectSquares 解法2极其类似，这里进行更具体的分析：
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
    * ⭐ 原理：之所以可以使用这样的递归结构解决问题，是因为该题是一个求最优解的问题，而求最优解的问题只要可以分解，它就符合
    *   “最优子结构”性质，即“通过求子问题的最优解，可以获得原问题的最优解”。
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
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
            res = maxOfN(res, i*(n-i), i*integerBreak(n-i, cache));  // 求几种分割方案中的最优解（即各整数乘积最大者）
                                     // i*(n-i) 是将 n 分成2部分的解；i*integerBreak(n-i) 是将 n 分成更多份的解中的最优解
        return cache[n] = res;
    }

    /*
    * 解法2：DP (bottom-up iteration)
    * - 思路：与 L279_PerfectSquares 解法3极其类似。
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    public static int integerBreak2(int n) {
        assert n >= 2;
        int[] cache = new int[n + 1];
        cache[1] = 1;                    // 先解答最小问题

        for (int i = 2; i <= n; i++)     // 从小到大一个一个解决更大的问题
            for (int j = 1; j < i; j++)  // 将 i 分割成 j 和 i-j
                cache[i] = maxOfN(cache[i], j*(i-j), j*cache[i-j]);  // 此时 cache[i-j] 已经被计算过了

        return cache[n];                 // 最后返回最大问题的解
    }

    public static void main(String[] args) {
        log(integerBreak2(4));   // expects 4.  (4 = 2 + 2, 2 × 2 = 4)
        log(integerBreak2(2));   // expects 1.  (2 = 1 + 1, 1 × 1 = 1)
        log(integerBreak2(10));  // expects 36. (10 = 3 + 3 + 4, 3 × 3 × 4 = 36)
    }
}