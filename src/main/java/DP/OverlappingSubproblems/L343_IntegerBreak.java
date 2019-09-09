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
* ⭐总结：
*   1. 当一个问题具有递归结构，且该结构中有重叠子问题 & 满足最优子结构性质时，可使用 Memoization 或 DP 求解：
*                                          Memoization（自顶向下）
*                                                ↗
*                递归问题  →  重叠子问题 & 最优子结构
*                                                ↘
*                                             DP（自底向上）
*      - 其中，最优子结构保证了动态规划求最优解的正确性，而重叠子问题保证了高效性。
*      - 若一个问题没有重叠子问题，原则上也是可以使用 DP 求解的，但这样达不到 DP 本身的提速目的。正是因为有重叠子问题，使得重复
*        状态不需要重复计算，才体现出 DP 的优势。
*   2. 对比 Memoization 和 DP 两种方法，从时间、空间效率上来说 DP 更优，但对于大部分情况，递归的消耗近乎可以忽略。
*   3. 该问题之所以能通过 DP 解决，是因为它满足“最优子结构”性质，即一个最优策略的子策略也是必须是最优的，因此通过求子问题的局部
*      最优解，可以获得原问题的全局最优解。如果一个问题不能满足最优子结构性质，则不适合用 DP 求解。
* */

public class L343_IntegerBreak {
    /*
    * 超时解：DFS
    * - 思路：与 L279_PerfectSquares 解法2极其类似，这里进行更具体的分析：
    *   1. ∵ 需要将 n 分割成几份是未知的 ∴ 很难使用循环解决（不知道需要几重循环），需要使用递归解决（只要设置好终止条件，
    *      其余的就交给递归即可）。
    *   2. ∴ 可以对 n 进行递归分割（动画讲解 SEE: https://coding.imooc.com/lesson/82.html#mid=2953，2'38''）：
    *                            分割4
    *                    1+?/    2+?|    3+?\
    *                  分割3      分割2      分割1
    *              1+?/  2+?\   1+?|
    *             分割2    分割1   分割1
    *            1+?|
    *             分割1
    *      要分割4有三种方案：1+?、2+?、3+?（其中?代表分割出来的一个或多个整数），最后只需从其中选出各整数乘积最大的即可；而对于
    *      方案 1+? 来说，?中的整数之和为3，而3又有两种分割方案：1+?、2+?；这样下去直到每部分都分割成最小整数1时递归结束。
    *   3. 基于以上推断，可知：
    *      - 定义子问题：f(i) 表示“由正整数 i 分割得到的多个正整数的最大乘积”；
    *      - 状态转移方程：f(i) = max(f(i-1), f(i-2), ..., f(1))。
    *
    * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
    * */
    public static int integerBreak(int n) {
        if (n == 1) return 1;

        int maxProduct = 0;
        for (int i = 1; i < n; i++)  // 遍历所有分割方案：1*(n-1), 2*(n-2), ..., (n-1)*1
            maxProduct = maxOfN(maxProduct, i*(n-i), i*integerBreak(n-i));  // 求所有方案中的最大乘积

        return maxProduct;
    }

    /*
    * 解法1：Recursion + Memoization (DFS with cache)
    * - 思路：在超时解的基础上加入 Memoization 优化。
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    public static int integerBreak1(int n) {
        assert n >= 2;                 // ∵ 题中要求 n 至少分要被割成两部分 ∴ 要 >= 2
        int[] cache = new int[n + 1];  // 初值为0即可
        return helper1(n, cache);
    }

    private static int helper1(int n, int[] cache) {
        if (n == 1) return 1;
        if (cache[n] != 0) return cache[n];

        int res = 0;
        for (int i = 1; i < n; i++)
            res = maxOfN(res, i*(n-i), i*helper1(n-i, cache));

        return cache[n] = res;
    }

    /*
     * 解法2：DP (bottom-up iteration) - 思路：与 L279_PerfectSquares 解法3极其类似。 - 时间复杂度
     * O(n^2)，空间复杂度 O(n)。
     */
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
        log(integerBreak(4));   // expects 4.  (4 = 2 + 2, 2 × 2 = 4)
        log(integerBreak(2));   // expects 1.  (2 = 1 + 1, 1 × 1 = 1)
        log(integerBreak(10));  // expects 36. (10 = 3 + 3 + 4, 3 × 3 × 4 = 36)
    }
}