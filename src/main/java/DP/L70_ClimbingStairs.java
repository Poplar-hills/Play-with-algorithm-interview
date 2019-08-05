package DP;

import javafx.util.Pair;

import java.util.*;

import static Utils.Helpers.log;

/*
* Climbing Stairs
*
* - You are climbing a stair case. It takes n steps to reach to the top. Each time you can either climb 1 or 2 steps.
*   In how many distinct ways can you climb to the top? Note: n > 0.
* */

public class L70_ClimbingStairs {
    /*
    * 解法1：找规律 -> Fibonacci
    * - 思路：该问题非常类似 L279_PerfectSquares，同样可图论建模：从顶点0开始，两顶点值之间相差不超过2，求有几条到达顶点 n 的路径：
    *            1 ----> 3 ----> 5 ...
    *          ↗   ↘   ↗   ↘   ↗
    *        0 ----> 2 ----> 4 ...
    *   ∵ n > 0，可知：当 n=1 时有1条路径；n=2 时有2条路径；n=3 时有3条路径；n=4 时有5条路径... 当有n级台阶时的路径数：
    *   num(n) = num(n-1) + num(n-2)。该规律对应从第2项开始的 Fibonacci 数列（1, 2, 3, 5, 8... 而完整的 Fibonacci
    *   数列是 1, 1, 2, 3, 5, 8...）。至此此该题目转化为求第 n 个 Fibonacci 数。
    * - 实现：采用 bottom-up DP（即 Fibonacci.java 中的解法3）。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int climbStairs1(int n) {
        int[] cache = new int[n + 1];   // 或者使用 map 也可以
        cache[0] = cache[1] = 1;        // 题中说了 n > 0，但仍要给 cache[0] 赋值用以生成 Fibonacci 数列
        for (int i = 2; i <= n; i++)    // cache 中放入 fib(0), fib(1) 后再从小到大逐个计算更大的 n 值
            cache[i] = cache[i - 1] + cache[i - 2];
        return cache[n];
    }

    /*
     * 解法2：DFS
     * - 思路：解法1中对该题使用图论建模后，题目就转化成了：求图上两点之间的路径条数，若采用正统一些的解法就是 BFS 或 DFS。本解法
     *   采用 DFS，思路为：任一顶点到终点的路径条数 = sum(其所有下游相邻顶点到终点的路径条数)。按该思路使用递归求解非常自然。
     * - 实现：不管是 BFS 或 DFS，过执行程中都需要能找到任一顶点的所有相邻顶点，大体有2种方式：
     *   1. 提前创建好 graph（本解法采用这种方式）；
     *   2. 需要的时候再计算。
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
        int[] pathNums = new int[n];
        Arrays.fill(pathNums, -1);
        return dfs(graph, 0, n, pathNums);
    }

    private static int dfs(List<List<Integer>> graph, int currStep, int targetStep, int[] pathNums) {
        if (currStep == targetStep) return 1;  // 终点前的顶点需要1步到达终点
        if (pathNums[currStep] != -1) return pathNums[currStep];

        int pathNum = 0;
        for (int adj : graph.get(currStep))    // 获取所有相邻顶点
            pathNum += dfs(graph, adj, targetStep, pathNums);

        return pathNums[currStep] = pathNum;
    }

    /*
    * 解法3：解法2的优化版
    * - 思路：解法2的通用性较强，但创建 graph 的过程会增加时间复杂度，因此这里采用解法2的"实现"描述中的第2种思路：到需要的时候再计算
    *   顶点的所有相邻顶点。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int climbStairs3(int n) {
        if (n <= 0) return 0;
        int[] pathNums = new int[n];
        Arrays.fill(pathNums, -1);
        return dfs(0, n, pathNums);
    }

    private static int dfs(int curr, int target, int[] pathNums) {
        if (curr == target) return 1;
        if (pathNums[curr] != -1) return pathNums[curr];

        int pathNum = 0;
        for (int i = 1; i <= 2 && curr + i <= target; i++)  // 相邻的两个顶点的值不能相差超过2，且顶点值不能超过 n
            pathNum += dfs(curr + i, target, pathNums);

        return pathNums[curr] = pathNum;
    }

    /*
     * 超时解：BFS 全搜索（虽然超时，但是结果正确）
     * - 思路：先用 BFS 找出图上从 0 到 n 之间的所有路径，再取个数。解释 SEE: play-with-algorithms/Graph/Path 中的 allPaths 方法。
     * - TODO: 时间复杂度？？？
     * */
    public static int climbStairs4(int n) {
        List<List<Integer>> res = new ArrayList<>();

        Queue<List<Integer>> q = new LinkedList<>();
        List<Integer> initialPath = new ArrayList<>();
        initialPath.add(0);
        q.offer(initialPath);

        while (!q.isEmpty()) {
            List<Integer> path = q.poll();
            int lastVertex = path.get(path.size() - 1);
            if (lastVertex == n) {
                res.add(path);
                continue;
            }
            for (int i = 1; i <= 2 && lastVertex + i <= n; i++) {
                int adj = lastVertex + i;
                if (path.contains(adj)) continue;
                List<Integer> newPath = new ArrayList<>(path);
                newPath.add(adj);
                q.offer(newPath);
            }
        }

        return res.size();
    }

    public static void main(String[] args) {
        // log(climbStairs2(2));  // expects 2 (1+1, 2 in one go)
        // log(climbStairs2(3));  // expects 3 (1+1, 1+2, 2+1)
        log(climbStairs2(5));  // expects 8
    }
}
