package RecursionAndBackTracking.S4_TwoDimensionalBackTracking;

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
     *        [ A, B, C, E ]， word="EDFC" 来说，先对 board 进行从左上到右下的遍历，找到的第一个 E 在右上角，但找不到
     *        [ S, F, C, S ]   第二个字母 D ∴ 不是解，继续遍历。而第二个 E 开始能走完 word 中的所有字母 ∴ 是解。
     *        [ A, D, E, E ]
     *   在以每个格子为起点搜索 word 中的字母时（例如找到的第二个 E），过程为：
     *                 E
     *           ↑/   ←|   →\       - 往↓会越界 ∴ 没有往↓的分支
     *           C     D     E
     *              ↑/  ←\          - 往→会走到已走过的格子 ∴ 没有往→的分支
     *              F    A
     *           ↑/←|→\             - 往↓会走到已走过的格子 ∴ 没有往↓的分支
     *           B  S  C
     * - 💎 技巧：directions 位移数组是平面搜索问题的常用技巧，但要注意搜索方向的是否需要有顺序性，如顺时针、只往右下搜索等限制。
     * - 时间复杂度 O(w*l * 4^n)，空间复杂度 O(w*l)，其中 w,l 为 board 的高和宽，n 为 word 的长度。
     * */

    private static int l, w;
    private static boolean[][] visited;
    private static int[][] directions = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};

    public static boolean exist(char[][] board, String word) {
        if (word == null || word.isEmpty() || board == null || board.length == 0)
            return false;

        l = board.length;
        w = board[0].length;
        visited = new boolean[l][w];

        for (int r = 0; r < l; r++)                       // 第1步：遍历 board 上的每个格子
            for (int c = 0; c < w; c++)
                if (searchForWord(board, r, c, word, 0))  // 第2步：以每个格子为起点搜索 word
                    return true;
        return false;
    }

    private static boolean searchForWord(char[][] board, int r, int c, String word, int i) {
        if (i == word.length() - 1)                // 先处理 test case 2 的情况（board 只有一个格时不会进入下一层递归）
            return board[r][c] == word.charAt(i);

        if (i == word.length()) return true;       // 再处理正常情况
        if (board[r][c] != word.charAt(i)) return false;

        visited[r][c] = true;
        for (int[] d : directions) {
            int newR = r + d[0], newC = c + d[1];
            if (validPos(newR, newC) && !visited[newR][newC])
                if (searchForWord(board, newR, newC, word, i + 1))  // 若没有有效的相邻坐标，则不会进入下层递归
                    return true;
        }
        visited[r][c] = false;

        return false;
    }

    private static boolean validPos(int m, int n) {
        return m >= 0 && m < l && n >= 0 && n < w;
    }

    public static void main(String[] args) {
        char[][] board1 = {
          {'A', 'B', 'C', 'E'},
          {'S', 'F', 'C', 'S'},
          {'A', 'D', 'E', 'E'}
        };
        log(exist(board1, "ABCCED"));  // expects true.
        log(exist(board1, "SEE"));     // expects true.
        log(exist(board1, "ABCB"));    // expects false.

        char[][] board2 = {
            {'A'}
        };
        log(exist(board2, "A"));       // expects true.
        log(exist(board2, "AB"));      // expects false.

        char[][] board3 = {
            {'A', 'B'},
            {'C', 'D'}
        };
        log(exist(board3, "DBAC"));    // expects true.
    }
}
