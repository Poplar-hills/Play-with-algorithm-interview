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
     * - 思路：Very straight-forward solution. 唯一比较难懂的是根据一个坐标 [m,n] 找到其所在的 3*3 block 里的所有坐标。
     * - 时间复杂度 O(9^c)，其中 c 为 board 填充前空格的个数：c 个空格需要填充，而个格都要尝试9个数字 ∴ 是 O(9^c)；
     * - 空间复杂度 O(9*9)。
     * */
    public static void solveSudoku(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) return;
        solve(board);
    }

    private static boolean solve(char[][] board) {
        for (int m = 0; m < board.length; m++) {
            for (int n = 0; n < board[0].length; n++) {
                if (board[m][n] == ' ') {
                    for (char c = '1'; c <= '9'; c++) {  // 对 board 上的每个空格尝试用 '1'~'9' 填充
                        if (isValid(board, m, n, c)) {   // 前提是 [m,n] 所在的行、列、3*3 block 中 c 还未被使用过
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
        int blkRow = (m / 3) * 3, blkCol = (n / 3) * 3;  // m/3 ∈ [0,1,2], (m/3)*3 ∈ [0,3,6] 即 block 中的第一列
        for (int i = 0; i < 9; i++)                      // 检查 [m,n] 所在的行、列、3*3 block
            if (board[m][i] == c || board[i][n] == c || board[blkRow + i / 3][blkCol + i % 3] == c)
                return false;
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
