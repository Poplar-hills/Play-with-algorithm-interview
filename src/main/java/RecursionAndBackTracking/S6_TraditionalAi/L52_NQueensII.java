package RecursionAndBackTracking.S6_TraditionalAi;

import static Utils.Helpers.*;

/*
 * N Queens
 *
 * - 与 L51_NQueens 的题意相同，只是本题中只需返回出解的个数。
 * */

public class L52_NQueensII {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：与 L51_NQueens 解法1相同，只是在找到具体解时计数即可，无需记录皇后的具体放置位置。
     * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
     * */
    private static int count = 0;
    private static boolean[] col, dia1, dia2;

    public static int totalNQueens(int n) {
        if (n == 0) return 0;
        col = new boolean[n];
        dia1 = new boolean[2 * n - 1];
        dia2 = new boolean[2 * n - 1];
        putQueue(n, 0);
        return count;
    }

    private static void putQueue(int n, int i) {
        if (i == n) {
            count++;
            return;
        }
        for (int j = 0; j < n; j++) {
            if (!col[j] && !dia1[i + j] && !dia2[i - j + n - 1]) {
                col[j] = dia1[i + j] = dia2[i - j + n - 1] = true;
                putQueue(n, i + 1);
                col[j] = dia1[i + j] = dia2[i - j + n - 1] = false;
            }
        }
    }

    public static void main(String[] args) {
        log(totalNQueens(4));
        /*
         * expects 2.
         *   [".Q..",  // 解1
         *    "...Q"
         *    "Q..."
         *    "..Q."],
         *
         *   ["..Q.",  // 解2
         *    "Q..."
         *    "...Q"
         *    ".Q.."]
         * */
    }
}
