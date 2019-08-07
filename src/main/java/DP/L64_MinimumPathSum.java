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
    * - 时间复杂度 O()，空间复杂度 O()。
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

    // 解法2：DFS???
    public static void main(String[] args) {
        log(minPathSum(new int[][]{
            new int[]{1, 3, 1},
            new int[]{1, 5, 1},
            new int[]{4, 2, 1}
        }));  // expects 7. (1->3->1->1->1)

        log(minPathSum(new int[][]{
            new int[]{0}
        }));  // expects 0.
    }
}
