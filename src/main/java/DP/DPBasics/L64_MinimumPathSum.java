package DP.DPBasics;

import static Utils.Helpers.log;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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
    * 超时解：Brute force BFS
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
        final int row, col, sum;
        public Path(int row, int col, int sum) {
            this.row = row;
            this.col = col;
            this.sum = sum;
        }
    }

    public static int minPathSum(int[][] grid) {
        int res = Integer.MAX_VALUE;
        int m = grid.length, n = grid[0].length;

        Queue<Path> q = new LinkedList<>();
        q.offer(new Path(0, 0, grid[0][0]));

        while (!q.isEmpty()) {
            Path path = q.poll();
            int row = path.row, col = path.col, sum = path.sum;

            if (row == m - 1 && col == n - 1) {  // 若已抵达右下角
                res = Math.min(res, sum);        // 求最小的节点值之和
                continue;
            }

            if (col < n - 1)       // 若还没到最右一列，则入队右侧节点
                q.offer(new Path(row, col + 1, sum + grid[row][col + 1]));
            if (row < m - 1)       // 若还没到最后一行，则入队下方节点
                q.offer(new Path(row + 1, col, sum + grid[row + 1][col]));
        }

        return res;
    }

    /*
    * 解法2：DP
    * - 思路：
    *   - 子问题定义：f(i, j) 表示“从左上角到位置 (i,j) 的所有路径上最小的节点值之和”；
    *   - 状态转移方程：f(i, j) = min(f(i+1, j), f(i, j+1))。
    * - 优化：该解法还可以再进行空间优化 —— ∵ 每一行的计算都只依赖于当前行右侧和下一行中的值 ∴ 可以采用类似 _ZeroOneKnapsack
    *   中解法3的滚动数组方案，dp 数组只保留两行并重复利用。但遍历方向需要改为从左上到右下（∵ 需要知道当前是奇/偶数行）。
    * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
    * */
    public static int minPathSum2(int[][] grid) {
        if (grid == null || grid[0] == null) return 0;

        int m = grid.length;
        int n = grid[0].length;

        int[][] dp = new int[m][n];
        for (int[] row : dp)
            Arrays.fill(row, Integer.MAX_VALUE);

        dp[m - 1][n - 1] = grid[m - 1][n - 1];

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (i != m - 1)
                    dp[i][j] = Math.min(dp[i][j], grid[i][j] + dp[i + 1][j]);
                if (j != n - 1)
                    dp[i][j] = Math.min(dp[i][j], grid[i][j] + dp[i][j + 1]);
            }
        }

        return dp[0][0];
    }

    /*
    * 解法3：In-place DP
    * - 思路：不建立 dp 数组，就地修改。
    * - 时间复杂度 O(m*n)，空间复杂度 O(n)。
    * */
    public static int minPathSum3(int[][] grid) {
        if (grid == null || grid[0] == null) return 0;

        int m = grid.length;
        int n = grid[0].length;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) continue;
                if (i == 0)
                    grid[0][j] += grid[0][j - 1];
                else if (j == 0)
                    grid[i][0] += grid[i - 1][j];
                else
                    grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]);
            }
        }

        return grid[m - 1][n - 1];
    }

    /*
    * 解法4：DP
    * - 思路：
    *       1 → 3 → 1           1 → 3 → 1           1 → 4 → 5           1 → 4 → 5
    *       ↓   ↓   ↓   step1   ↓   ↓   ↓   step2   ↓   ↓   ↓   step3   ↓   ↓   ↓
    *       1 → 5 → 1  ------>  2 → 5 → 1  ------>  2 → 5 → 1  ------>  2 → 7 → 6
    *       ↓   ↓   ↓           ↓   ↓   ↓           ↓   ↓   ↓           ↓   ↓   ↓
    *       4 → 2 → 1           6 → 2 → 1           6 → 2 → 1           6 → 8 → 7
    * - 时间复杂度 O(m*n)，空间复杂度 O(1)，其中 m 为行数，n 为列数。
    * */
    public static int minPathSum4(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        for (int i = 1; i < m; i++)  // step1: 遍历除第1行之外的每一行，让每行第0个元素加上上一行的第0个元素
            grid[i][0] += grid[i - 1][0];

        for (int j = 1; j < n; j++)  // step2: 遍历除第1列之外的每一列，让每列第0个元素加上左边一列的第0个元素
            grid[0][j] += grid[0][j - 1];

        for (int i = 1; i < m; i++)  // step3: 计算除第一行和第一列外的所有节点，一边比较一边往右下角 reduce
            for (int j = 1; j < n; j++)
                grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]);

        return grid[m - 1][n - 1];   // 返回右下角的元素值
    }

    /*
    * 解法5：Recursion + Memoization（也可以理解为 DFS）
    * - 思路：
    *   1. 从左上到右下递归地为每个节点计算从左上角到该节点的 minimum path sum；
    *   2. ∵ 中间节点会被重复计算 ∴ 使用 memoization（cache）进行优化；
    *   3. 思路上很类似 L279_PerfectSquares 中的解法2。
    * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)，其中 m 为行数，n 为列数。
    * */
    public static int minPathSum5(int[][] grid) {
        int[][] cache = new int[grid.length][grid[0].length];
        return calcNodeMinPathSum(grid, 0, 0, cache);
    }

    private static int calcNodeMinPathSum(int[][] grid, int row, int col, int[][] cache) {
        int m = grid.length;
        int n = grid[0].length;

        if (row == m - 1 && col == n - 1)      // 递归终止条件：到达右下角，此时返回右下角节点的值
            return grid[row][col];

        if (cache[row][col] != 0)              // 有缓存就用缓存（Q: 是否应在初始化时将 cache 填充-1？？？）
            return cache[row][col];

        int downSum = Integer.MAX_VALUE;  // 下方节点的 path sum
        int rightSum = Integer.MAX_VALUE;  // 右侧节点的 path sum

        if (row < m - 1)
            downSum = calcNodeMinPathSum(grid, row + 1, col, cache);  // 若还未到达最底层 row 则计算当前节点下方的节点
        if (col < n - 1)
            rightSum = calcNodeMinPathSum(grid, row, col + 1, cache);  // 若还未到达最右侧 column 则计算当前节点右侧的节点

        cache[row][col] = Math.min(downSum, rightSum) + grid[row][col]; // 递归到底（右下角节点）后在回来的路上开始真正计算

        return cache[row][col];
    }

    public static void main(String[] args) {
        log(minPathSum2(new int[][]{
            new int[]{1, 3, 1},
            new int[]{1, 5, 1},
            new int[]{4, 2, 1}
        }));  // expects 7. (1->3->1->1->1)

        log(minPathSum2(new int[][]{
            new int[]{1, 3, 4},
            new int[]{1, 2, 1},
        }));  // expects 5. (1->1->2->1)

        log(minPathSum2(new int[][]{
            new int[]{1, 2, 3}
        }));  // expects 6.

        log(minPathSum2(new int[][]{
            new int[]{0}
        }));  // expects 0.
    }
}
