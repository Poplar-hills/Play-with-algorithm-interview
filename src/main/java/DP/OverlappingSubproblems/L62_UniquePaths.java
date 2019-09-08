package DP.OverlappingSubproblems;

import static Utils.Helpers.log;

import java.util.LinkedList;
import java.util.Queue;
import javafx.util.Pair;

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
        int[][] cache = new int[m][n];
        cache[m - 1][n - 1] = 1;

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (i != m - 1) cache[i][j] += cache[i + 1][j];
                if (j != n - 1) cache[i][j] += cache[i][j + 1];
            }
        }

        return cache[0][0];
    }

    public static void main(String[] args) {
        log(uniquePaths2(2, 3));  // expects 3. (R->R->D, R->D->R, D->R->R)
        log(uniquePaths2(7, 3));  // expects 28. 
    }
}