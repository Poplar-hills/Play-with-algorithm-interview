package DP.OverlappingSubproblems;

import static Utils.Helpers.log;

/*
* Unique Paths II
*
* - 在 L62 的条件上，now consider if some obstacles are added to the grids. How many unique paths would there be?
* */

public class L63_UniquePathsII {
    /*
    * 超时解：Recursion + Memoizaiton
    * - 思路：在 L62 解法1的基础上加上对障碍物的判断条件即可。
    *       ■ → ■ → ■         2 ← 1 ← 1
    *       ↓       ↓         ↑       ↑
    *       ■   □   ■   -->   1   0   1
    *       ↓       ↓         ↑       ↑
    *       ■ → ■ → ■         1 ← 1 ← 1
    * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
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

        if (grid[i][j] == 1) return 0;       // 若是障碍物节点则直接返回0
        if (i == m - 1 && j == n - 1) return 1;
        if (cache[i][j] != 0) return cache[i][j];

        int res = 0;
        if (i + 1 < m && grid[i+1][j] != 1)  // 若下方是障碍物，则不计算
            res += uniquePathsWithObstacles(grid, i + 1, j, cache);
        if (j + 1 < n && grid[i][j+1] != 1)  // 若右侧是障碍物，则不计算
            res += uniquePathsWithObstacles(grid, i, j + 1, cache);

        return cache[i][j] = res;
    }

    /*
    * 解法1：DP
    * - 思路：在 L62 解法2的基础上加上对障碍物的判断即可。
    * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
    * */
    public static int uniquePathsWithObstacles1(int[][] obstacleGrid) {
        if (obstacleGrid == null || obstacleGrid[0] == null)
            return 0;

        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[][] cache = new int[m][n];
        cache[m - 1][n - 1] = 1;

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (obstacleGrid[i][j] == 1) {  // 若遇到障碍则直接将障碍点的结果记为0
                    cache[i][j] = 0;
                    continue;
                }
                if (i + 1 < m)
                    cache[i][j] += cache[i + 1][j];
                if (j + 1 < n)
                    cache[i][j] += cache[i][j + 1];
            }
        }

        return cache[0][0];
    }

    public static void main(String[] args) {
        log(uniquePathsWithObstacles1(new int[][]{
            new int[]{0, 0, 0},
            new int[]{0, 1, 0},
            new int[]{0, 0, 0},
        }));  // expects 2. (R->R->D->D, D->D->R->R)

        log(uniquePathsWithObstacles1(new int[][]{
            new int[]{1},
        }));  // expects 0.
    }
}