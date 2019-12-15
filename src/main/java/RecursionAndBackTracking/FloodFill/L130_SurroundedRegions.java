package RecursionAndBackTracking.FloodFill;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Surrounded Regions
 *
 * - Given a 2D board containing 'X' and 'O' (the letter O), capture all regions surrounded by 'X'.
 * - A region is captured by flipping all 'O's into 'X's in that surrounded region.
 * */

public class L130_SurroundedRegions {
    /*
     * 解法1：Flood Fill + Recursion (DFS)
     * - 思路：
     *   -> 首先一眼可知该题可使用 Flood Fill 求解。只是在不同于 L200_NumberOfIslands，该题中对有效 region 的定义是四周都
     *      是 'X' 的 'O'，而与边界相邻的 'O' 则是无效的 region。
     *      -> 如此一来，程序的主体仍然可以是 Flood Fill，只需要在遍历 'O' 的邻居时需加入对边界的判断 —— 若与边界相邻则整个
     *         region 都不能 flip，只有当 Flood Fill 在没有碰到边界的情况下正常结束时才算找到了有效的 region，进而再将其中
     *         的所有 'O' 都 flip 成 'X'。
     *         -> 如此一来，就需要一个能暂存当前找到的所有 'O' 的坐标的列表，若 Flood Fill 正常结束则再来 flip 该列表中的
     *            所有坐标，而若 Flood Fill 因碰到边界而退出，则可直接丢弃列表中的坐标。
     *
     * - 时间复杂度 O()，空间复杂度 O()。
     * */

    private static boolean[][] filled;
    private static int l, w;
    private static int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    public static void solve(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) return;
        l = board.length;
        w = board[0].length;
        filled = new boolean[l][w];

        for (int m = 0; m < l; m++) {
            for (int n = 0; n < w; n++) {
                if (board[m][n] == 'O' && !filled[m][n]) {
                    List<Pair<Integer, Integer>> list = new ArrayList<>();
                    if (validRegion(board, m, n, list))
                        for (Pair<Integer, Integer> p : list)  // flip the region
                            board[p.getKey()][p.getValue()] = 'X';
                }
            }
        }
    }

    private static boolean validRegion(char[][] board, int m, int n, List<Pair<Integer, Integer>> list) {
        filled[m][n] = true;
        list.add(new Pair<>(m, n));
        boolean res = true;  // 需要让 DFS 走完后，不能中途 return

        for (int[] d : directions) {
            int newM = m + d[0], newN = n + d[1];
            if (!validPos(newM, newN)) res = false;
            else if (board[newM][newN] == 'O' && !filled[newM][newN])
                if (!validRegion(board, newM, newN, list))
                    res = false;
        }

        return res;
    }

    private static boolean validPos(int m, int n) {
        return m >= 0 && m < l && n >= 0 && n < w;
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
         *   X X X X
         *   X X X X
         *   X X X X
         *   X O X X
         * */


        char[][] board2 = {
            {'O', 'O', 'O', 'O'},
            {'X', 'O', 'X', 'O'},
            {'X', 'O', 'O', 'X'},
            {'X', 'O', 'X', 'O'}
        };
        solve(board2);
        log(board2);
        /*
         * expects: (nothing changes)
         *   O O O O
         *   X O X O
         *   X O O X
         *   X O X O
         * */


        char[][] board3 = {
            {'O', 'O', 'O', 'O'},
            {'X', 'O', 'X', 'O'},
            {'X', 'O', 'O', 'X'},
            {'X', 'X', 'X', 'O'}   // 该行第2个元素与 board2 中不同
        };
        solve(board3);
        log(board3);
        /*
         * expects: (nothing changes)
         *   O O O O
         *   X O X O
         *   X O O X
         *   X X X O
         * */
    }
}
