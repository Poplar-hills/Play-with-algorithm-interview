package DP.S1_Basics;

import static Utils.Helpers.log;

import java.util.*;

/*
 * Minimum Path Sum
 *
 * - Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right which
 *   minimizes the sum of all numbers along its path. Returns the sum at the end.
 * - Rule: You can only move either down or right at any point in time.
 *
 * 💎 若用图论建模则需要注意：
 *   1. 本题与 L120_Triangle 不同，L120 是由一个顶点出发到多个顶点结束，而本题中是由一个顶点出发到一个顶点结束。
 *   2. 该题与 L279_PerfectSquares 不同，L279 可建模成无权图，无权图的最短路径是节点数最少的那条；而该题用图论建模只能建模成
 *      带权图，因为要求的是节点值之和最小的路径，相当于求成本最小的路径。
 * */

public class L64_MinimumPathSum {
    /*
     * 超时解1：BFS
     * - 思路：与 L120_Triangle 解法2完全一致，采用 BFS 遍历每一条路径，同时计算最小的节点值之和。
     *        1 → 3 → 1
     *        ↓   ↓   ↓
     *        1 → 5 → 1
     *        ↓   ↓   ↓
     *        4 → 2 → 1
     *   这样该问题就转化为了求左上到右下的所有路径中最小的节点值之和 ∴ 可以用 BFS 找到每一条路径，同时求其中最小的顶点值之和。
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)，其中 n 为节点个数。
     * */
    static class Path {
        final int r, c, sum;
        public Path(int r, int c, int sum) {
            this.r = r;
            this.c = c;
            this.sum = sum;
        }
    }

    public static int minPathSum_1(int[][] grid) {
        int minSum = Integer.MAX_VALUE;
        int m = grid.length, n = grid[0].length;

        Queue<Path> q = new LinkedList<>();
        q.offer(new Path(0, 0, grid[0][0]));

        while (!q.isEmpty()) {
            Path path = q.poll();
            int r = path.r, c = path.c, sum = path.sum;

            if (r == m - 1 && c == n - 1) {      // 若已抵达右下角
                minSum = Math.min(minSum, sum);  // 求最小的节点值之和
                continue;
            }

            if (c < n - 1)       // 若还没到最右一列，则入队右侧节点
                q.offer(new Path(r, c + 1, sum + grid[r][c + 1]));
            if (r < m - 1)       // 若还没到最后一行，则入队下方节点
                q.offer(new Path(r + 1, c, sum + grid[r + 1][c]));
        }

        return minSum;
    }

    /*
     * 超时解2：DFS + Recursion
     * - 思路：从左上到右下递归地计算每个节点到达右下角的 min path sum：
     *   - 子问题定义：f(r,c) 表示从节点 [r,c] 到达右下角的 min path sum。
     *   - 递推表达式：f(r,c) = min(f(r+1,c), f(r,c+1))，其中 r ∈ [0,w)，l ∈ [0,l)。
     *       1 ← 3 ← 1        7 ← 6 ← 3
     *       ↑   ↑   ↑        ↑   ↑   ↑   - f(0,0) = min(f(1,0), f(0,1))
     *       1 ← 5 ← 1   ->   8 ← 7 ← 2            = min(min(f(2,0), f(1,1)), min(f(0,2), f(1,1)))
     *       ↑   ↑   ↑        ↑   ↑   ↑            = ...
     *       4 ← 2 ← 1        7 ← 3 ← 1
     *   其中：
     *     - ∵ f(2,2) 是右下角终点，再没有路可走 ∴ f(2,2) = 1；
     *     - ∵ 类似 f(2,0)、f(0,2) 的边缘节点只有一个方向可以走 ∴ f(2,0) = f(2,1); f(0,2) = f(1,2)；
     *     - f(1,0)、f(0,1) 都可以走到 f(1,1) ∴ 出现了重叠子问题，本解法中并未进行优化。
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
     * */
    public static int minPathSum_2(int[][] grid) {
        if (grid == null || grid[0] == null) return 0;
        return dfs_2(grid, 0, 0);
    }

    private static int dfs_2(int[][] grid, int r, int c) {
        int m = grid.length, n = grid[0].length;
        int val = grid[r][c];

        if (r == m - 1 && c == n - 1) return val;
        if (r == m - 1) return val + dfs_2(grid, r, c + 1);
        if (c == n - 1) return val + dfs_2(grid, r + 1, c);

        return val + Math.min(dfs_2(grid, r + 1, c), dfs_2(grid, r, c + 1));
    }

    /*
     * 超时解3：DFS + Recursion
     * - 思路：虽然都是 DFS，但不同于超时解2，该解法是从左上角开始一直累加路径和 sum 直到到右下角，而非超时解2中从右下角逐层
     *   返回，一直累加到左上角（即 + grid[r][c] 的操作发生在每层递归内部，而非超时解2中的递归外部）。
     *        1 → 3 → 1        1 → 4 → 5
     *        ↓   ↓   ↓        ↓   ↓   ↓
     *        1 → 5 → 1   ->   2 → 7 → 6
     *        ↓   ↓   ↓        ↓   ↓   ↓
     *        4 → 2 → 1        6 → 8 → 7
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
     * */
    public static int minPathSum_3(int[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        return dfs_3(grid, 0, 0, 0);
    }

    private static int dfs_3(int[][] grid, int r, int c, int sum) {
        int newSum = sum + grid[r][c];
        if (r == grid.length - 1 && c == grid[0].length - 1)
            return newSum;

        int minSum = Integer.MAX_VALUE;
        if (r + 1 < grid.length)
            minSum = dfs_3(grid, r + 1, c, newSum);
        if (c + 1 < grid[0].length)
            minSum = Math.min(minSum, dfs_3(grid, r, c + 1, newSum));

        return minSum;
    }

    /*
     * // TODO: 解法1：Dijkstra
     * - 思路：带权图的最短路径可使用 Dijkstra 算法，可先解决 https://leetcode.com/problems/network-delay-time/，再解决本问题
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static int minPathSum1(int[][] grid) {
        return 0;
    }

    /*
     * 解法2：DFS + Recursion + Memoization
     * - 思路：在超时解2的基础上加入 Memoization 进行优化，以避免重复计算重叠子问题。例如：
     *       1 ← 3 ← 1
     *       ↑   ↑   ↑    - 递推表达式：f(r,c) = min(f(r+1,c), f(r,c+1))，其中 r ∈ [0,w)，l ∈ [0,l)
     *       1 ← 5 ← 1    - 在计算格子 f(0,1)、f(1,0) 时，f(1,1) 会被计算2遍 ∴ 需要 Memoization
     *       ↑   ↑   ↑
     *       4 ← 2 ← 1
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int minPathSum2(int[][] grid) {
        if (grid == null || grid[0] == null) return 0;

        int[][] cache = new int[grid.length][grid[0].length];  // cache[r][c] 记录节点 [r,c] 到达右下角的 min path sum
        for (int[] row : cache)
            Arrays.fill(row, -1);

        return dfs2(grid, 0, 0, cache);
    }

    private static int dfs2(int[][] grid, int r, int c, int[][] cache) {
        int m = grid.length, n = grid[0].length;
        int val = grid[r][c];

        if (r == m - 1 && c == n - 1) return val;
        if (cache[r][c] != -1) return cache[r][c];

        if (r == m - 1)
            return cache[r][c] = val + dfs2(grid, r, c + 1, cache);
        if (c == n - 1)
            return cache[r][c] = val + dfs2(grid, r + 1, c, cache);

        return cache[r][c] = val + Math.min(dfs2(grid, r + 1, c, cache), dfs2(grid, r, c + 1, cache));
    }

    /*
     * 解法3：DP
     * - 思路：超时解2中的递推表达式是 f(r, c) = min(f(r+1, c), f(r, c+1))，即每个节点的解 f(r, c) 是建立在其下游的两个
     *   节点的解 f(r+1, c)、f(r, c+1) 之上的 ∴ 可以很自然的使用递归求解 —— 这是自上而下的递推过程。而 DP 的思路与此是一致的，
     *   使用的递推表达式也一样，只是递推过程是自下而上，即从终点 f(w-1, l-1) 开始，递推出 f(w-2, l-1)、f(w-1, l-2)，再递推
     *   出 f(w-2, l-2)…… 如此往复直到递推出 f(0, 0) 为止。
     * - 优化：该解法还可以再进行空间优化 —— ∵ 每行的计算都只依赖于当前行右侧和下一行中的值 ∴ 可以采用类似 _ZeroOneKnapsack
     *   中解法3的滚动数组方案，dp 数组只保留两行并重复利用。但遍历方向需要改为从左上到右下（∵ 需要知道当前是奇/偶数行）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int minPathSum3(int[][] grid) {
        if (grid == null || grid[0] == null) return 0;

        int m = grid.length;
        int n = grid[0].length;

        int[][] dp = new int[m][n];
        for (int[] row : dp)
            Arrays.fill(row, Integer.MAX_VALUE);

        dp[m - 1][n - 1] = grid[m - 1][n - 1];  // 设置递推的起始值

        for (int r = m - 1; r >= 0; r--) {      // 从终点 [m-1,n-1] 开始往左、往上进行递推
            for (int c = n - 1; c >= 0; c--) {
                if (r != m - 1)
                    dp[r][c] = Math.min(dp[r][c], grid[r][c] + dp[r + 1][c]);
                if (c != n - 1)
                    dp[r][c] = Math.min(dp[r][c], grid[r][c] + dp[r][c + 1]);
            }
        }

        return dp[0][0];
    }

    /*
     * 解法4：In-place DP
     * - 思路：与解法3一致。
     * - 实现：
     *   1. 不另外建立 dp 数组，而是就地修改 grid 数组；
     *   2. 遍历方向为从左上到右下，不断对 grid 进行填充/更新：f(r,c) = min(f(r-1, c), f(r, c-1))。
     *
     *          1  3  1         1 → 4 → 5         1 → 4 → 5         1 → 4 → 5
     *          1  5  1   -->              -->    ↓   ↓   ↓   -->   ↓   ↓   ↓
     *          4  2  1                           2 → 7 → 6         2 → 7 → 6
     *                                                              ↓   ↓   ↓
     *                                                              6 → 8 → 7
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int minPathSum4(int[][] grid) {
        if (grid == null || grid[0] == null) return 0;

        int m = grid.length;
        int n = grid[0].length;

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (r == 0 && c == 0) continue;
                if (r == 0)
                    grid[r][c] += grid[r][c - 1];
                else if (c == 0)
                    grid[r][c] += grid[r - 1][c];
                else
                    grid[r][c] += Math.min(grid[r - 1][c], grid[r][c - 1]);
            }
        }

        return grid[m - 1][n - 1];
    }

    /*
     * 解法5：In-place DP（解法4的另一种写法）
     * - 思路：与解法4一致。
     * - 实现：观察解法4可知 ∵ 第一行和第一列是特殊情况，不需要比较，只有一种选择 ∴ 可以先手动处理这些特殊情况，然后再处理其他
     *   位置上的一般情况：
     *       1   3   1               1 → 4 → 5               1 → 4 → 5                1 → 4 → 5
     *                    Add up                  Add up     ↓             Handle     ↓   ↓   ↓
     *       1   5   1   -------->   1   5   1   -------->   2   5   1   --------->   2 → 7 → 6
     *                    1st row                 1st col    ↓            the rest    ↓   ↓   ↓
     *       4   2   1               4   2   1               6   2   1                6 → 8 → 7
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int minPathSum5(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        for (int r = 1; r < m; r++)        // Add up 1st row
            grid[r][0] += grid[r - 1][0];

        for (int c = 1; c < n; c++)        // Add up 1st column
            grid[0][c] += grid[0][c - 1];

        for (int r = 1; r < m; r++)        // Handle the rest
            for (int c = 1; c < n; c++)
                grid[r][c] += Math.min(grid[r - 1][c], grid[r][c - 1]);

        return grid[m - 1][n - 1];
    }

    public static void main(String[] args) {
        log(minPathSum_3(new int[][]{
            {1, 3, 1},
            {1, 5, 1},
            {4, 2, 1}
        }));  // expects 7. (1->3->1->1->1)

        log(minPathSum_3(new int[][]{
            {1, 3, 4},
            {1, 2, 1},
        }));  // expects 5. (1->1->2->1)

        log(minPathSum_3(new int[][]{
            {1, 2, 3}
        }));  // expects 6.

        log(minPathSum_3(new int[][]{
            {0}
        }));  // expects 0.
    }
}
