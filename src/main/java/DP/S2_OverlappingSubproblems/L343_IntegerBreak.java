package DP.S2_OverlappingSubproblems;

import static Utils.Helpers.log;

import java.util.Arrays;

/*
 * Integer Break
 *
 * - Given a positive integer n, break it into the sum of at least two positive integers and maximize the
 *   product of those integers. Return the maximum product you can get.
 *
 * - ⭐ 总结：
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
     * 超时解：DFS + Recursion
     * - 思路：与 L279_PerfectSquares 解法2极其类似，具体分析如下：
     *     1. 要使用 DFS + 递归求解就意味着子问题定义要与原问题一致：f(i) 表示“由正整数 i 分割得到的多个正整数的最大乘积”；
     *     2. DFS + 递归的关键在于找到前后子问题之间的关系，从而写出递推表达式。
     *   ∵ 需要将 n 分割成几份是未知的 ∴ 很难使用循环解决（不知道需要几重循环），需要使用递归解决（只要设置好终止条件，其余的就
     *   交给递归即可）∴ 要对 n 进行递归分割（动画讲解 SEE: https://coding.imooc.com/lesson/82.html#mid=2953，2'38''）：
     *                    f(4)
     *             1/      2|      3\       - 4有三种分割方式
     *           f(3)     f(2)     f(1)     - 1不能再分割，即分割到 f(1) 时就分割到底了
     *          1/  2\     1|               - 3有两种分割方式；2只有一种
     *        f(2)  f(1)  f(1)
     *         1|
     *        f(1)
     *   根据上图可知：
     *     1. 前后子问题之间的关系：f(4) = max(1*f(3), 2*f(2), 3*f(1))；
     *     2. 其中 ∵ f(1) 不能再分割 ∴ 作为递归退出条件，让 f(1)=1；
     *     3. 在分割时有个陷阱：当用2分割 f(4) 时，除了 2*f(2) 这个解之外，还要考虑 2*2 这个解，即 f(4) 的完整表达式应该是：
     *        f(4) = max(1*f(3), 1*3, 2*f(2), 2*2, 3*f(1), 3*1)；
     *     4. 可见该树中存在重叠子问题 f(2) ∴ 可以使用 memoization 或 dp 的方式进行优化。
     *   总结一下：
     *     - 定义子问题：f(n) 表示“由正整数 n 分割得到的多个正整数的最大乘积”；
     *     - 状态转移方程：f(n) = max(i*f(n-i), i*(n-i))，其中 i ∈ [1,n)。
     *
     * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
     * */
    public static int integerBreak(int n) {
        if (n == 1) return 1;

        int maxProduct = 0;
        for (int i = 1; i < n; i++) {    // 遍历所有分割方案
            maxProduct = maxOf(maxProduct, i * integerBreak(n-i), i * (n-i));  // 求所有方案中的最大乘积
        }

        return maxProduct;
    }

    private static int maxOf(int ...nums) {
        return Arrays.stream(nums)
            .reduce(Math::max)
            .getAsInt();  // reduce() 若有初值参数则返回 int 类型；若没有则返回 OptionalInt 类型，因此需要解包
    }

    /*
     * 解法1：DFS + Recursion + Memoization
     * - 思路：在超时解的基础上加入 Memoization 优化。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int integerBreak1(int n) {
        assert n >= 2;                      // ∵ 题中要求 n 至少分要被割成两部分 ∴ 要 >= 2
        return helper1(n, new int[n + 1]);  // ∵ 正整数分解不会出现0 ∴ 最大乘积一定大于0 ∴ cache 初值为0即可
    }

    private static int helper1(int n, int[] cache) {
        if (n == 1) return 1;
        if (cache[n] != 0) return cache[n];

        int res = 0;
        for (int j = 1; j < n; j++)
            res = maxOf3(res, j*(n-j), j*helper1(n-j, cache));

        return cache[n] = res;
    }

    private static int maxOf3(int a, int b, int c) {
        return Math.max(a, Math.max(b, c));
    }

    /*
     * 解法2：DP
     * - 思路：与 L279_PerfectSquares 解法3类似。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int integerBreak2(int n) {
        assert n >= 2;
        int[] cache = new int[n + 1];    // cache[0] 空着不用
        cache[1] = 1;                    // 最基本问题

        for (int i = 2; i <= n; i++)
            for (int j = 1; j < i; j++)  // 将 i 分割成 j 和 i-j
                cache[i] = maxOf3(cache[i], j*(i-j), j*cache[i-j]);  // 此时 cache[i-j] 已经被计算过了

        return cache[n];                 // 最后返回最大问题的解
    }

    public static void main(String[] args) {
        log(integerBreak(4));   // expects 4.  (4 = 2 + 2, 2 × 2 = 4)
        log(integerBreak(2));   // expects 1.  (2 = 1 + 1, 1 × 1 = 1)
        log(integerBreak(10));  // expects 36. (10 = 3 + 3 + 4, 3 × 3 × 4 = 36)
    }
}