package DP.S1_Basics;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Triangle
 *
 * - Given a triangle, find the minimum path sum from top to bottom.
 *
 * - Note: Each step you may move to adjacent numbers on the row below (比如下面的 test case 2 中，第2行的2只能移动
 *   到第3行中的1或-1上，而不能移动到-3上，因此不是从每行中找到最小值就行).
 *
 * - Bonus point if using only O(n) extra space, where n is the number of rows in the triangle.
 * */

public class L120_Triangle {
    /*
     * 超时解1：BFS
     * - 思路：采用 BFS，在 Queue 中存储由 level, index, sum 组成的 Path 对象，level + index 确定该路径上的最新顶点的
     *   位置，sum 记录路径当前的节点值之和。
     * - 时间复杂度 O(2^n)：∵ 每个顶点都会产生2个分支 ∴ 复杂度与 Fibonacci.java 的解法1一致。
     * - 空间复杂度 O(n)。
     * */
    static class Path {
        final int level, index, sum;  // immutable memebers
        public Path(int level, int index, int sum) {
            this.level = level;
            this.index = index;
            this.sum = sum;
        }
    }

    public static int minimumTotal_1(List<List<Integer>> triangle) {
        int res = Integer.MAX_VALUE;
        Queue<Path> q = new LinkedList<>();
        q.offer(new Path(0, 0, triangle.get(0).get(0)));  // 队列中记录 <level, index, sum>（也可以用 Queue<int[]>）

        while (!q.isEmpty()) {
            Path path = q.poll();
            int level = path.level, index = path.index, sum = path.sum;

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
     * 超时解2：BFS + 记录所有路径
     * - 思路：与 L70_ClimbingStairs 超时解2一致，用 BFS 找到所有路径，求其中最小的路径和。
     * - 时间复杂度 O(2^n)：解释同超时解1。
     * - 空间复杂度 O(nlogn)：Queue 中同一时间最多存储 n/2 条路径（完美二叉树最底层节点个数为 n/2），而每条路径中有 logn（树高）个顶点。
     * */
    public static int minimumTotal_2(List<List<Integer>> triangle) {
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
     * 超时解3：DFS + Recursion
     * - 思路：若用 DFS + 递归求解，那就需要思考前后子问题之间的递推关系，即 f(l,i) 与 f(l+1,i), f(l+1,i+1) 的是如何递推的：
     *   - 定义子问题：f(l, i) 表示"从节点 [l,i] 开始到三角形最底层的 minimum path sum"；
     *   - 递推表达式：f(l, i) = min(f(l+1, i), f(l+1, i+1)) + nodeVal[l,i]。
     * - 时间复杂度 O(2^n)，空间复杂度 O(h)。
     * */
    public static int minimumTotal_3(List<List<Integer>> triangle) {
        if (triangle == null || triangle.isEmpty()) return 0;
        return helper_3(triangle, 0, 0);
    }

    private static int helper_3(List<List<Integer>> triangle, int level, int index) {
        if (level == triangle.size() - 1)
            return triangle.get(level).get(index);

        return Math.min(
            helper_3(triangle, level + 1, index),
            helper_3(triangle, level + 1, index + 1)) + triangle.get(level).get(index);
	}

    /*
     * 解法1：DFS + Recursion + Memoization
     * - 思路：超时解1-3之所以超时，是因为对其中的重叠子问题进行了重复计算：
     *              2
     *            3   4
     *          6   5   7
     *        4   1   8   3
     *   在计算 f(3)、f(4) 的时候 f(5) 就被重复计算了两次 ∴ 只要在超时解3的基础上加入 Memoization 就可以解决问题。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * - 优化：∵ 开辟的 cache 是以三角最后一行为宽度 ∴ 会浪费一半的空间。改进方式是采用：
     *   1. Map<String, Integer>，其中 key 为 l + "-" + i；
     *   2. 类似 _ZeroOneKnapsack 中解法3的滚动数组方案。
     * */
    public static int minimumTotal1(List<List<Integer>> triangle) {
        int h = triangle.size();
        int[][] cache = new int[h][triangle.get(h - 1).size()];
        for (int[] row : cache)
            Arrays.fill(row, -1);
        return dfs1(triangle, 0, 0, cache);
    }

    private static int dfs1(List<List<Integer>> triangle, int l, int i, int[][] cache) {
        if (l == triangle.size() - 1)
            return triangle.get(l).get(i);

        if (cache[l][i] != -1) return cache[l][i];

        return cache[l][i] = triangle.get(l).get(i) + Math.min(
            dfs1(triangle, l + 1, i, cache),
            dfs1(triangle, l + 1, i + 1, cache));
    }

    /*
     * 解法2：DP
     * - 思路：既然可以用 DFS + Recursion 求解，那很可能也能用 DP 求解 —— 自下而上递推出每个节点上的解。其中子问题定义和
     *   递推表达式不变）：
     *   - 子问题定义：f(l,i) 表示"从节点 [l,i] 开始到三角形最底层的 minimum path sum"；
     *   - 递推表达式：f(l,i) = nodeVal[l,i] + min(f(l+1,i), f(l+1,i+1))。
     * - 实现：从下到上逐层递推，同一层内两两节点先进行比较，选出较小的与父节点相加：
     *            -1         |                                                        -1
     *           /  \        |                                                       ↗  ↖
     *          2    3       |                               1    0                 1    0
     *        /  \  /  \     |                     -->     ↗  ↖  ↗  ↖     -->     ↗  ↖  ↗  ↖
     *       1    -1   -3    |    1     -1    -3         1     -1    -3         1     -1    -3
     * - 时间复杂度 O(n) 或 O(h^2)，其中 h 为三角形高度（之所以为 O(h^2) 是 ∵ 代码中的双重循环范围都可以近似为 0~h）。
     * - 空间复杂度 O(1)。
     * */
    public static int minimumTotal2(List<List<Integer>> triangle) {
        int h = triangle.size();
        int[][] dp = new int[h][triangle.get(h - 1).size()];

        for (int l = h - 1; l >= 0; l--) {
            for (int i = 0; i < triangle.get(l).size(); i++) {
                if (l == h - 1) {
                    dp[l][i] = triangle.get(l).get(i);
                    continue;
                }
                dp[l][i] = triangle.get(l).get(i) + Math.min(dp[l+1][i], dp[l+1][i+1]);
            }
        }

        return dp[0][0];
    }

    /*
     * 解法3：DP（解法3的优化版，即滚动数组）
     * - 思路：与解法3一致。
     * - 实现：dp 数组的大小无需解法2中的那么大，只需开辟三角形最底层节点数大小，之后每层都在上面覆盖即可（即滚动数组）。
     * - 时间复杂度 O(n) 或 O(h^2)，其中 h 为三角形高度（操作数组比操作 List 更快，∴ 该解法统计性能更优）。
     * - 空间复杂度 O(h)。
     * */
    public static int minimumTotal3(List<List<Integer>> triangle) {
        int h = triangle.size();

        int[] dp = new int[h];                   // 开辟三角形底层节点数大小的数组（全等三角形高度 = 底层节点数）
        for (int i = 0; i < h; i++)
            dp[i] = triangle.get(h - 1).get(i);  // 初始化为三角形最底层的节点值

        for (int l = h - 2; l >= 0; l--)         // 从倒数第2层开始往上遍历
            for (int i = 0; i <= l; i++)         // 遍历每一层中的每个节点（∵ 每层的节点数 = 当前层的高度 ∴ i ∈ [0,l]）
                dp[i] = triangle.get(l).get(i) + Math.min(dp[i], dp[i + 1]);  // 覆盖

        return dp[0];
    }

    /*
     * 解法4：In-place DP
     * - 思路：与解法2一致。
     * - 实现：不另开空间，而是直接在 triangle 上覆盖。
     * - 时间复杂度 O(n) 或 O(h^2)，其中 h 为三角形高度，空间复杂度 O(1)。
     * */
    public static int minimumTotal4(List<List<Integer>> triangle) {
        for (int l = triangle.size() - 2; l >= 0; l--) {  // 从倒数第2层开始往上遍历
            List<Integer> level = triangle.get(l);
            List<Integer> levelBelow = triangle.get(l + 1);

            for (int i = 0; i <= l; i++)  // 遍历一层中的节点（全等三角形每层的节点个数等于层高 ∴ 第 l 层共有 l 个节点）
                level.set(i, level.get(i) + Math.min(levelBelow.get(i), levelBelow.get(i + 1)));
        }
        return triangle.get(0).get(0);
    }

    public static void main(String[] args) {
        log(minimumTotal4(List.of(
               Arrays.asList(2),
              Arrays.asList(3, 4),
             Arrays.asList(6, 5, 7),
            Arrays.asList(4, 1, 8, 3)
        )));  // expects 11. (2 + 3 + 5 + 1)

        log(minimumTotal4(List.of(
               Arrays.asList(-1),
              Arrays.asList(2, 3),
            Arrays.asList(1, -1, -3)
        )));  // expects -1. (-1 + 3 + -3) 注意不是从每行中找到最小值就行

        log(minimumTotal4(List.of(
            Arrays.asList(-10)
        )));  // expects -10
    }
}
