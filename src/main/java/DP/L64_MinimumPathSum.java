package DP;

import static Utils.Helpers.log;

import java.util.LinkedList;
import java.util.Queue;

/*
* Minimum Path Sum
*
* - Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right which minimizes
*   the sum of all numbers along its path. Returns the sum at the end.
* - Note: You can only move either down or right at any point in time.
*
* - 注意：本题与 L120 不同，L120 是由一个顶点出发到多个顶点结束，而本题中是由一个顶点出发到一个顶点结束。
* */

public class L64_MinimumPathSum {
    /*
    * 超时解：top-down BFS
    * - 思路：与 L120 解法2完全一致，用图论将问题建模成为典型的寻路问题：
    *        1 → 3 → 1
    *        ↓   ↓   ↓
    *        1 → 5 → 1
    *        ↓   ↓   ↓
    *        4 → 2 → 1
    *   这样该问题就转化为了求左上到右下的所有路径中最小的节点值之和，因此可以用 BFS 找到每一条路径，同时求其中最小的顶点值之和。
    * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
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
        int rowCount = grid.length;
        int colCount = grid[0].length;

        Queue<Path> q = new LinkedList<>();
        q.offer(new Path(0, 0, grid[0][0]));

        while (!q.isEmpty()) {
            Path path = q.poll();
            int row = path.row;
            int col = path.col;
            int sum = path.sum;

            if (row == rowCount - 1 && col == colCount - 1) {  // 若已抵达右下角
                res = Math.min(res, sum);
                continue;
            }

            if (col < colCount - 1)  // 若还没到最右侧一列，则入队右侧节点
                q.offer(new Path(row, col + 1, sum + grid[row][col + 1]));
            if (row < rowCount - 1)        // 若还没到最下面一行，则入队下方节点
                q.offer(new Path(row + 1, col, sum + grid[row + 1][col]));
        }

        return res;
    }

    /*
    * 解法1：bottom-up DP
    * - 思路：
    *       1 → 3 → 1       1 → 4 → 5       1 → 4 → 5
    *       ↓   ↓   ↓       ↓   ↓   ↓       ↓   ↓   ↓
    *       1 → 5 → 1  -->  2 → 5 → 1  -->  2 → 7 → 6
    *       ↓   ↓   ↓       ↓   ↓   ↓       ↓   ↓   ↓
    *       4 → 2 → 1       6 → 2 → 1       6 → 8 → 7
    * - 时间复杂度 O(2^n)，空间复杂度 O(1)。
    * */
    public static int minPathSum1(int[][] grid) {
        int rowCount = grid.length;
        int colCount = grid[0].length;

        for (int i = 1; i < rowCount; i++)  // 遍历除第1行之外的每一行，让每行第0个元素加上上一行的第0个元素
            grid[i][0] += grid[i - 1][0];

        for (int j = 1; j < colCount; j++)  // 遍历除第1列之外的每一列，让每列第0个元素加上左边一列的第0个元素
            grid[0][j] += grid[0][j - 1];

        for (int i = 1; i < rowCount; i++)  // 一边比较一边往右下角 reduce
            for (int j = 1; j < colCount; j++)
                grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]);

        return grid[rowCount - 1][colCount - 1];   // 返回右下角的元素值
    }

    /*
    * 解法2：DFS
    * - 思路：
    *
    *
    * */
    public static int minPathSum2(int[][] grid) {
        int[][] cache = new int[grid.length][grid[0].length];
        return minPathSum2(grid, 0, 0, cache);
    }

    private static int minPathSum2(int[][] grid, int row, int col, int[][] cache) {
        int rowCount = grid.length;
        int colCount = grid[0].length;

        if (row == rowCount - 1 && col == colCount - 1)  // 若到达右下角则返回最终结果
            return grid[row][col];
        if (cache[row][col] != 0)                // 若该坐标之前已经计算过则使用缓存（Q: 是否应在初始化时将 cache 填充-1？？？）
            return cache[row][col];

        int rowInc = Integer.MAX_VALUE;
        int colInc = Integer.MAX_VALUE;

        if (row < rowCount - 1)
            rowInc = minPathSum2(grid, row + 1, col, cache);
        if (col < colCount - 1)
            colInc = minPathSum2(grid, row, col + 1, cache);

        cache[row][col] = Math.min(rowInc, colInc) + grid[row][col];

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
