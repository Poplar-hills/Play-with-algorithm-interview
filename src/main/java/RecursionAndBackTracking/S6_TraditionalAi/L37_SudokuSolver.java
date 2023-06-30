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
     * 解法1：Backtracking + Brute Force
     * - 思路：Very straight-forward solution：
     *     1. 扫描 board，找到第一个是'.'的格子；
     *     2. 尝试用1-9去填充该格子（遍历检查要填充的数字是否在该 row、col、block 中存在）；
     *     3. 填充后进入下层递归，回到步骤1 —— 再从头扫描 board，找到下一个是'.'的格子进行填充；
     *     4. 若一个格子无法填充1-9中的任何数字，则说明之前的填充方式无解，于是回到上层更换数字再继续尝试；
     *     5. 当整个 board 遍历完毕之后（说明不再有是'.'的格子，即找到解），则返回 true。
     * - 时间复杂度 O(9^n)，其中 n 为 board 填充前空格的个数：n 个空格需要填充，每个格都要尝试9个数字 ∴ 是 O(9^n)；
     * - 空间复杂度 O(9*9)。
     * */
    public static void solveSudoku(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) return;
        backtracking(board);
    }

    private static boolean backtracking(char[][] board) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] == '.') {                // 找到一个起始 '.' 后开始 backtracking
                    for (char n = '1'; n <= '9'; n++) {  // 对 board 上的每个空格尝试用 '1'~'9' 填充
                        if (isValid(board, r, c, n)) {   // 前提是 [r,c] 所在的行、列、3*3 block 中 n 还未被使用过
                            board[r][c] = n;
                            if (backtracking(board)) return true;  // 当所有层递归都返回 true（即找到数独的解）时，中断回溯
                            board[r][c] = '.';
                        }
                    }
                    return false;  // 若1-9都不能放，说明当前的落子无解，需返回上一层，继续尝试。
                }
            }
        }
        return true;  // 当 board 上不再有 '.' 时说明找到了解
    }

    private static boolean isValid(char[][] board, int r, int c, char n) {
        int blkRow = r / 3 * 3, blkCol = c / 3 * 3;  // ∵ r ∈ [0,9] ∴ r/3 ∈ [0,1,2]；然后再*3就 ∈ [0,3,6]，即每个 block 的起始行（c 同理）
        for (int i = 0; i < 9; i++)                  // 检查 [r,c] 所在的行、列、block
            if (board[r][i] == n || board[i][c] == n || board[blkRow + i / 3][blkCol + i % 3] == n)
                return false;
        return true;
    }

    /*
     * 解法2：Backtracking (解法1的时间优化版)
     * - 思路：与解法1一致。
     * - 实现：借鉴 L51_NQueens 解法1中的3个 boolean[]，对解法1中的 isValid 方法进行优化，以空间换时间的方式用 O(1) 的
     *   复杂度完成检查。但不同于 L51，该问题中需要对每行、每列、每个 block 都维护单独的 boolean[] 以检查是否有重复数字出现
     *   （相当于为每行、每列、每个 block 都创建了一个 Set）∴ 需要创建3个 9*9 的二维数组：
     *     1. row[r][n] 表示第 r 行中是否已有数字 n，其中 r ∈ [0,8], n ∈ [1,9]；
     *     2. col[c][n] 表示第 c 列中是否已有数字 n，其中 c ∈ [0,8], n ∈ [1,9]；
     *     3. block[b][n] 表示第 b 个 block 中是否已有数字 n，其中 b ∈ [0,8], n ∈ [1,9]。
     *   整体流程：
     *     1. 先根据 board 上已有的数字初始化 row、col、block 三个数组；
     *     2. 再用解法1中的方式，对 board 上的空格进行回溯填充。
     *   其中另一点不同于解法1的是，在找到下一个是'.'的格子时，不再每次都从头遍历 board，而是使用格子的 index 编号。
     * - 时间复杂度 O(9^n)，其中 n 为 board 填充前空格的个数（与解法一在量级上相同，但实际效率高很多）；
     * - 空间复杂度 O(9*9)。
     * */
    private static boolean[][] row, col, block;

    public static void solveSudoku2(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) return;
        init(board);
        solve2(board, 0);  // 再次遍历 board，对空格进行回溯填充
    }

    private static void init(char[][] board) {
        row = new boolean[9][10];
        col = new boolean[9][10];
        block = new boolean[9][10];

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] != '.') {       // 先遍历 board 上已有的数字
                    int n = board[r][c] - '0';  // 将 char 转化为 int（'5'-'0' = 5，即在 ASCII 中 '5' 与 5 相差48）
                    row[r][n] = col[c][n] = block[r / 3 * 3 + c / 3][n] = true;
                }                               // r/3 ∈ [0,1,2]；r/3*3 ∈ [0,3,6]；r/3*3+c/3 ∈ [0,1,2,3,4,5,6,7,8]
            }
        }
    }

    private static boolean solve2(char[][] board, int cellIdx) {
        if (cellIdx == 81) return true;
        int r = cellIdx / 9;
        int c = cellIdx % 9;
        int b = r / 3 * 3 + c / 3;

        if (board[r][c] != '.')
            return solve2(board, cellIdx + 1);

        for (int n = 1; n <= 9; n++) {
            if (!row[r][n] && !col[c][n] && !block[b][n]) {
                board[r][c] = (char) (n + '0');  // 将 int 转化为 char（不能直接强转，需要加'0'，即 ASCII 中5与'5'相差48）
                row[r][n] = col[c][n] = block[b][n] = true;
                if (solve2(board, cellIdx + 1)) return true;
                row[r][n] = col[c][n] = block[b][n] = false;
                board[r][c] = '.';
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
