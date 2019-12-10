
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
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static boolean exist(char[][] board, String word) {
        return true;
    }

    public static void main(String[] args) {
        log(new char[][] {            // expects 1
          {'1', '1', '1', '1', '0'},
          {'1', '1', '0', '1', '0'},
          {'1', '1', '0', '0', '0'},
          {'0', '0', '0', '0', '0'},
        });

        log(new char[][] {            // expects 3
          {'1', '1', '0', '0', '0'},
          {'1', '1', '0', '0', '0'},
          {'0', '0', '1', '0', '0'},
          {'0', '0', '0', '1', '1'},
        });
    }
}
