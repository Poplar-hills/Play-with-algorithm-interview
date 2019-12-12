package RecursionAndBackTracking.FloodFill;

import static Utils.Helpers.*;

/*
 * Surrounded Regions
 *
 * - Given a 2D board containing 'X' and 'O' (the letter O), capture all regions surrounded by 'X'.
 * - A region is captured by flipping all 'O's into 'X's in that surrounded region.
 * */

public class L130_SurroundedRegions {
    /*
     * 解法1：Recursion + Backtracking
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static void solve(char[][] board) {

    }

    public static void main(String[] args) {
        char[][] board1 = {
          {'X', 'X', 'X', 'X'},
          {'X', 'O', 'O', 'X'},
          {'X', 'X', 'O', 'X'},
          {'X', 'O', 'X', 'X'}
        };
        solve(board1);
        log(board1);
        /*
         * expects:
         * X X X X
         * X X X X
         * X X X X
         * X O X X
         * */
    }
}
