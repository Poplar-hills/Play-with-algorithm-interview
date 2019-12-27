package RecursionAndBackTracking.S6_TraditionalAi;

import static Utils.Helpers.*;

/*
 * Sudoku Solver
 *
 * - Write a program to solve a Sudoku puzzle (数独问题) by filling the empty cells.
 *
 * - Note:
 *   1. The given board contain only digits 1-9 and the character '.' which indicates an empty cell.
 *   2. The given Sudoku puzzle will have a single unique solution.
 *   3. The given board size is always 9x9.
 * */

public class L37_SudokuSolver {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：Very straight-forward solution.
     * - 时间复杂度 O(9^n)，其中 n 为 board 填充前空格的个数：n 个空格需要填充，而个格都要尝试9个数字 ∴ 是 O(9^n)；
     * - 空间复杂度 O(9*9)。
     * */
    public static void solveSudoku(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) return;
        solve(board);
    }

    private static boolean solve(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == '.') {
                    for (char c = '1'; c <= '9'; c++) {  // 对 board 上的每个空格尝试用 '1'~'9' 填充
                        if (isValid(board, i, j, c)) {   // 前提是 [i,j] 所在的行、列、3*3 block 中 n 还未被使用过
                            board[i][j] = c;
                            if (solve(board)) return true;
                            board[i][j] = '.';
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValid(char[][] board, int i, int j, char c) {
        int blkRow = i / 3 * 3, blkCol = j / 3 * 3;  // i/3 ∈ [0,1,2]；i/3*3 ∈ [0,3,6] 即每个 block 的起始列
        for (int n = 0; n < 9; n++)                  // 检查 [i,j] 所在的行、列、block
            if (board[i][n] == c || board[n][j] == c || board[blkRow + n / 3][blkCol + n % 3] == c)
                return false;
        return true;
    }

    /*
     * 解法2：Recursion + Backtracking (时间复杂度优化版)
     * - 思路：整体思路与解法1一致，仍然采用回溯搜索求解。
     * - 实现：解法1用 isValid 方法判断某一数字 c 是否能填入某一格子 [i,j]，具体检查方式为遍历 [i,j] 所在的行、列、block。
     *   若借鉴 L51_NQueens 解法1中的3个 boolean[]，可以对该检查过程进行优化，通过空间换时间的方式以 O(1) 的复杂度完成检查。
     *   但具体实现不同于 L51，该问题中需要对每行、每列、每个 block 都维护单独的 boolean[] 以保证他们各自中都没有重复 ∴ 需要
     *   创建的是3个 9*9 的二维数组：
     *     1. row[i][n] 表示第 i 行里是否已有数字 n，其中 i ∈ [0,8], n ∈ [1,9]；
     *     2. col[i][n] 表示第 i 列里是否已有数字 n，其中 i ∈ [0,8], n ∈ [1,9]；
     *     3. block[i][n] 表示第 i 个 block 里是否已有数字 n，其中 i ∈ [0,8], n ∈ [1,9]。
     *   基于该思路可得到程序的整体逻辑：
     *     1. 先遍历 board 上已有的数字，并将 row、col、block 对应的位置置为 true；
     *     2. 再用解法1中的方式，对 board 上的空格进行回溯填充。
     * - 时间复杂度 O(9^c)，其中 c 为 board 填充前空格的个数：c 个空格需要填充，而个格都要尝试9个数字 ∴ 是 O(9^c)；
     * - 空间复杂度 O(9*9)。
     * */
    private static boolean[][] row, col, block;

    public static void solveSudoku2(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) return;
        row = new boolean[9][10];
        col = new boolean[9][10];
        block = new boolean[9][10];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    int n = board[i][j] - '0';  // 将 char 转化为 int（'5' - '0' = 5）
                    row[i][n] = true;
                    col[j][n] = true;
                    block[i / 3 * 3 + j / 3][n] = true;  // i/3 ∈ [0,1,2]；i/3*3 ∈ [0,3,6]；i/3*3+j/3 ∈ [0,1,2,3,4,5,6,7,8]
                }
            }
        }

        dfs(board, 0);
    }

    private static boolean dfs(char[][] board, int cellIdx) {
        if (cellIdx == 81)
            return true;
        int i = cellIdx / 9;
        int j = cellIdx % 9;
        int k = i / 3 * 3 + j / 3;

        if (board[i][j] != '.')
            return dfs(board, cellIdx + 1);

        for (int n = 1; n <= 9; n++) {
            if (!row[i][n] && !col[j][n] && !block[k][n]) {
                board[i][j] = (char) n;
                row[i][n] = col[j][n] = block[k][n] = true;
                if (dfs(board, cellIdx + 1)) return true;
                board[i][j] = '.';
                row[i][n] = col[j][n] = block[k][n] = false;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        char[][] board = new char[][] {
            {'5', '3', '.',   '.', '7', '.',   '.', '.', '.'},
            {'6', '.', '.',   '1', '9', '5',   '.', '.', '.'},
            {'.', '9', '8',   '.', '.', '.',   '.', '6', '.'},

            {'8', '.', '.',   '.', '6', '.',   '.', '.', '3'},
            {'4', '.', '.',   '8', '.', '3',   '.', '.', '1'},
            {'7', '.', '.',   '.', '2', '.',   '.', '.', '6'},

            {'.', '6', '.',   '.', '.', '.',   '2', '8', '.'},
            {'.', '.', '.',   '4', '1', '9',   '.', '.', '5'},
            {'.', '.', '.',   '.', '8', '.',   '.', '7', '9'}
        };
        solveSudoku2(board);
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
