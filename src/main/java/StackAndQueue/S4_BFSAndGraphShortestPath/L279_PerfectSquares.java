package StackAndQueue.S4_BFSAndGraphShortestPath;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static Utils.Helpers.Pair;
import static Utils.Helpers.log;

/*
 * Perfect Squares
 *
 * - Given a positive integer n, find the least number of perfect square numbers (for example, 1, 4, 9, 16, ...)
 *   which sum to n.
 * */

public class L279_PerfectSquares {
    /*
     * 解法1：BFS（借助 Queue 实现）
     * - 思路："正整数 n 最少能用几个完全平方数组成"，这个问题可以转化为"正整数 n 最少减去几个完全平方数后等于0"。若把 n、0
     *   看成图上的两个顶点，把"减去一个完全平方数"看做两点间的一条边，则该问题可转化为"求顶点 n 到 0 之间的最短路径"，即将原
     *   问题转化为了一个在有向无权图上寻找 n → 0 间最短路径的问题。例如：
     *                                0 ← ← 1 ← ← 2            0 ← ← 1 ← ←  2
     *           0 ← 1 ← 2            ↑     ↑     ↑            ↑     ↑    ↗ ↑
     *           ↑       ↑            ↑     5     ↑            ↑     5 ← 6  ↑
     *           4 → → → 3            ↑     ↓     ↑            ↑     ↓      ↑
     *                                + ← ← 4 → → 3            + ← ← 4 → →  4
     *        n=4 时最短路径为1       n=5 时最短路径为2          n=6 时最短路径为3
     * - 💎 要点：
     *   1. 求无权图中两点的最短路径可使用 BFS（若是带权图的最短路径问题则可使用 Dijkstra）；
     *   2. 无权图上进行的 BFS 时，第一次访问某个顶点时的路径一定是从起点到该顶点的最短路径（当然可能存在多个最短路径，
     *      如👆n=6 的例子中 6 → 5 → 1 → 0 和 6 → 2 → 1 → 0 都是最短路径）。
     *   3. 注意避免重复访问顶点，如 6 → 5 → 1 和 6 → 2 → 1，若没有 visited 数组，则顶点1会被入队、处理两遍。
     * - 时间复杂度 O(n * sqrt(n))，空间复杂度 O(n)。
     * */
    public static int numSquares(int n) {
        if (n <= 0) return 0;

        Queue<Pair<Integer, Integer>> q = new LinkedList<>();  // Pair<顶点, 从起点到该顶点的步数>
        q.offer(new Pair<>(n, 0));                             // 顶点 n 作为 BFS 的起点
        boolean[] visited = new boolean[n + 1];                // ∵ 顶点取值范围是 [0,n] ∴ 要开 n+1 的空间
        visited[n] = true;

        while (!q.isEmpty()) {
            Pair<Integer, Integer> p = q.poll();
            int curr = p.getKey();
            int step = p.getValue();

            for (int i = 1; i * i <= curr; i++) {  // 当前顶点值 - 每一个完全平方数 = 每一个相邻顶点
                int next = curr - i * i;
                if (next == 0) return step + 1;    // BFS 中第一条到达终点的路径就是最短路径
                if (!visited[next]) {
                    q.offer(new Pair<>(next, step + 1));
                    visited[next] = true;
                }
            }
        }

        throw new IllegalStateException("No Solution.");  // 只要 n 有效就不会走到这里 ∵ 所有正整数都可以用 n 个1相加得到
    }

    /*
     * 超时解：DFS
     * - 💎 思路：即可以沿用解法1中的图论建模思路，也可以将该问题看做 top-down 的逐层分解（其实两种思路可互相转化）：
     *                        6
     *                  1/        4\
     *                 5            2
     *              1/   4\        1|
     *              4     1         1
     *           1/  4\  1|        1|
     *           3    0   0         0       - 得到解 3 (1+1+4)
     *          1|
     *           2
     *          1|
     *           1
     *          1|
     *           0                          - 得到解 6 (1+1+1+1+1+1)
     *
     * - 实现：采用 DFS，从图论的视角看的话就是从顶点 n 开始向0顶点方向递归查找 ∵ 前一顶点到达0的最少步数 = 后一顶点到达0的最少
     *   步数 + 1，且后一顶点值与前一顶点值之间差一个完全平方数 ∴ 有 f(i) = min(f(i - p) + 1)，其中 p 为 <= i 的完全平方数。
     * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
     * */
    public static int numSquares_1(int n) {
        if (n <= 1) return n;             // n=0/1 时，到达0的步数分别为0/1

        int minStep = Integer.MAX_VALUE;  // 用于记录当前顶点到0的最小步数
        for (int i = 1; i * i <= n; i++)  // 找到当前顶点的相邻顶点
            minStep = Math.min(minStep, numSquares_1(n - i * i) + 1);  // 计算所有相邻顶点到0的最少步数，其中最小值+1即是当前顶点到0的最少步数

        return minStep;
    }

    /*
     * 解法2：Recursion + Memoization（DFS with cache）
     * - 思路：👆超时解的问题在于大量子问题会被重复计算 ∴ 加入 Memoization 机制来优化重叠子问题。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int numSquares2(int n) {
        int[] minSteps = new int[n + 1];  // minSteps[i] 保存顶点 i 到0的最少步数（∵ 顶点取值范围是 [0,n] ∴ 要开 n+1 的空间）
        Arrays.fill(minSteps, -1);
        minSteps[0] = 0;
        minSteps[1] = 1;
        return helper2(n, minSteps);
    }

    private static int helper2(int n, int[] minSteps) {
        if (minSteps[n] != -1) return minSteps[n];  // cache hit

        int minStep = Integer.MAX_VALUE;
        for (int i = 1; i * i <= n; i++)
            minStep = Math.min(minStep, helper2(n - i * i, minSteps) + 1);

        return minSteps[n] = minStep;  // 赋值语句的返回值为所赋的值
    }

    /*
     * 解法3：DP
     * - 思路：与解法2一样都是基于已解决的子问题去解决高层次的问题。状态转移方程都是：f(i) = min(f(i - p) + 1)，
     *   其中 p 为 <= i 的完全平方数。
     * - 实现：采用 DP 从下往上 f(0) → f(1) → ... 递归出 f(n) 的解，例如 n=6 时：
     *         +---------------------+
     *         |           ⑥        ③     - dp[6] = min(dp[5]+1, dp[2]+1); dp[3] = dp[2]+1
     *         |        1↗   4↖   1↗
     *         |      ⑤        ②          - dp[5] = min(dp[4]+1, dp[1]+1); dp[2] = dp[1]+1
     *          ↘  1↗   4↖   1↗
     *           ④        ①               - dp[1] = dp[0]+1; dp[4] = min(dp[0]+1, dp[3]+1)
     *             4↖   1↗
     *                ⓪                    - dp[0] = 0
     * - 💎 DP vs. Memoization：DP 是 bottom-up 的，从子问题开始求解，而解法2的 DFS + Memorization 是 top-down，
     *   即先从高层次问题入手，递归到最基本问题后再开始往上逐层解决（SEE: https://zhuanlan.zhihu.com/p/68059061）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int numSquares3(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);    // ∵ 求每个位置上的最小值 ∴ 初值为正无穷
        dp[0] = 0;                            // 也可以增加一行 dp[1]=1，然后下面从 i=2 开始 DP

        for (int i = 1; i <= n; i++)          // 从 1→n 逐个计算到达0的最少步数，从下往上层层递推出原问题 f(n) 的解
            for (int j = 1; j * j <= i; j++)  // 对于每个 i 找到不大于它的完全平方数 p（即 j*j）
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);

        return dp[n];
    }

    public static void main(String[] args) {
        log(numSquares2(5));   // expects 2. (5 = 4 + 1)
        log(numSquares2(6));   // expects 3. (6 = 4 + 1 + 1)
        log(numSquares2(12));  // expects 3. (12 = 4 + 4 + 4)
        log(numSquares2(13));  // expects 2. (13 = 4 + 9)
    }
}
