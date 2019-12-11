package RecursionAndBackTracking.TwoDimensionalBackTracking;

import static Utils.Helpers.*;

/*
 * Word Search
 *
 * - Given a 2D board and a word, find if the word exists in the grid. The word can be constructed from
 *   letters of sequentially adjacent cell, where "adjacent" cells are those horizontally or vertically
 *   neighboring. The same letter cell may not be used more than once.
 *
 * - TODO: 对比类似的 DP 问题。
 * */

public class L79_WordSearch {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：整体思路是对 board 中的格子进行遍历，看以每个格子为起点是否能找到 word 中的所有字母。例如对于对于 board =
     *        [ A, B, C, E ]， word="EDFC" 来说，先对 board 进行从左上到右下的遍历，找到的第一个 E 在右上角，但找不到
     *        [ S, F, C, S ]   第二个字母 D ∴ 不是解，继续遍历。而从找到的第二个 E 开始能走完 word 中的所有字母 ∴ 是解。
     *        [ A, D, E, E ]
     *   在以每个格子为起点搜索 word 中的字母时（例如找到的第二个 E），过程为：
     *                 E
     *           ↑/   ←|   →\       - 往↓会越界 ∴ 没有往↓的分支
     *           C     D     E
     *              ↑/  ←\          - 往→会走到已走过的格子 ∴ 没有往→的分支
     *              F    A
     *           ↑/←|→\             - 往↓会走到已走过的格子 ∴ 没有往↓的分支
     *           B  S  C
     *   可见该过程是在树型结构中进行搜索 ∴ 可使用回溯法。
     * - 时间复杂度 O(w*l * 4^n)，空间复杂度 O(w*l)，其中 w,l 为 board 的高和宽，n 为 word 的长度。
     * */

    private static int w, l;
    private static boolean[][] visited;
    private static int[][] directions = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};  // 位移数组（💎平面搜索问题中的常用技巧，但要注意对搜索
                                                                             // 方向的顺序性是否有要求，如顺时针、只往右下搜索等限制）
    public static boolean exist(char[][] board, String word) {
        if (word == null || word.isEmpty() || board == null || board.length == 0) return false;
        l = board.length;
        w = board[0].length;
        visited = new boolean[l][w];

        for (int m = 0; m < l; m++)      // 第一阶段：遍历 board 上的每个格子
            for (int n = 0; n < w; n++)
                if (searchForWord(board, word, 0, m, n))  // 第二阶段：以每个格子为起点对 word 中的字母进行回溯搜索
                    return true;
        return false;
    }

    private static boolean searchForWord(char[][] board, String word, int i, int m, int n) {
        if (i == word.length() - 1)                // 注意这里不能是 if (i == word.length()) return true;
            return board[m][n] == word.charAt(i);  // 否则 test case 2 会挂 ∵ board 只有一个格子时不会进入下一层递归

        if (board[m][n] != word.charAt(i)) {
            visited[m][n] = true;
            for (int[] d : directions) {           // 尝试在四个方向邻格中查找 word 中的下一个字母
                int newM = m + d[0], newN = n + d[1];
                if (validPos(newM, newN) && !visited[newM][newN])  // 查找的前提是格子不越界、格子之前没有被访问过
                    if (searchForWord(board, word, i + 1, newM, newN))
                        return true;
            }
            visited[m][n] = false;
        }

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
    }
}
