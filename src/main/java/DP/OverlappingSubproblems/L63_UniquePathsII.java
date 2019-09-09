package DP.OverlappingSubproblems;

import static Utils.Helpers.log;
import static Utils.Helpers.timeIt;

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

    private static int helper(int[][] grid, int i, int j, int[][] cache) {
        int m = grid.length;
        int n = grid[0].length;

        if (grid[i][j] == 1) return 0;             // 若是障碍物节点则直接返回0
        if (i == m - 1 && j == n - 1) return 1;    // 递归到底
        if (cache[i][j] != 0) return cache[i][j];

        int res = 0;
        if (i != m - 1 && grid[i + 1][j] != 1)     // 若下方是障碍物，则不计算
            res += helper(grid, i + 1, j, cache);
        if (j != n - 1 && grid[i][j + 1] != 1)     // 若右侧是障碍物，则不计算
            res += helper(grid, i, j + 1, cache);

        return cache[i][j] = res;
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

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (grid[i][j] == 1) {  // 若遇到障碍则直接将障碍点的结果记为0
                    dp[i][j] = 0;
                    continue;
                }
                if (i != m - 1) dp[i][j] += dp[i + 1][j];
                if (j != n - 1) dp[i][j] += dp[i][j + 1];
            }
        }

        return dp[0][0];
    }

    /*
    * 解法2：DP + 一维数组
    * - 思路：类似 L62 解法6的思路，但不同点在于：
    *   1. ∵ grid 中有障碍物 ∴ L62 解法4、5、6里的`if (i == 0 || j == 0) dp[j] = 1;`不再成立，只能用 L62 解法3的实现方式；
    *   2. ∵ 采用了一维 dp 数组 ∴ 当 i != 0 时，dp[j] 保持不变即可，不用再加任何数值。
    * - 时间复杂度 O(m*n)，空间复杂度 O(n)。
    * */
    public static int uniquePathsWithObstacles2(int[][] grid) {
        if (grid == null || grid[0] == null) return 0;

        int m = grid.length;
        int n = grid[0].length;
        int[] dp = new int[n];
        dp[0] = 1;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    dp[j] = 0;
                    continue;
                }
                if (j != 0) dp[j] += dp[j - 1];  // 只考虑 j != 0 的情况就行；i != 0 的情况 dp[j] 保持不变即可
            }
        }

        return dp[n - 1];
    }

    public static void main(String[] args) {
        log(uniquePathsWithObstacles2(new int[][]{
            new int[]{0, 0, 0},
            new int[]{0, 1, 0},
            new int[]{0, 0, 0},
        }));  // expects 2. (R->R->D->D, D->D->R->R)

        log(uniquePathsWithObstacles2(new int[][]{
            new int[]{0, 0, 1, 0},
            new int[]{0, 0, 0, 0},
            new int[]{1, 1, 1, 0},
            new int[]{0, 0, 0, 0},
        }));  // expects 2.

        log(uniquePathsWithObstacles2(new int[][]{
            new int[]{1},
        }));  // expects 0.
    }
}