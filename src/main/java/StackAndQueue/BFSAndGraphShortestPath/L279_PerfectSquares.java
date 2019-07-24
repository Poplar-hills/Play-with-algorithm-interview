package StackAndQueue.BFSAndGraphShortestPath;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static Utils.Helpers.log;

/*
* Perfect Squares
*
* - Given a positive integer n, find the least number of perfect square numbers (for example, 1, 4, 9, 16, ...) which sum to n.
* */

public class L279_PerfectSquares {
    /*
    * 解法1：BFS（借助 Queue 实现）
    * - 思路："正整数 n 最少能用几个完全平方数组成"，这个问题可以转化为"正整数 n 最少减去几个完全平方数后等于0"。如果把 n 和 0 看成
    *   图上的两个顶点，把"减去一个完全平方数"看做两点间的一段路径，则该问题即可转化为"求顶点 n 到 0 之间的最短路径"，即将原问题转化
    *   为了一个图论问题，重新描述一遍就是：从 n 到 0，每个数字表示一个顶点；若两顶点之间相差一个完全平方数，则链接两顶点，由此得到
    *   一个有向无权图（方向都是从大顶点指向小顶点）；该问题转化为了求该无权图中从 n 到 0 的最短路径的步数。例如：
    *                                    0 -- 1 -- 2                 0 -- 1 ---- 2
    *          0 -- 1 -- 2               |    |    |                 |    |    / |
    *          |         |               |    5    |                 |    5 - 6  |
    *          4 ------- 3               |    |    |                 |    |      |
    *      n = 4 时最短路径为1步           +--- 4 -- 3                 +--- 4 ---- 3
    *                                n = 5 时最短路径为2步          n = 6 时最短路径为3步
    * - 实现过程：求无权图中两点的最短路径可使用广度优先遍历（BFS）实现（有权图不适用）：
    *   - 使用队列作为辅助数据结构，即从起点开始，入队相邻顶点、访问出队的顶点，再将其相邻顶点入队，直到到达终点为止。
    *   - ∵ 最终要返回找到的最短路径的步数，因此队列中除了保存顶点之外还要保存步数信息。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int numSquares(int n) {
        if (n <= 0) return 0;
        Queue<Pair<Integer, Integer>> q = new LinkedList<>();  // Pair<顶点, 从起点到该顶点所走过的步数>
        q.offer(new Pair<>(n, 0));                 // 顶点 n 作为 BFS 的起点

        boolean[] visited = new boolean[n + 1];    // 记录某个顶点是否已经计算过（n+1 是因为从 n 到 0 需要开 n+1 的空间）
        visited[n] = true;

        while (!q.isEmpty()) {
            Pair<Integer, Integer> pair = q.poll();
            int num = pair.getKey();
            int step = pair.getValue();

            for (int i = 1; num - i * i >= 0; i++) {  // 尝试用当前顶点值 - 每一个完全平方数，得到不同的下一步顶点（相当于 BFS 中找到所有相邻顶点）
                int next = num - i * i;
                if (next == 0) return step + 1;       // 若下一步就是终点则返回该路径的步数（第一条到达终点的路径就是最短路径，直接 return）
                if (!visited[next]) {                 // 已访问过顶点不入队
                    q.offer(new Pair<>(next, step + 1));  // 将下一步的顶点入队，其距离起点的步数 = 当前顶点距离起点的步数 + 1
                    visited[next] = true;
                }
            }
        }
        throw new IllegalStateException("No Solution.");  // 只要输入参数正确则不会到达这行 ∵ 所有正整数最终都可以用多个1相加得到
    }

    /*
    * 解法2：DFS（借助 buckets 数组实现）
    * - 思路：基于解法1中的图论建模思路，在具体实现时采用 DFS（SEE: Play-with-algorithms/Graph/Path.java)。具体来说是通过 DFS
    *   从0开始往起点方向递归计算每个顶点到达0的最少步数。
    * - 本质：该解法实际上也是一种 DP，只是实现方式是 top-down 的（因为采用了递归），以及其中对于 overlap sub-problem 的优化策略
    *   采用的是 Memoization，而非 bottom-up DP 中的 Tabulation。SEE: https://zhuanlan.zhihu.com/p/68059061
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int numSquares2(int n) {
        int[] steps = new int[n + 1];  // 保存每个顶点到达0的最短路径的步数（n+1 是因为从 n 到 0 需要开 n+1 的空间）
        Arrays.fill(steps, -1);
        return numSquares2(n, steps);
    }

    private static int numSquares2(int n, int[] steps) {
        if (n == 0) return 0;                 // 顶点0到达自己的步数为0
        if (steps[n] != -1) return steps[n];  // 计算过的顶点直接返回（以免重复计算）

        int minStep = Integer.MAX_VALUE;      // 记录某一顶点到0的最短路径的步数
        for (int i = 1; n - i * i >= 0; i++)  // 找到当前顶点的相邻顶点
            minStep = Math.min(minStep, numSquares2(n - i * i, steps) + 1);  // 不同的相邻顶点对应了不同的到达0的路径，其中最短路径的步数+1即是当前顶点 n 的 step

        return steps[n] = minStep;            // 赋值语句的返回值为所赋的值
    }

    /*
    * 解法3：Dynamic Programming (bottom-up)
    * - 思路：
    *   - 类似 DP/Fibonacci 中解法3的自下而上的求解思路。
    *   - 与解法2的不同之处在于，bottom-up DP 的过程需要保证在解决 big problem 之前先解决 sub-problems，对应到该解中就是在
    *     计算较大的 n 之前需要先计算好较小的 n。较大的 n 的解是在较小的 n 的解的基础上得到的。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int numSquares3(int n) {
        int[] steps = new int[n + 1];
        Arrays.fill(steps, Integer.MAX_VALUE);
        steps[0] = 0;

        for (int i = 1; i <= n; i++)  // 使用双重循环从小到大为 1~n 间的每一个数字计算到达0的最小步数
            for (int j = 1; i - j * j >= 0; j++)
                steps[i] = Math.min(steps[i], steps[i - j * j] + 1);

        return steps[n];
    }

    public static void main(String[] args) {
        log(numSquares(5));   // expects 2. (5 = 4 + 1)
        log(numSquares(6));   // expects 3. (6 = 4 + 1 + 1)
        log(numSquares(12));  // expects 3. (12 = 4 + 4 + 4)
        log(numSquares(13));  // expects 2. (13 = 4 + 9)
    }
}
