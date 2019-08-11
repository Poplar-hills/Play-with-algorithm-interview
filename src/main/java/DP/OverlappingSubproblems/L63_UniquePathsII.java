package DP.OverlappingSubproblems;

import static Utils.Helpers.log;

/*
* Unique Paths II
*
* - 在 L62 的条件上，now consider if some obstacles are added to the grids. How many unique paths would there be?
* */

public class L63_UniquePathsII {
    /*
    * 解法1：
    * - 思路：
    *       ■ → ■ → ■         2 ← 1 ← 1
    *       ↓       ↓         ↑       ↑
    *       ■   □   ■   -->   1   0   1
    *       ↓       ↓         ↑       ↑
    *       ■ → ■ → ■         1 ← 1 ← 1
    * - 时间复杂度 O()，空间复杂度 O()。
    * */
    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        if (obstacleGrid == null || obstacleGrid[0] == null)
            return 0;
        int[][] cache = new int[obstacleGrid.length][obstacleGrid[0].length];
        return uniquePathsWithObstacles(obstacleGrid, 0, 0, cache);
    }

    private static int uniquePathsWithObstacles(int[][] grid, int i, int j, int[][] cache) {
        int m = grid.length;
        int n = grid[0].length;

        if (grid[i][j] == 1) return 0;
        if (i == m - 1 && j == n - 1) return 1;
        if (cache[i][j] != 0) return cache[i][j];

        int res = 0;
        if (i + 1 < m)
            res += uniquePathsWithObstacles(grid, i + 1, j, cache);
        if (j + 1 < n)
            res += uniquePathsWithObstacles(grid, i, j + 1, cache);

        return cache[i][j] = res;
    }

    public static void main(String[] args) {
        log(uniquePathsWithObstacles(new int[][]{
            new int[]{0, 0, 0},
            new int[]{0, 1, 0},
            new int[]{0, 0, 0},
        }));  // expects 2. (R->R->D->D, D->D->R->R)

        log(uniquePathsWithObstacles(new int[][]{
            new int[]{1},
        }));  // expects 0.
    }
}