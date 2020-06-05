package DP.S1_Basics;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Triangle
 *
 * - Given a triangle, find the minimum path sum from top to bottom.
 * - Note: Each step you may move to adjacent numbers on the row below (比如下面的 test case 2 中，第2行的2只能移动
 *   到第3行中的1或-1上，而不能移动到-3上，因此不是从每行中找到最小值就行).
 * - Bonus point if using only O(n) extra space, where n is the number of rows in the triangle.
 * */

public class L120_Triangle {
    /*
     * 超时解1：Brute force BFS
     * - 思路：与 L70_ClimbingStairs 中的超时解一致，用图论建模：
     *            -1
     *           ↙  ↘
     *          2    3
     *        ↙  ↘  ↙  ↘
     *       1    -1   -3
     *   建模后该题转化为求从顶层到底层顶点的路径中最小的顶点值之和，因此可以用 BFS 找到每一条路径，同时求其中最小的顶点值之和。
     * - 时间复杂度 O(2^n)：∵ 每个顶点都会产生2个分支 ∴ 复杂度与 Fibonacci.java 的解法1一致。
     * - 空间复杂度 O(nlogn)：queue 中同一时间最多存储 n/2 条路径（完美二叉树最底层节点个数为 n/2），而每条路径中有 logn（树高）个顶点。
     * */
    public static int minimumTotal(List<List<Integer>> triangle) {
        int res = Integer.MAX_VALUE;
        Queue<List<Integer>> q = new LinkedList<>();  // 队列中存放 path，每条 path 中存放每个顶点在其 level 上的 index
        List<Integer> initialPath = new ArrayList<>();
        initialPath.add(0);
        q.offer(initialPath);

        while (!q.isEmpty()) {
            List<Integer> path = q.poll();

            if (path.size() == triangle.size()) {       // 若一个 path 中的顶点个数 == triangle 的高度，则说明已经到底
                int sum = 0;                            // 开始求该 path 的所有顶点值之和
                for (int i = 0; i < path.size(); i++)
                    sum += triangle.get(i).get(path.get(i));
                res = Math.min(res, sum);
                continue;                               // 进入下一轮循环
            }

            int currIndex = path.get(path.size() - 1);  // 若该 path 还未到底，则获取其最新顶点在其 level 上的 index
            for (int i = 0; i < 2; i++) {               // 寻找相邻顶点
                List<Integer> newPath = new ArrayList<>(path);   // 复制当前的 path（这个技巧很有用）
                newPath.add(currIndex + i);                      // 将下一步节点的 index 放入 newPath 中
                q.offer(newPath);
            }
        }

        return res;
    }

    /*
     * 超时解2：Brute force BFS
     * - 思路：和超时解1一样，都采用 BFS 遍历每一条路径。区别在于封装了 Path 对象，由 level, index, sum 三者确定的一条路径，
     *   level, index 记录路径上的最新顶点，sum 记录路径当前的节点值之和。
     * - 时间复杂度 O(2^n)：解释同超时解1。
     * - 空间复杂度 O(n)：queue 中每个元素只是 Path 对象，而非如超时解1中的整个顶点列表，因此不需要乘以 logn。
     * */
    static class Path {
        final int level, index, sum;  // immutable memebers
        public Path(int level, int index, int sum) {
            this.level = level;
            this.index = index;
            this.sum = sum;
        }
    }

    public static int minimumTotal0(List<List<Integer>> triangle) {
        int res = Integer.MAX_VALUE;
        Queue<Path> q = new LinkedList<>();
        q.offer(new Path(0, 0, triangle.get(0).get(0)));  // 队列中保存 <level, index, sum> 信息，sum 初始化为根顶点值

        while (!q.isEmpty()) {
            Path path = q.poll();
            int level = path.level;
            int index = path.index;
            int sum = path.sum;

            if (level == triangle.size() - 1) {  // 若已抵达 bottom level 则不再入队，只比较 sum
                res = Math.min(res, sum);
                continue;
            }

            for (int i = 0; i < 2; i++) {
                int adj = triangle.get(level + 1).get(index + i);    // 到下一层中取相邻顶点
                q.offer(new Path(level + 1, index + i, sum + adj));  // 每个相邻顶点都是一个分支，即产生一条新的路径
            }
        }

        return res;
    }

    /*
     * TODO: 解法1：Dijkstra
     * - 思路：同 L64_MinimumPathSum 解法1，可将该问题建模成带权图，而带权图的最短路径可使用 Dijkstra 算法。
     * */
    public static int minimumTotal1(List<List<Integer>> triangle) {
        return 0;
    }

    /*
     * 超时解3：DFS
     * - 思路：
     *   - 定义子问题：f(i, j) 表示"从节点 (i, j) 开始到三角形底层的之间的 minimum path sum"；
     *   - 状态转移方程：f(i, j) = min(f(i+1, j), f(i+1, j+1))。
     * - 时间复杂度 O(2^n)，空间复杂度 O(h)。
     * */
    public static int minimumTotal00(List<List<Integer>> triangle) {
        return helper(triangle, 0, 0);
    }

    private static int helper(List<List<Integer>> triangle, int i, int j) {
        if (i == triangle.size() - 1)
            return triangle.get(i).get(j);

        int minSum = triangle.get(i).get(j);
        minSum += Math.min(helper(triangle, i + 1, j), helper(triangle, i + 1, j + 1));

        return minSum;
	}

    /*
     * 解法2：Recursion + Memoization (DFS with cache)
     * - 思路：在超时解3的基础上加入 Memoization 优化。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * - 注：∵ 开辟的 cache 是以三角最后一行为宽度 ∴ 会浪费一半的空间。改进方式是采用类似 _ZeroOneKnapsack 中解法3的滚动数组方案。
     * */
    public static int minimumTotal2(List<List<Integer>> triangle) {
        int h = triangle.size();
        int[][] cache = new int[h][triangle.get(h - 1).size()];
        for (int[] row : cache)
            Arrays.fill(row, -1);
        return helper(triangle, 0, 0, cache);
    }

    private static int helper(List<List<Integer>> triangle, int i, int j, int[][] cache) {
        if (i == triangle.size() - 1)
            return triangle.get(i).get(j);

        if (cache[i][j] != -1) return cache[i][j];

        int minSum = triangle.get(i).get(j);
        minSum += Math.min(helper(triangle, i + 1, j, cache), helper(triangle, i + 1, j + 1, cache));

        return cache[i][j] = minSum;
    }

	/*
     * 解法3：In-place DP
     * - 思路：状态转移方程仍然是 f(i, j) = min(f(i+1, j), f(i+1, j+1))。
     * - 实现：从下到上逐层遍历，同一层内两个子节点先进行比较，选出较小的与父节点相加：
     *            -1
     *           /  \               -1
     *          2    3     --->    /  \    --->    -1
     *        /  \  /  \          1    0
     *       1    -1   -3
     *   在第三层中从1、-1中选出-1加到第二层的2上；从-1、-3中选出-3加到第二层的3上。在第二层中从1、0中选出0加到第一层的-1上，
     *   得到最终结果-1。
     * - 时间复杂度 O(h^2)，空间复杂度 O(1)，其中 h 为三角形高度。之所以为 O(h^2) 是因为代码中的双重循环范围都可以近似为 0~h。
     * */
    public static int minimumTotal3(List<List<Integer>> triangle) {
        for (int i = triangle.size() - 2; i >= 0; i--) {  // 从倒数第2层开始往上遍历
            List<Integer> currLevel = triangle.get(i);
            List<Integer> lowerLevel = triangle.get(i + 1);

            for (int j = 0; j <= i; j++) {  // 遍历一层中的每个节点（全等三角形每层的节点个数等于层高 ∴ 第 i 层共有 i 个节点）
                int min = Math.min(lowerLevel.get(j), lowerLevel.get(j + 1));
                currLevel.set(j, currLevel.get(j) + min);
            }
        }
        return triangle.get(0).get(0);
    }

    /*
     * 解法4：DP
     * - 思路：与解法3一样，只是写法不同，另外操作数组比操作 List 更快，因此该解法统计性能更优。
     * - 时间复杂度 O(h^2)，空间复杂度 O(n)，其中 h 为三角形高度。
     * */
    public static int minimumTotal4(List<List<Integer>> triangle) {
        int h = triangle.size();

        int[] dp = new int[h];                   // 开辟大小为 h 的额外空间，不改变 triangle 中的值
        for (int i = 0; i < h; i++)
            dp[i] = triangle.get(h - 1).get(i);  // 将 dp 初始化为三角形最底层（底层有 h 个节点）

        for (int i = h - 2; i >= 0; i--)         // 从倒数第2层开始往上遍历
            for (int j = 0; j <= i; j++)         // 遍历每一层中的每个节点（第 i 层共有 i 个节点）
                dp[j] = triangle.get(i).get(j) + Math.min(dp[j], dp[j + 1]);  // 覆盖

        return dp[0];
    }
    public static void main(String[] args) {
        log(minimumTotal3(Arrays.asList(
            Arrays.asList(2),
            Arrays.asList(3, 4),
            Arrays.asList(6, 5, 7),
            Arrays.asList(4, 1, 8, 3)
        )));  // expects 11 (2 + 3 + 5 + 1)

        log(minimumTotal3(Arrays.asList(
            Arrays.asList(-1),
            Arrays.asList(2, 3),
            Arrays.asList(1, -1, -3)
        )));  // expects -1 (-1 + 3 + -3) 注意不是从每行中找到最小值就行，如：(-1 + 2 + -3)

        log(minimumTotal3(Arrays.asList(
            Arrays.asList(-10)
        )));  // expects -10
    }
}
