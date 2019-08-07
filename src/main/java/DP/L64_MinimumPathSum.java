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
        final int row, column, sum;
        public Path(int row, int column, int sum) {
            this.row = row;
            this.column = column;
            this.sum = sum;
        }
    }

    public static int minPathSum(int[][] grid) {
        int res = Integer.MAX_VALUE;
        Queue<Path> q = new LinkedList<>();
        q.offer(new Path(0, 0, grid[0][0]));

        int rowNum = grid.length;
        int columnNum = grid[0].length;

        while (!q.isEmpty()) {
            Path path = q.poll();
            int row = path.row;
            int column = path.column;
            int sum = path.sum;

            if (row == rowNum - 1 && column == columnNum - 1) {  // 若已抵达右下角
                res = Math.min(res, sum);
                continue;
            }

            if (column < columnNum - 1)  // 若还没到最右侧一列，则入队右侧节点
                q.offer(new Path(row, column + 1, sum + grid[row][column + 1]));
            if (row < rowNum - 1)        // 若还没到最下面一行，则入队下方节点
                q.offer(new Path(row + 1, column, sum + grid[row + 1][column]));
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
        int m = grid.length;
        int n = grid[0].length; 

        for (int i = 1; i < m; i++)  // 遍历除第1行之外的每一行，让每行第0个元素加上上一行的第0个元素
            grid[i][0] += grid[i - 1][0];

        for (int j = 1; j < n; j++)  // 遍历除第1列之外的每一列，让每列第0个元素加上左边一列的第0个元素
            grid[0][j] += grid[0][j - 1];

        for (int i = 1; i < m; i++)  // 一边比较一边往右下角 reduce
            for (int j = 1; j < n; j++)
                grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]);

        return grid[m - 1][n - 1];    // 返回右下角的元素值
    }

    public static void main(String[] args) {
        log(minPathSum1(new int[][]{
            new int[]{1, 3, 1},
            new int[]{1, 5, 1},
            new int[]{4, 2, 1}
        }));  // expects 7. (1->3->1->1->1)

        log(minPathSum1(new int[][]{
            new int[]{1, 3, 4},
            new int[]{1, 2, 1},
        }));  // expects 5. (1->1->2->1)

        log(minPathSum1(new int[][]{
            new int[]{1, 2, 3}
        }));  // expects 6.

        log(minPathSum1(new int[][]{
            new int[]{0}
        }));  // expects 0.
    }
}
