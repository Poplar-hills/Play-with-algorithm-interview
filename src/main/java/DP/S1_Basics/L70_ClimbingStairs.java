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
     * - 实现：若用 Stack 代替 Queue，则是 DFS。
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
     * 超时解3：DFS + Recursion
     * - 思路：若使用递归求解，那么就需要思考前后子问题之间的递推关系，即 f(i) 与 f(i+1) 之间如何进行递推。对于 n=5 来说：
     *            1   →   3   →   5
     *          ↗   ↘   ↗   ↘   ↗
     *        0   →   2   →   4
     *   其中 f(4)=1, f(3)=2, f(2)=3 ∴ 有 f(2) = f(3) + f(4)，且该递推关系也适用于其他情况 ∴ 找到递推表达式：
     *   f(i) = f(i + 1) + f(i + 2)，其中 i ∈ [0,n-2] ∴ 可按该递推表达式设计递归程序。
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
     * */
    public static int climbStairs000(int n) {
        if (n <= 0) return 0;
        return helper000(0, n);
    }

    private static int helper000(int i, int n) {
        if (i == n) return 1;
        int numOfPath = helper000(i + 1, n);
        if (i + 2 <= n) numOfPath += helper000(i + 2, n);
        return numOfPath;
    }

    /*
     * 超时解4：DFS（超时解3的简化版）
     * - 思路：与超时解3一致。
     * - 实现：1. 去掉超时解3中的 i，直接让 n 逼近 0，即递推表达式可以改写为 f(n) = f(n-1) + f(n-2)。
     *        2. 当 n < 0 时返回0（对应到超时解3中就是当 i > n 时返回0，这样就无需 if (i+2 <= n) 的判断了，代码更简洁）。
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
     * */
    public static int climbStairs0000(int n) {
        if (n == 0) return 1;    // 这里与其他解的表现略微不同，n=0 时会返回1，但 ∵ 题中规定 n>0 ∴ 没关系
        if (n < 0) return 0;
        return climbStairs0000(n - 1) + climbStairs0000(n - 2);
    }

    /*
     * 解法1：DP
     * - 思路：其实超时解4中的递推表达式就是 L509_FibonacciNumber 中的递推表达式，相当于该题就是在求第 n 个 Fibonacci 数。
     * - 实现：在超时解4的基础上加入 L509 解法1的 Memoization 进行优化。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int climbStairs1(int n) {
        int[] cache = new int[n + 1];
        return helper1(n, cache);
    }

    private static int helper1(int n, int[] cache) {
        if (n == 0) return 1;
        if (n < 0) return 0;
        if (cache[n] != 0) return cache[n];
        return cache[n] = helper1(n - 1, cache) + helper1(n - 2, cache);
    }

    /*
     * 解法2：DP
     * - 思路：采用 L509 解法3的 DP 思路（类似 L91_DecodeWays 解法2）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int climbStairs2(int n) {
        int[] dp = new int[n + 1];
        dp[0] = dp[1] = 1;

        for (int i = 2; i <= n; i++)
            dp[i] = dp[i - 1] + dp[i - 2];

        return dp[n];
    }

    /*
     * 解法3：DFS + Recursion + Adjacent list
     * - 思路：与超时解3一致，都是基于 f(顶点) = f(相邻顶点1) + f(相邻顶点2) + ... 只是超时解3中将该表达式化简为
     *   f(i) = f(i+1) + f(i+2) 了，而该解法中使用的是更一般的表达式。
     * - 实现：若用更一般形式的 DFS 来实现，则需在过执行程中到任一顶点的所有相邻顶点，大体有2种方式：
     *   1. 提前创建好 graph（本解法采用这种方式）；
     *   2. 需要的时候再计算。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int climbStairs3(int n) {
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
        if (i == n) return 1;
        if (cache[i] != -1) return cache[i];

        int numOfPath = 0;
        for (int adj : graph.get(i))
            numOfPath += dfs(graph, adj, n, cache);

        return cache[i] = numOfPath;
    }

    /*
     * 解法4：解法2的优化版，即 Recursion + Memoization（类似 L91 的解法1）
     * - 思路：解法2的通用性较强，但创建 graph 的过程会增加时间复杂度，因此这里采用解法2的"实现"描述中的第2种思路：到需要的时候再
     *   计算顶点的所有相邻顶点。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int climbStairs4(int n) {
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
        log(climbStairs2(2));  // expects 2 (1+1, 2 in one go)
        log(climbStairs2(3));  // expects 3 (1+1, 1+2, 2+1)
        log(climbStairs2(5));  // expects 8
    }
}
