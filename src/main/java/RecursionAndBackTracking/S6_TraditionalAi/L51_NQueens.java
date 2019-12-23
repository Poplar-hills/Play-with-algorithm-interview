package RecursionAndBackTracking.S6_TraditionalAi;

import static Utils.Helpers.*;

import java.util.List;

/*
 * N Queens
 *
 * - The n-queens puzzle is the problem of placing n queens on an n×n chessboard such that no two queens
 *   attack each other (in horizontal, vertical or diagonal direction).
 *
 * - Given an integer n, return all distinct solutions to the n-queens puzzle. Each solution contains a
 *   distinct board configuration of the n-queens' placement, where 'Q' and '.' both indicate a queen and an
 *   empty space respectively.
 * */

public class L51_NQueens {
    /*
     * 解法1：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<String>> solveNQueens(int n) {
        return null;
    }

    public static void main(String[] args) {
        log(solveNQueens(4));
        /*
         * expects [   // 4皇后问题有两个解
         *   [".Q..",
         *    "...Q"
         *    "Q..."
         *    "..Q."],
         *
         *   ["..Q.",
         *    "Q..."
         *    "...Q"
         *    ".Q.."]
         * ]
         * */
    }
}
