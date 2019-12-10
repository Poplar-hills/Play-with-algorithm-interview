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
     * - 思路：整体思路是对 board 中的格子进行遍历，看以每个格子为起点是否能找到 word 中的所有字母。例如对于对于 board =
     *        | A | B | C | E |， word="EDFC" 来说，先对 board 进行从左上到右下的遍历，找到的第一个 E 在右上角，但找不到
     *        | S | F | C | S |   第二个字母 D ∴ 不是解，继续遍历。而从找到的第二个 E 开始能走完 word 中的所有字母 ∴ 是解。
     *        | A | D | E | E |
     *   在以每个格子为起点搜索 word 中的字母时（例如找到的第二个 E），过程为：
     *                 E
     *         ↑/     ←|     →\
     *         C       D       E
     *            ↑/  ←|  →\
     *            F    A    E
     *        ↑/↓|←|→\
     *        B  D S  C
     *   可见该过程是在树型结构中进行搜索 ∴ 可使用回溯法。
     * - 时间复杂度 O()，空间复杂度 O()。
     * */

    private static int w, h;
    private static boolean[][] visited = new boolean[h][w];

    public static boolean exist(char[][] board, String word) {
        if (word == null || word.isEmpty() || board == null || board.length == 0) return false;
        w = board[0].length;
        h = board.length;

        for (int y = 0; y < h; y++)            // 第一阶段：遍历 board 上的每个格子
            for (int x = 0; x < w; x++)
                if (searchForWord(board, word, 0, x, y))  // 第二阶段：以每个格子为起点搜索 word 中的字母
                    return true;
        return false;
    }

    private static boolean searchForWord(char[][] board, String word, int i, int x, int y) {
        if (i == word.length()) return true;
        if (board[y][x] != word.charAt(i)) return false;

        boolean up = false, down = false, left = false, right = false;
        if (x + 1 < w)
            up = searchForWord(board, word, i + 1, x + 1, y);
        if (x - 1 >= 0)
            down = searchForWord(board, word, i + 1, x - 1, y);
        if (y + 1 < h)
            left = searchForWord(board, word, i + 1, x, y + 1);
        if (y - 1 >= 0)
            right = searchForWord(board, word, i + 1, x, y - 1);
        return up || down || left || right;
    }

    /*
     * 解法2：DP - 思路：f() - 时间复杂度 O()，空间复杂度 O()。
     */
    public static boolean exist2(char[][] board, String word) {

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
