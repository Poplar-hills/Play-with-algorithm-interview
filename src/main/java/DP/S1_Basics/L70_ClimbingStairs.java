package DP.S1_Basics;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Climbing Stairs
 *
 * - You are climbing a stair case. It takes n steps to reach to the top. Each time you can either climb
 *   1 or 2 steps. In how many distinct ways can you climb to the top? Note: n > 0.
 * */

public class L70_ClimbingStairs {
    /*
     * 超时解1：BFS
     * - 思路：该题是个图搜索问题 ∴ 可采用最简单的 BFS 搜索求解。
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
     * */
    public static int climbStairs0(int n) {
        int numOfPath = 0;
        if (n <= 0) return numOfPath;

        Queue<Integer> q = new LinkedList<>();
        q.offer(0);

        while (!q.isEmpty()) {
            int step = q.poll();

            if (step == n) {
                numOfPath++;
                continue;
            }

            if (step + 1 <= n) q.offer(step + 1);
            if (step + 2 <= n) q.offer(step + 2);
        }

        return numOfPath;
    }

    /*
     * 超时解2：BFS + 记录所有路径
     * - 思路：另一种方式是用 BFS 找出图上从 0 → n 之间的所有路径，再取其个数（SEE: Play-with-algorithms/Graph/Path 中的
     *   allPaths 方法）。
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)（解释 SEE: L120 超时解1）。
     * */
    public static int climbStairs00(int n) {
        List<List<Integer>> paths = new ArrayList<>();
        Queue<List<Integer>> q = new LinkedList<>();  // q 存储所有从起点出发的路径，每个分支都会形成一条新路径

        List<Integer> initialPath = new ArrayList<>();
        initialPath.add(0);
        q.offer(initialPath);

        while (!q.isEmpty()) {
            List<Integer> path = q.poll();             // 每次拿出一条路径
            int lastStep = path.get(path.size() - 1);  // 获取路径中的最后一个顶点

            if (lastStep == n) {  // 若该顶点就是 n 则说明该是一条有效路径，放入 paths 中
                paths.add(path);
                continue;
            }

            for (int i = 1; i <= 2 && lastStep + i <= n; i++) {  // 获取所有相邻顶点
                int nextStep = lastStep + i;
                List<Integer> newPath = new ArrayList<>(path);  // 复制该路径并添加节点，形成一条新路径，并放入 q 中
                newPath.add(nextStep);
                q.offer(newPath);
            }
        }

        return paths.size();
    }

    /*
     * 解法1：找规律 -> Fibonacci
     * - 思路：该问题非常类似 L279_PerfectSquares，同样可图论建模：从顶点0开始，两顶点值之间相差不超过2，求有几条到达顶点 n 的路径：
     *            1 ----> 3 ----> 5 ...
     *          ↗   ↘   ↗   ↘   ↗
     *        0 ----> 2 ----> 4 ...
     *   ∵ n > 0，可知：当 n=1 时有1条路径；n=2 时有2条路径；n=3 时有3条路径；n=4 时有5条路径... 当有n级台阶时的路径数：
     *   f(n) = f(n-1) + f(n-2)。该规律对应从第2项开始的 Fibonacci 数列 1, 2, 3, 5, 8...（完整的 Fibonacci 数列是
     *   1, 1, 2, 3, 5, 8...）。至此此该题目转化为求第 n 个 Fibonacci 数。
     * - 实现：采用 DP（即 L509 中的解法3；类似 L91 中的解法2）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int climbStairs1(int n) {
        int[] cache = new int[n + 1];   // 或者使用 map 也可以
        cache[0] = cache[1] = 1;        // 题中说了 n > 0，但仍要给 cache[0] 赋值用以生成 Fibonacci 数列
        for (int i = 2; i <= n; i++)    // 解决最基本问题 f(0), f(1) 后再递推出 f(n)
            cache[i] = cache[i - 1] + cache[i - 2];
        return cache[n];
    }

    /*
     * 解法2：DFS
     * - 思路：解法1中对该题使用图论建模后，题目就转化成了：求图上两点之间的路径条数，若采用正统一些的解法就是 BFS 或 DFS。本解法
     *   采用 DFS，即任一顶点到终点的路径数 = sum(其所有下游相邻顶点到终点的路径条数)，比如上图中，2->5的路径数 = 3->5的路径数
     *   + 4->5的路径数。
     * - 实现：
     *   - 按该思路使用递归求解非常自然；
     *   - 不管是 BFS 或 DFS，过执行程中都需要能找到任一顶点的所有相邻顶点，大体有2种方式：
     *     1. 提前创建好 graph（本解法采用这种方式）；
     *     2. 需要的时候再计算。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int climbStairs2(int n) {
        if (n <= 0) return 0;

        // 提前创建好 graph（邻接表，如解法1思路中的图）
        List<List<Integer>> graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());

        for (int i = 0; i < n; i++) {
            if (i + 1 <= n) graph.get(i).add(i + 1);
            if (i + 2 <= n) graph.get(i).add(i + 2);
        }

        // 开始 DFS
        int[] cache = new int[n];
        Arrays.fill(cache, -1);
        return dfs(graph, 0, n, cache);
    }

    private static int dfs(List<List<Integer>> graph, int i, int n, int[] cache) {
        if (i == n) return 1;  // 终点前的顶点需要1步到达终点
        if (cache[i] != -1) return cache[i];

        int pathNum = 0;
        for (int adj : graph.get(i))    // 获取所有相邻顶点
            pathNum += dfs(graph, adj, n, cache);

        return cache[i] = pathNum;
    }

    /*
     * 解法3：解法2的优化版，即 Recursion + Memoization（类似 L91 的解法1）
     * - 思路：解法2的通用性较强，但创建 graph 的过程会增加时间复杂度，因此这里采用解法2的"实现"描述中的第2种思路：到需要的时候再
     *   计算顶点的所有相邻顶点。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int climbStairs3(int n) {
        if (n <= 0) return 0;
        int[] cache = new int[n];
        Arrays.fill(cache, -1);
        return dfs(0, n, cache);
    }

    private static int dfs(int i, int n, int[] cache) {
        if (i == n) return 1;
        if (cache[i] != -1) return cache[i];

        int pathNum = 0;
        for (int j = 1; j <= 2 && i + j <= n; j++)  // 相邻的两个顶点的值不能相差超过2，且顶点值不能超过 n
            pathNum += dfs(i + j, n, cache);

        return cache[i] = pathNum;
    }

    public static void main(String[] args) {
        log(climbStairs00(2));  // expects 2 (1+1, 2 in one go)
        log(climbStairs00(3));  // expects 3 (1+1, 1+2, 2+1)
        log(climbStairs00(5));  // expects 8
    }
}
