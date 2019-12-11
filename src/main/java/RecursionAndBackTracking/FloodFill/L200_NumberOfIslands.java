
package RecursionAndBackTracking.FloodFill;

import static Utils.Helpers.*;

/*
 * Number of Islands
 *
 * - Given a 2d grid map of '1's (land) and '0's (water), count the number of islands. An island is surrounded
 *   by water and is formed by connecting adjacent lands horizontally or vertically. You may assume all four
 *   edges of the grid are all surrounded by water.
 * */

public class L200_NumberOfIslands {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：该题是经典的 Flood Fill 场景，而 Flood Fill 算法其实非常简单，就是从单一的一个格子开始往各个方向填充（fill），
     *   直到各个方向都走到头为止 ∴ Flood Fill 本质上就是基于回溯的 DFS ∴ 该解整体思路与 L79 类似。例如 test case 1：尝试
     *   对 grid 上的每个格子进行 Flood Fill，其中从 [0, 0] 开使的 Flood Fill 如下：
     *              1
     *           ↓/  →\     - 只有 ↓、→ 两个方向可以填充
     *           1     1
     *          →|          - 已填充过的格子不能再次填充 ∴ 左边的1只有往 → 填充，右边的1无路可走
     *           1
     *   可见，当所有分支都走到头时相当于找到了一个完整的 island，此时一次 Flood Fill 结束，再继续在 grid 上搜索下一个
     *   还未填充过的1，并从那里开始新一轮 Flood Fill。
     * - 时间复杂度 O(l*w)，空间复杂度 O(l*w)。
     * */

    private static int l, w;
    private static boolean[][] filled;
    private static int[][] directions = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};

    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        l = grid.length;
        w = grid[0].length;
        filled = new boolean[l][w];
        int count = 0;

        for (int m = 0; m < l; m++) {
            for (int n = 0; n < w; n++) {
                if (grid[m][n] == '1' && !filled[m][n]) {  // 找到下一个还未填充过的 island
                    count++;                               // 此时就让 island 个数 +1（或放到下一句后面，即 Flood Fill 结束的地方也一样）
                    floodFill(grid, m, n);                 // 再从这里开始 Flood Fill
                }
            }
        }

        return count;
    }

    private static void floodFill(char[][] grid, int m, int n) {
        filled[m][n] = true;              // 将该格子标记为填充过
        for (int[] d : directions) {
            int newM = m + d[0], newN = n + d[1];
            if (validPos(newM, newN) && grid[newM][newN] == '1' && !filled[newM][newN])  // 当 Flood Fill 的所有分支都走到头时递归
                floodFill(grid, newM, newN);                                             // 会自然结束 ∴ 不需要显示的递归终止条件
        }
    }

    private static boolean validPos(int m, int n) {
        return m >= 0 && m < l && n >= 0 && n < w;
    }

    public static void main(String[] args) {
        log(numIslands(new char[][] {            // expects 3
            {'1', '1', '0', '0', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '1', '0', '0'},
            {'0', '0', '0', '1', '1'},
        }));

        log(numIslands(new char[][] {            // expects 1
            {'1', '1', '1', '1', '0'},
            {'1', '1', '0', '1', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '0', '0', '0'},
        }));

        log(numIslands(new char[][] {            // expects 2
            {'0', '0', '0'},
            {'0', '1', '1'},
            {'1', '0', '0'},
        }));

        log(numIslands(new char[][] {            // expects 1
            {'1'},
        }));
    }
}
