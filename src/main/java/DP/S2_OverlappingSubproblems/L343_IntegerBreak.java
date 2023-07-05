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
 *   1. DP 问题的两个关键特征：重叠子问题、最优子结构。其中“最优子结构”指一个最优策略的子策略也是必须是最优的 ∴ 通过求子问题的
 *      最优解，可以获得原问题的最优解。
 *   2. 当一个问题具有递归结构，且该结构中有重叠子问题 & 满足最优子结构性质时，即可使用 Memoization 或 DP 求解：
 *
 *                                                 ↗  Memoization（自顶向下分解）
 *                递归问题  →  重叠子问题 & 最优子结构
 *                                                 ↘  DP（自底向上递推）
 *
 *      - 其中，最优子结构保证了 DP 求最优解的正确性，而重叠子问题保证了高效性。
 *      - 若一个问题没有重叠子问题，原则上也是可以使用 DP 求解的，但这样达不到 DP 本身的提速目的。正是因为有重叠子问题，使得重复
 *        状态不需要重复计算，才体现出 DP 的优势。
 *   3. 对比 Memoization 和 DP 两种方法，从时空间效率上来说 DP 更优，但对于大部分情况，递归的消耗近乎可以忽略。
 * */

public class L343_IntegerBreak {
    /*
     * 超时解：DFS + Recursion
     * - 💎思路：与 L279_PerfectSquares 解法2极其类似，具体分析如下：
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
     *   总结一下：
     *     - 定义子问题：f(n) 表示“由正整数 n 分割得到的多个正整数的最大乘积”；
     *     - 递推表达式：f(n) = max(i*f(n-i), i*(n-i))，其中 i ∈ [1,n)。
     *
     * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
     * */
    public static int integerBreak_1(int n) {
        if (n == 1) return 1;

        int maxProd = 0;
        for (int i = 1; i <= n; i++)  // 遍历所有分割方案，并求所有方案中的最大乘积
            maxProd = maxOfN(maxProd, i * (n - i), i * integerBreak_1(n - i));

        return maxProd;
    }

    private static int maxOfN(int ...nums) {
        return Arrays.stream(nums)
            .reduce(Math::max)
            .getAsInt();  // reduce() 若有初值参数则返回 int 类型；若没有则返回 OptionalInt 类型，因此需要解包
    }

    /*
     * 解法1：DFS + Recursion + Memoization
     * - 思路：
     *   1. 从👆🏻超时解中可见，该树中存在重叠子问题 f(2) ∴ 可以使用 memoization 或 dp 的方式进行优化；
     *   2. 对4进行分支时 ∵ 4-1=3，4-3=1 ∴ f(4)=1*f(3) 和 f(4)=3*f(1) 其实是一回事 ∴ 可以提前进行剪枝，每次
     *      只遍历一遍的分支即可，即每层递归中让 i ∈ [1, n/2]。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int integerBreak(int n) {
        assert n > 1;                   // ∵ 题中要求 n 至少分要被割成两部分 ∴ 要 >1
        return dfs(n, new int[n + 1]);  // ∵ 正整数分解不会出现0 ∴ 最大乘积一定大于0 ∴ cache 初值为0即可
    }

    private static int dfs(int n, int[] cache) {
        if (n == 1) return 1;
        if (cache[n] != 0) return cache[n];

        int maxProd = 0;
        for (int i = 1; i <= n / 2; i++)  // 每次只遍历一半分支即可
            maxProd = maxOf3(maxProd, i * (n - i), i * dfs(n - i, cache));

        return cache[n] = maxProd;
    }

    private static int maxOf3(int a, int b, int c) {
        return Math.max(a, Math.max(b, c));
    }

    /*
     * 解法2：DP
     * - 思路：与 L279_PerfectSquares 解法3类似。
     * - 👉 实现：可以看做就是解法1的迭代版，将递归翻译成外层 for 循环，将递归内的 for 循环直接搬进去作为内层 for 循环即可。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int integerBreak2(int n) {
        assert n >= 2;
        int[] dp = new int[n + 1];       // cache[0] 空着不用
        dp[1] = 1;                       // 最基本问题

        for (int m = 2; m <= n; m++)
            for (int i = 1; i < m; i++)  // 用不同的 i 去分割 m
                dp[m] = maxOf3(dp[m], i * (m - i), i * dp[m - i]);

        return dp[n];                    // 最后返回最大问题的解
     }

    public static void main(String[] args) {
        log(integerBreak_1(4));   // expects 4.  (4 = 2 + 2, 2 × 2 = 4)
        log(integerBreak_1(2));   // expects 1.  (2 = 1 + 1, 1 × 1 = 1)
        log(integerBreak_1(10));  // expects 36. (10 = 3 + 3 + 4, 3 × 3 × 4 = 36)
    }
}