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
     * - 思路：类似 L64 解法1，直接用 BFS 搜索能到达右下角的路径。
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
     *   - 定义子问题：f(r, c) 表示“从格子 [r,c] 到右下角格子之间的不同路径数”；
     *   - 递推表达式：f(r, c) = f(r+1, c) + f(r, c+1)。
     *        ■ → ■ → ■
     *        ↓   ↓   ↓
     *        ■ → ■ → ■
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
     *        ■ → ■ → ■
     *        ↓   ↓   ↓
     *        ■ → ■ → ■
     *        ↓   ↓   ↓
     *        ■ → ■ → ■
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
     * - 思路：既然可以用 DFS + Recursion 求解，那很可能也能用 DP 求解 —— 从右下往左上逐步递推出每个格子上的解。
     *        ■ ← ■ ← ■           3 ← 2 ← 1
     *        ↑   ↑   ↑    -->    ↑   ↑   ↑
     *        ■ ← ■ ← ■           1 ← 1 ← 1
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
     * 解法3：DP（解法2的从前到后版）
     * - 思路：解法2中的 DP 是从 DFS + Recursion 演化过来的 ∴ 递推的方向是从右下到左上。但若是从一开始就直接用 DP 求解，
     *   那最常见的思考方式是从前到后 ∴ 子问题的定义与递推表达式都与解法2不同：
     *   - 定义子问题：f(r, c) 表示“从格子 [0,0] 到格子 [r,c] 之间的不同路径数”；
     *   - 递推表达式：f(r, c) = f(r-1, c) + f(r, c-1)。
     *        ■ → ■ → ■           1 → 1 → 1
     *        ↓   ↓   ↓    -->    ↓   ↓   ↓
     *        ■ → ■ → ■           1 → 2 → 3
     * - 💎 总结：这种方式其实更能体现出 DP 的本质 —— 自下而上，先解决基本问题，再递推出高层次问题的解；而不像 DFS + Recursion
     *   思路中那样自上而下，对问题进行逐层分解（这里多体会一下）。
     * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
     * */
    public static int uniquePaths3(int m, int n) {
        if (m == 0 || n == 0) return 0;
        int[][] dp = new int[m][n];
        dp[0][0] = 1;

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (r != 0)
                    dp[r][c] += dp[r - 1][c];
                if (c != 0)
                    dp[r][c] += dp[r][c - 1];
            }
        }

        return dp[m - 1][n - 1];
    }

    /*
     * 解法4：DP（解法2的另一种实现）
     * - 思路：与解法2一致。
     * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
     * */
    public static int uniquePaths4(int m, int n) {
        if (m == 0 || n == 0) return 0;
        int[][] dp = new int[m][n];

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (r == 0 || c == 0) dp[r][c] = 1;
                else dp[r][c] = dp[r - 1][c] + dp[r][c - 1];
            }
        }

        return dp[m - 1][n - 1];
    }

    /*
     * 解法5：DP + 滚动数组（Rolling array）
     * - 思路：与解法3、4一致。
     * - 实现：从解法3、4中可见，每个格子的解都只取决于其左、上方的解，即在求解 f(r, c) 时，只需知道第 r 行和第 r-1 行里的解
     *   即可，而不需要用 m*n 大小数组维护所有行的解 ∴ 在解法4的基础上使用滚动数组进行优化：
     *     1. dp 数组只开辟2行；
     *     2. 在遍历 grid 时，若 r 为偶数，则读写 dp[0]；若 r 为奇数，则读写 dp[1]。
     *        ■ → ■ → ■
     *        ↓   ↓   ↓      遍历过程中 dp 数组的变化：
     *        ■ → ■ → ■         0 0 0  -->  1 1 1       1 1 1  -->  1 3 6  ← 这个6就是解
     *        ↓   ↓   ↓         0 0 0       0 0 0  -->  1 2 3       1 2 3
     *        ■ → ■ → ■
     * - 注意：若使用滚动数组，最好像从左上向右下遍历（如解法4），若从右下向左上遍历（如解法2、3）会很不方便。
     * - 时间复杂度 O(m*n)，空间复杂度 O(2n)。
     * */
    public static int uniquePaths5(int m, int n) {
        if (m == 0 || n == 0) return 0;
        int[][] dp = new int[2][n];

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (r == 0 || c == 0) dp[r % 2][c] = 1;
                else dp[r % 2][c] = dp[(r - 1) % 2][c] + dp[r % 2][c - 1];
            }
        }

        return dp[(m - 1) % 2][n - 1];
    }

    /*
     * 解法6：DP
     * - 思路：与解法3、4、5一致。
     * - 实现：采用 _ZeroOneKnapsack 解法4的思路再进一步优化 —— 只使用一维数组，每次对其进行覆盖。
     * - 注意：覆盖的方向要跟递推方向相同 —— 递推是从左到右，即由 f(r-1) 推出 f(r)，覆盖是从 0 到 n 进行覆盖。
     * - 时间复杂度 O(m*n)，空间复杂度 O(1n)。
     * */
    public static int uniquePaths6(int m, int n) {
        if (m == 0 || n == 0) return 0;
        int[] dp = new int[n];

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (r == 0 || c == 0) dp[c] = 1;
                else dp[c] += dp[c - 1];
            }
        }

        return dp[n - 1];
    }

    public static void main(String[] args) {
        log(uniquePaths4(2, 3));  // expects 3. (R->R->D, R->D->R, D->R->R)
        log(uniquePaths4(3, 3));  // expects 6.
        log(uniquePaths4(7, 3));  // expects 28.
    }
}