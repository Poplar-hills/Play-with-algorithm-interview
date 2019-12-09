package RecursionAndBackTracking.TwoDimensionalBackTracking;

import static Utils.Helpers.*;

/*
 * Word Search
 *
 * - Given a 2D board and a word, find if the word exists in the grid. The word can be constructed from
 *   letters of sequentially adjacent cell, where "adjacent" cells are those horizontally or vertically
 *   neighboring. The same letter cell may not be used more than once.
 * */

public class L79_WordSearch {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static boolean exist(char[][] board, String word) {
        return true;
    }


    public static void main(String[] args) {
        char[][] board = {
          {'A', 'B', 'C', 'E'},
          {'S', 'F', 'C', 'S'},
          {'A', 'D', 'E', 'E'}
        };

        log(exist(board, "ABCCED"));  // expects true.
        log(exist(board, "SEE"));     // expects true.
        log(exist(board, "ABCB"));    // expects false.
    }
}
