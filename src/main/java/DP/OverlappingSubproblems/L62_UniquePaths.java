package DP.OverlappingSubproblems;

import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;

/*
* Unique Paths
*
* - A robot is located at the top-left corner of a m x n grid (m 行 n 列). The robot can only move either down
*   or right at any point in time. The robot is trying to reach the bottom-right corner of the grid. How many
*   possible unique paths are there?
* - Note: m and n will be at most 100.
*
* - 与 L64_MinimumPathSum 的区别是 L64 是求最小成本的路径，而该题是求有多少种不同路径。
* */

public class L62_UniquePaths {
    /*
    * 超时解：BFS 全搜索
    * - 思路：类似 L64 解法1，图论建模：
    *        ■ → ■ → ■
    *        ↓   ↓   ↓
    *        ■ → ■ → ■
    * - 时间复杂度 O(2^n)，空间复杂度 O(n)，n 为节点个数。
    * */
    public static int uniquePaths(int m, int n) {
        int res = 0;
        if (m == 0 || n == 0) return res;

        Queue<Pair<Integer, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(0, 0));

        while (!q.isEmpty()) {
            Pair<Integer, Integer> pair = q.poll();
            int x = pair.getKey();
            int y = pair.getValue();

            if (x == m - 1 && y == n - 1) {
                res++;
                continue;
            }

            if (x + 1 < m) q.offer(new Pair<>(x + 1, y));
            if (y + 1 < n) q.offer(new Pair<>(x, y + 1));
        }

        return res;
    }

    /*
    * 解法1：Recursion + Memoization (DFS with cache)
    * - 思路：类似 L64 解法2。该题具有明显的重叠子问题特征 —— 前一个问题的解是基于后两个问题的解。
    *        ■ → ■ → ■         3 ← 2 ← 1
    *        ↓   ↓   ↓   -->   ↑   ↑   ↑
    *        ■ → ■ → ■         1 ← 1 ← 0
    *   - 定义子问题：f(i, j) 表示"从坐标 (i,j) 到达右下角的 unique paths 个数"；
    *   - 状态转移方程：f(i, j) = f(i+1, j) + f(i, j+1)。
    * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
    * */
    public static int uniquePaths1(int m, int n) {
        if (m == 0 || n == 0) return 0;
        int[][] cache = new int[m][n];
        return uniquePaths2(m, n, 0, 0, cache);
    }

    private static int uniquePaths2(int m, int n, int i, int j, int[][] cache) {
        if (i == m - 1 && j == n - 1) return 1;  // 注意递归到底时（即 cache[m-1][n-1]）要返回1
        if (cache[i][j] != 0) return cache[i][j];

        int res = 0;
        if (i != m - 1) res += uniquePaths2(m, n, i + 1, j, cache);
        if (j != n - 1) res += uniquePaths2(m, n, i, j + 1, cache);

        return cache[i][j] = res;
    }

    /*
    * 解法2：DP
    * - 思路：类似 L64 解法3，从后往前求解。
    * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
    * */
    public static int uniquePaths2(int m, int n) {
        if (m == 0 || n == 0) return 0;
        int[][] dp = new int[m][n];
        dp[m - 1][n - 1] = 1;

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (i != m - 1) dp[i][j] += dp[i + 1][j];
                if (j != n - 1) dp[i][j] += dp[i][j + 1];
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

    public static void main(String[] args) {
        log(uniquePaths6(2, 3));  // expects 3. (R->R->D, R->D->R, D->R->R)
        log(uniquePaths6(3, 3));  // expects 6.
        log(uniquePaths6(7, 3));  // expects 28.
    }
}