package RecursionAndBackTracking.S6_TraditionalAi;

import static Utils.Helpers.*;

import java.util.List;

/*
 * N Queens
 *
 * - 与 L51_NQueens 的题意相同，只是本题中只需返回出解的个数。
 * */

public class L52_NQueensII {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：
     * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
     * */
    public static List<List<String>> totalNQueens(int n) {
        return null;
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
