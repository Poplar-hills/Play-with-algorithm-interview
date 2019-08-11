package DP.OverlappingSubproblems;

import static Utils.Helpers.log;

import java.util.LinkedList;
import java.util.Queue;
import javafx.util.Pair;

/*
* Unique Paths
*
* - A robot is located at the top-left corner of a m x n grid. The robot can only move either down or right
*   at any point in time. The robot is trying to reach the bottom-right corner of the grid. How many possible
*   unique paths are there?
* - Note: m and n will be at most 100.
* */

public class L62_UniquePaths {
    /*
    * 超时解：BFS 全搜索
    * - 思路：图论建模
    *   ■ → ■ → ■
    *   ↓   ↓   ↓
    *   ■ → ■ → ■
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

            if (x + 1 < m)
                q.offer(new Pair<>(x + 1, y));
            if (y + 1 < n)
                q.offer(new Pair<>(x, y + 1));
        }

        return res;
    }

    /*
    * 解法1：Recursion + Memoization (DFS)
    * - 思路：全局解 = 局部解之和
    * - 时间复杂度 O()，空间复杂度 O()。
    * */
    public static int uniquePaths2(int m, int n) {
        if (m == 0 || n == 0) return 0;
        int[][] cache = new int[m][n];
        return uniquePaths2(m, n, 0, 0, cache);
    }

    private static int uniquePaths2(int m, int n, int x, int y, int[][] cache) {
        if (x == m - 1 && y == n - 1) return 1;
        if (cache[x][y] != 0) return cache[x][y];

        int res = 0;
        if (x + 1 < m)
            res += uniquePaths2(m, n, x + 1, y, cache);
        if (y + 1 < n)
            res += uniquePaths2(m, n, x, y + 1, cache);

        return cache[x][y] = res;
    }

    public static void main(String[] args) {
        log(uniquePaths2(3, 2));  // expects 3.  (R->R->D, R->D->R, D->R->R)
        log(uniquePaths2(7, 3));  // expects 28. ...
    }
}