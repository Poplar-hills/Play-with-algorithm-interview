package DP.S2_OverlappingSubproblems;

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
     *        ■ → ■ → ■         2 ← 1 ← 1
     *        ↓       ↓         ↑       ↑
     *        ■   □   ■   -->   1   0   1
     *        ↓       ↓         ↑       ↑
     *        ■ → ■ → ■         1 ← 1 ← 1
     * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。∵ Recursion 是自顶向下递归到底后再自底向上返回顶部 ∴ 用时会比只自底向上遍历的解法2慢。
     * */
    public static int uniquePathsWithObstacles(int[][] grid) {
        if (grid == null || grid[0] == null) return 0;
        int[][] cache = new int[grid.length][grid[0].length];
        return helper(grid, 0, 0, cache);
    }

    private static int helper(int[][] grid, int r, int c, int[][] cache) {
        int m = grid.length;
        int n = grid[0].length;

        if (grid[r][c] == 1) return 0;             // 若是障碍物节点则直接返回0
        if (r == m - 1 && c == n - 1) return 1;    // 递归到底
        if (cache[r][c] != 0) return cache[r][c];

        int res = 0;
        if (r != m - 1) res += helper(grid, r + 1, c, cache);
        if (c != n - 1) res += helper(grid, r, c + 1, cache);

        return cache[r][c] = res;
    }

    /*
     * 解法1：DP
     * - 思路：在 L62 解法2的基础上加上对障碍物的判断即可。
     * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
     * */
    public static int uniquePathsWithObstacles1(int[][] grid) {
        if (grid == null || grid[0] == null) return 0;

        int m = grid.length;
        int n = grid[0].length;
        int[][] dp = new int[m][n];
        dp[m - 1][n - 1] = 1;

        for (int r = m - 1; r >= 0; r--) {
            for (int c = n - 1; c >= 0; c--) {
                if (grid[r][c] == 1) {  // 若遇到障碍则直接将障碍点的结果记为0
                    dp[r][c] = 0;
                    continue;
                }
                if (r != m - 1) dp[r][c] += dp[r + 1][c];
                if (c != n - 1) dp[r][c] += dp[r][c + 1];
            }
        }

        return dp[0][0];
    }

    /*
     * 解法2：DP + 一维数组
     * - 思路：类似 L62 解法6的思路，但不同点在于 ∵ grid 中有障碍物 ∴ L62 解法4、5、6里的 "if (r==0 || c==0) dp[c] = 1;"
     *   不再成立（∵ dp[障碍物] = 0）∴ 只能用 L62 解法3的方式实现。
     * - 时间复杂度 O(m*n)，空间复杂度 O(n)。
     * */
    public static int uniquePathsWithObstacles2(int[][] grid) {
        if (grid == null || grid[0] == null) return 0;

        int m = grid.length;
        int n = grid[0].length;
        int[] dp = new int[n];
        dp[0] = 1;

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == 1) {
                    dp[c] = 0;
                    continue;
                }
                if (c != 0) dp[c] += dp[c - 1];
            }
        }

        return dp[n - 1];
    }

    public static void main(String[] args) {
        log(uniquePathsWithObstacles(new int[][]{
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0},
        }));  // expects 2. (R->R->D->D, D->D->R->R)

        log(uniquePathsWithObstacles(new int[][]{
            {0, 0, 1, 0},
            {0, 0, 0, 0},
            {1, 1, 1, 0},
            {0, 0, 0, 0},
        }));  // expects 2.

        log(uniquePathsWithObstacles(new int[][]{
            {1},
        }));  // expects 0.
    }
}