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
     * - 思路：与 L51_NQueens 解法2一致，只是在找到解时计数即可，无需记录皇后的位置。
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

    private static void putQueue(int n, int r) {
        if (r == n) {
            count++;
            return;
        }
        for (int c = 0; c < n; c++) {
            if (!col[c] && !dia1[r + c] && !dia2[r - c + n - 1]) {
                col[c] = dia1[r + c] = dia2[r - c + n - 1] = true;
                putQueue(n, r + 1);
                col[c] = dia1[r + c] = dia2[r - c + n - 1] = false;
            }
        }
    }

    public static void main(String[] args) {
        log(totalNQueens(4));  // expects 2.
        log(totalNQueens(5));  // expects 10.
    }
}
