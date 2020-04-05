package StackAndQueue.S4_BFSAndGraphShortestPath;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static Utils.Helpers.*;

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
     *                                     0 ← 1 ← 2               0 ← 1 ← ← ← 2
     *          0 ← 1 ← 2                  ↑   ↑   ↑               ↑   ↑     ↗ ↑
     *          ↑       ↑                  ↑   5   ↑               ↑   5 ← 6   ↑
     *          4 → → → 3                  ↑   ↓   ↑               ↑   ↓       ↑
     *                                     + ← 4 → 3               + ← 4 → → → 3
     *        n=4 时最短路径为1           n=5 时最短路径为2           n=6 时最短路径为3
     *
     * - 💎 实现：
     *   1. 求无权图中两点的最短路径可使用 BFS（若是带权图的最短路径问题则可使用 Dijkstra）；
     *   2. ∵ 在无权图上进行 BFS 时，第一次访问某个顶点时的路径一定是从起点到该顶点的最短路径 ∴ 无需重复顶点 ∴ 使用 visited
     *      数组对访问路径进行剪枝（Pruning）（例如：👆n=6 例子中，6→5→1→0 访问顶点1之后就无需再走 6→2→1→0 了）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int numSquares(int n) {
        if (n <= 0) return 0;

        Queue<Pair<Integer, Integer>> q = new LinkedList<>();  // Pair<顶点, 从起点到该顶点的步数>
        q.offer(new Pair<>(n, 0));                             // 顶点 n 作为 BFS 的起点
        boolean[] visited = new boolean[n + 1];                // ∵ 顶点取值范围是 [0,n] ∴ 要开 n+1 的空间
        visited[n] = true;

        while (!q.isEmpty()) {
            Pair<Integer, Integer> pair = q.poll();
            int curr = pair.getKey();
            int step = pair.getValue();

            for (int i = 1; i * i <= curr; i++) {  // 当前顶点值 - 每一个完全平方数 = 每一个相邻顶点
                int next = curr - i * i;
                if (next == 0) return step + 1;    // BFS 中第一条到达终点的路径就是最短路径（之一）
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
     * - 思路：沿用解法1中的图论建模思路。
     * - 实现：采用 DFS，即从顶点 n 开始向0顶点方向递归查找 ∵ 前一顶点到达0的最少步数 = 后一顶点到达0的最少步数 + 1，且后一
     *   顶点值与前一顶点值之间差一个完全平方数 ∴ 有 f(i) = min(f(i - p) + 1)，其中 p 为 <= i 的完全平方数。
     * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
     * */
    public static int numSquares0(int n) {
        if (n == 0) return 0;             // 顶点0到达自己的步数为0

        int minStep = Integer.MAX_VALUE;  // 用于记录当前顶点到0的最小步数
        for (int i = 1; i * i <= n; i++)  // 找到当前顶点的相邻顶点
            minStep = Math.min(minStep, numSquares(n - i * i) + 1);  // 计算所有相邻顶点到0的最少步数，其中最小值+1即是当前顶点到0的最少步数

        return minStep;
    }

    /*
     * 解法2：Recursion + Memoization（DFS with cache）
     * - 思路：👆超时解的问题在于大量子问题会被重复计算 ∴ 加入 Memoization 机制来优化重叠子问题。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int numSquares2(int n) {
        int[] steps = new int[n + 1];         // 保存每个顶点到达0的最少步数（∵ 顶点取值范围是 [0,n] ∴ 要开 n+1 的空间）
        Arrays.fill(steps, -1);
        return helper2(n, steps);
    }

    private static int helper2(int n, int[] steps) {
        if (n == 0) return 0;
        if (steps[n] != -1) return steps[n];  // cache hit

        int minStep = Integer.MAX_VALUE;
        for (int i = 1; i * i <= n; i++)
            minStep = Math.min(minStep, helper2(n - i * i, steps) + 1);

        return steps[n] = minStep;            // 赋值语句的返回值为所赋的值
    }

    /*
     * 解法3：DP
     * - 思路：与解法2都是基于已解决的子问题去解决高层次的问题，不同点在于 DP 是 bottom-up 的，即直接从子问题开始求解，而解法2是
     *   先从高层次问题入手，递归到最基本问题后再开始往上逐层解决。
     * - DP vs. Memoization, SEE: https://zhuanlan.zhihu.com/p/68059061。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int numSquares3(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);  // ∵ 求最小值 ∴ 初值为正无穷
        dp[0] = 0;

        for (int i = 1; i <= n; i++)         // 从 1→n 逐个计算到达0的最少步数，层层递推出原问题 f(n) 的解
            for (int j = 1; j * j <= i; j++)
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);

        return dp[n];
    }

    public static void main(String[] args) {
        log(numSquares00(5));   // expects 2. (5 = 4 + 1)
        log(numSquares00(6));   // expects 3. (6 = 4 + 1 + 1)
        log(numSquares00(12));  // expects 3. (12 = 4 + 4 + 4)
        log(numSquares00(13));  // expects 2. (13 = 4 + 9)
    }
}
