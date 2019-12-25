package RecursionAndBackTracking.S6_TraditionalAi;

import static Utils.Helpers.*;

/*
 * Sudoku Solver
 *
 * - Write a program to solve a Sudoku puzzle (数独问题) by filling the empty cells.
 *
 * - Note:
 *   1. The given board contain only digits 1-9 and empty character ' '.
 *   2. The given Sudoku puzzle will have a single unique solution.
 *   3. The given board size is always 9x9.
 * */

public class L37_SudokuSolver {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static void solveSudoku(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) return;
        solve(board);
    }

    private static boolean solve(char[][] board) {
        for (int m = 0; m < board.length; m++) {
            for (int n = 0; n < board[0].length; n++) {
                if (board[m][n] == ' ') {
                    for (char c = '1'; c <= '9'; c++) {
                        if (isValid(board, m, n, c)) {
                            board[m][n] = c;
                            if (solve(board)) return true;
                            board[m][n] = ' ';
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValid(char[][] board, int m, int n, char c) {
        for (int i = 0; i < 9; i++) {
            if (board[m][i] == c) return false;                          // check row
            if (board[i][n] == c) return false;                          // check column
            if (board[3 * (m / 3) + i / 3][ 3 * (n / 3) + i % 3] != ' '  // check 3*3 block
                && board[3 * (m / 3) + i / 3][3 * (n / 3) + i % 3] == c) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        char[][] board = new char[][] {
            {'5', '3', ' ',   ' ', '7', ' ',   ' ', ' ', ' '},
            {'6', ' ', ' ',   '1', '9', '5',   ' ', ' ', ' '},
            {' ', '9', '8',   ' ', ' ', ' ',   ' ', '6', ' '},

            {'8', ' ', ' ',   ' ', '6', ' ',   ' ', ' ', '3'},
            {'4', ' ', ' ',   '8', ' ', '3',   ' ', ' ', '1'},
            {'7', ' ', ' ',   ' ', '2', ' ',   ' ', ' ', '6'},

            {' ', '6', ' ',   ' ', ' ', ' ',   '2', '8', ' '},
            {' ', ' ', ' ',   '4', '1', '9',   ' ', ' ', '5'},
            {' ', ' ', ' ',   ' ', '8', ' ',   ' ', '7', '9'}
        };
        solveSudoku(board);
        log(board);
        /*
         * expects {
         *   {'5', '3', '4',   '6', '7', '8',   '9', '1', '2'},
         *   {'6', '7', '2',   '1', '9', '5',   '3', '4', '8'},
         *   {'1', '9', '8',   '3', '4', '2',   '5', '6', '7'},
         *
         *   {'8', '5', '9',   '7', '6', '1',   '4', '2', '3'},
         *   {'4', '1', '6',   '8', '5', '3',   '7', '9', '1'},
         *   {'7', '2', '3',   '9', '2', '4',   '8', '5', '6'},
         *
         *   {'9', '6', '1',   '5', '3', '7',   '2', '8', '4'},
         *   {'2', '8', '7',   '4', '1', '9',   '6', '3', '5'},
         *   {'3', '4', '5',   '2', '8', '6',   '1', '7', '9'}
         * */
    }
}
