package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* 0/1 Knapsack Problem
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
*     1. 首先状态可以通过题中的约束条件来识别：
*        1. 每次从 i 个物品中找出最佳价值组合（i 是变量，每次放/不放一个物品后 i 都会-1）；
*        2. 背包的剩余容量 c（每次放入一个物品后 c 都会减小）；
*        这两个约束条件是该问题中的变量，不同的变量组合就意味着该问题的不同状态，因此这两个变量也就是状态转移方程的输入参数。
*     2. 定义子问题，即确定状态转移方程中的函数定义 —— f(i, c) 表示“当背包剩余容量为 c 时，从前 i 个物品中能得到的最大价值”。
*     3. 推导状态转移方程：
*        - ∵ 对每种物品都有放/不放两种选择 ∴ f 所求的“最大价值”一定来自于其中价值更大的那个选择；
*          - 若不放物品 i，则最大价值为 f(i-1, c)，即从前 i-1 个物品中能得到的最大价值；
*          - 若放入物品 i，则最大价值为 v(i) + f(i-1, c-w(i))，即当前物品价值 + 往剩余容量中尝试放入前 i-1 个物品能得到的最大价值；
*        - 完整方程为 f(i, c) = max(f(i-1, c), v(i) + f(i-1, c-w(i)))。
*     4. 在分别计算两种选择的过程中会有重叠子问题，因此可以用 Memoization、DP 的方式进行优化。
*
* ⭐ 心得：DP 里最关键的问题是寻找原问题的子问题，并写出状态转移方程（即递推表达式），之后的部分都是水到渠成的事情了。
*
* - 详解 SEE: 微信搜“【动态规划】一次搞定三种背包问题”。
* */

public class ZeroOneKnapsack {
    /*
    * 解法1：Recursion + Memoization
    * - 思路：top-down 方式。
    * - 时间复杂度 O(n*c)，即填表的耗时；空间复杂度 O(n*c)。
    * */
    public static int knapsack(int[] w, int[] v, int c) {
        int n = w.length;
        int[][] cache = new int[n][c + 1];  // ∵ 状态转移方程有两个输入变量 ∴ 缓存也是二维的（n 行 c+1 列；c 的取值范围是 [0,c]）
        for (int[] row : cache)
            Arrays.fill(row, -1);           // ∵ 要缓存的值可能有0 ∴ 都初始化为-1
        return largestValue(n - 1, w, v, c, cache);
    }

    private static int largestValue(int i, int[] w, int[] v, int c, int[][] cache) {
        if (i < 0 || c == 0) return 0;
        if (cache[i][c] != -1) return cache[i][c];

        int res = largestValue(i - 1, w, v, c, cache);  // 不放入第 i 个的最大价值
        if (c >= w[i])                                  // 若剩余容量充足则可以尝试放入第 i 个
            res = Math.max(res, v[i] + largestValue(i - 1, w, v, c - w[i], cache));

        return cache[i][c] = res;
    }

    /*
    * 解法2：DP
    * - 思路：bottom-up 方式直接 DP：
    *   1. ∵ 该问题中有两个变量 ∴ 状态转移过程可以通过填表发（tabulation）来可视化。动画演示 SEE:
    *      https://coding.imooc.com/lesson/82.html#mid=2955 (18'15'')。
    *   2. ∵ 解法1中要解决问题 f(i, ..)，需要先解决问题 f(i-1, ..)，需要先解决 f(i-2, ..)，... 直到 f(0, ..)，因此 i=0
    *      的情况是该题的最基本问题。
    * - 时间复杂度 O(n*c)，空间复杂度 O(n*c)。
    * */
    public static int knapsack2(int[] w, int[] v, int c) {
        int n = w.length;
        if (n == 0) return 0;

        int[][] cache = new int[n][c + 1];
        for (int[] row : cache)
            Arrays.fill(row, -1);

        for (int j = 0; j <= c; j++)  // 先解决最基础的问题（即表的第0行 —— 只考虑0号物品时在不同容量下能得到的最大价值）
            cache[0][j] = w[0] <= j ? v[0] : 0;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= c; j++) {
                cache[i][j] = cache[i - 1][j];  // cache[i][j] 表示在剩余容量为 j 时从 0-i 号物品中所能得到的最大价值
                if (j >= w[i])
                    cache[i][j] = Math.max(cache[i][j], v[i] + cache[i - 1][j - w[i]]);
            }
        }

        return cache[n - 1][c];
    }

    /*
    * 解法3：解法2的空间优化版
    * - 思路：观察解法2中的状态转移方程：f(i, c) = max(f(i-1, c), v(i) + f(i-1, c-w(i)))，可发现 f(i,..) 只与 f(i-1,..)
    *   有关，即填表法中除第0行之外的每一行中的值都能通过上一行中的值求得，而与其他行无关。因此：
    *   1. 不需要缓存整个二维表，只需缓存第 i 行和第 i-1 行这2行即可；
    *   2. 在从上到下逐行计算时，交替使用这2行缓存，例如：当缓存了第0、1两行，需要计算第2行时，已经不再需要第0行的值，因此可用第2行
    *      的计算结果覆盖第0行值；当需要计算第3行时，不再需要第1行的值，因此可用第3行的计算结果覆盖第1行的值…… 这个过程中的规律是：
    *      若需要计算的是偶数行则覆盖缓存第0行；若是奇数行则覆盖缓存第1行。
    *   这样优化之后，空间复杂度从 O(n*c) 降低到了 O(2c)，从而能计算的背包容量大大增加。
    * - 时间复杂度 O(n*c)，空间复杂度 O(c)。
    * */
    public static int knapsack3(int[] w, int[] v, int c) {
        int n = w.length;
        if (n == 0) return 0;

        int[][] cache = new int[2][c + 1];
        Arrays.fill(cache[1], -1);    // 第0行在后面初始化了

        for (int j = 0; j <= c; j++)
            cache[0][j] = w[0] <= j ? v[0] : 0;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= c; j++) {
                cache[i % 2][j] = cache[(i-1) % 2][j];  // i 为偶则写第0行，读第1行；i 为奇则写第1行，读第0行
                if (j >= w[i])
                    cache[i % 2][j] = Math.max(cache[i % 2][j], v[i] + cache[(i-1) % 2][j - w[i]]);
            }
        }

        return cache[(n-1) % 2][c];
    }

    public static void main(String[] args) {
        log(knapsack3(new int[]{1, 2, 3}, new int[]{6, 10, 12}, 5));       // expects 22.
        log(knapsack3(new int[]{1, 3, 4, 2}, new int[]{3, 9, 12, 8}, 5));  // expects 17.
    }
}
