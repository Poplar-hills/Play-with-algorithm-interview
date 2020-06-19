package DP.S2_OverlappingSubproblems;

import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;

/*
 * Unique Paths
 *
 * - A robot is located at the top-left corner of a m x n grid (m 行 n 列). The robot can only move either
 *   down or right at any point in time. The robot is trying to reach the bottom-right corner of the grid.
 *   How many possible unique paths are there?
 *
 * - Note: m and n will be at most 100.
 *
 * - 与 L64_MinimumPathSum 的区别是 L64 是求最小成本的路径，而该题是求不同的路径数。
 * */

public class L62_UniquePaths {
    /*
     * 超时解1：BFS
     * - 思路：类似 L64 解法1，图论建模：
     *        ■ → ■ → ■
     *        ↓   ↓   ↓
     *        ■ → ■ → ■
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)，n 为节点个数。
     * */
    public static int uniquePaths_1(int m, int n) {
        int numOfPath = 0;
        if (m == 0 || n == 0) return numOfPath;

        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{0, 0});

        while (!q.isEmpty()) {
            int[] pos = q.poll();
            int r = pos[0], c = pos[1];

            if (r == m - 1 && c == n - 1) {
                numOfPath++;
                continue;
            }

            if (r + 1 < m) q.offer(new int[]{r + 1, c});
            if (c + 1 < n) q.offer(new int[]{r, c + 1});
        }

        return numOfPath;
    }

    /*
     * 超时解2：DFS + Recursion
     * - 思路：若用 DFS + 递归求解，则需思考前后子问题之间的递推关系，即 f(r, c) 与 f(r+1, c)、f(r, c+1) 之间的递推关系：
     *   - 定义子问题：f(r, c) 表示“从格子 [r,c] 到右下角格子之间的不同路径个数”；
     *   - 递推表达式：f(r, c) = f(r+1, c) + f(r, c+1)。
     *        ■ → ■ → ■         3 → 2 → 1
     *        ↓   ↓   ↓   -->   ↓   ↓   ↓
     *        ■ → ■ → ■         1 → 1 → 0
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)，n 为节点个数。
     * */
    public static int uniquePaths_2(int m, int n) {
        if (m == 0 || n == 0) return 0;
        return helper_2(m, n, 0, 0);
    }

    private static int helper_2(int m, int n, int r, int c) {
        if (r == m || c == n) return 0;
        if (r == m - 1 && c == n - 1) return 1;
        return helper_2(m, n, r + 1, c) + helper_2(m, n, r, c + 1);
    }

    /*
     * 解法1：Recursion + Memoization (DFS with cache)
     * - 思路：类似 L64_MinimumPathSum 解法2。该题具有明显的重叠子问题特征，比如在 3×3 的 gird 上：
     *            ■ → ■ → ■
     *            ↓   ↓   ↓
     *            ■ → ■ → ■
     *            ↓   ↓   ↓
     *            ■ → ■ → ■
     *   所有有两个箭头指向的格子，如 f(1,1)、f(1,2)、f(2,1)、f(2,2) 会被重复计算2次 ∴ 可以使用 Memoization 进行优化。
     * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
     * */
    public static int uniquePaths1(int m, int n) {
        if (m == 0 || n == 0) return 0;
        int[][] cache = new int[m][n];
        return helper1(m, n, 0, 0, cache);
    }

    private static int helper1(int m, int n, int r, int c, int[][] cache) {
        if (r == m - 1 && c == n - 1) return 1;
        if (cache[r][c] != 0) return cache[r][c];

        int res = 0;
        if (r != m - 1) res += helper1(m, n, r + 1, c, cache);
        if (c != n - 1) res += helper1(m, n, r, c + 1, cache);

        return cache[r][c] = res;
    }

    /*
     * 解法2：DP
     * - 思路：既然可以用 DFS + Recursion 求解，那很可能也能用 DP 求解 —— 自下而上递推出每个格子上的解。
     * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
     * */
    public static int uniquePaths2(int m, int n) {
        if (m == 0 || n == 0) return 0;
        int[][] dp = new int[m][n];
        dp[m - 1][n - 1] = 1;

        for (int r = m - 1; r >= 0; r--) {
            for (int c = n - 1; c >= 0; c--) {
                if (r != m - 1)
                    dp[r][c] += dp[r+1][c];
                if (c != n - 1)
                    dp[r][c] += dp[r][c+1];
            }
        }

        return dp[0][0];
    }

    /*
     * 解法3：DP（解法2的反向遍历版）
     * - 思路：不同于解法2，本解法是是从左上遍历到右下 ∴ 状态转移方程变成了 f(i, j) = f(i-1, j) + f(i, j-1)。
     * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
     * */
    public static int uniquePaths3(int m, int n) {
        if (m == 0 || n == 0) return 0;
        int[][] dp = new int[m][n];
        dp[0][0] = 1;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i != 0) dp[i][j] += dp[i - 1][j];
                if (j != 0) dp[i][j] += dp[i][j - 1];
            }
        }

        return dp[m - 1][n - 1];
    }

    /*
     * 解法4：DP（解法3的另一种实现）
     * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
     * */
    public static int uniquePaths4(int m, int n) {
        if (m == 0 || n == 0) return 0;
        int[][] dp = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || j == 0) dp[i][j] = 1;
                else dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }

        return dp[m - 1][n - 1];
    }

    /*
     * 解法5：DP + 滚动数组（Rolling array）
     * - 思路：在解法3、4中 ∵ 每个坐标的计算结果都只取决于该坐标左侧及上方的结果（状态转移方程也说明的了这一点）∴ 在计算 f(i, j)
     *   时只需要第 i 行和第 i-1 行的计算结果即可，而不再需要维护整个二维表 ∴ 可在解法3或4的基础上使用类似 _ZeroOneKnapsack
     *   解法3中的滚动数组进行优化：当 i 为偶数时，读写 dp[0]；当 i 为奇数时，读写 dp[1]。
     * - 注意：若使用滚动数组，最好像从左上向右下遍历（如解法3、4），若从右下向左上遍历（如解法2）会有问题。
     * - 时间复杂度 O(m*n)，空间复杂度 O(2n)。
     * */
    public static int uniquePaths5(int m, int n) {
        if (m == 0 || n == 0) return 0;
        int[][] dp = new int[2][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || j == 0) dp[i % 2][j] = 1;
                else dp[i % 2][j] = dp[(i - 1) % 2][j] + dp[i % 2][j - 1];
            }
        }

        return dp[(m - 1) % 2][n - 1];
    }

    /*
     * 解法6：DP
     * - 思路：采用 _ZeroOneKnapsack 解法4的思路再进一步优化 —— 只使用一维数组，每次对其进行覆盖。
     * - 注意：覆盖的方向要跟递推方向相同 —— 递推是从左到右（由 f(i-1) 推出 f(i)），覆盖是从 0 到 n 进行覆盖。
     * - 时间复杂度 O(m*n)，空间复杂度 O(1n)。
     * */
    public static int uniquePaths6(int m, int n) {
        if (m == 0 || n == 0) return 0;
        int[] dp = new int[n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || j == 0) dp[j] = 1;
                else dp[j] += dp[j - 1];
            }
        }

        return dp[n - 1];
    }





    public static int uniquePathsxxx(int m, int n) {
        return 0;
    }

    public static void main(String[] args) {
        log(uniquePaths2(2, 3));  // expects 3. (R->R->D, R->D->R, D->R->R)
        log(uniquePaths2(3, 3));  // expects 6.
        log(uniquePaths2(7, 3));  // expects 28.
    }
}