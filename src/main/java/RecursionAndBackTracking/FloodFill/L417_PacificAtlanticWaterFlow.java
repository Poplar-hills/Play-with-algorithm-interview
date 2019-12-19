package RecursionAndBackTracking.FloodFill;

import static Utils.Helpers.*;

import java.util.List;

/*
 * Pacific Atlantic Water Flow
 *
 * - Given an m x n matrix of non-negative integers representing the height of each unit cell in a continent,
 *   the "Pacific ocean" touches the left and top edges of the matrix and the "Atlantic ocean" touches the
 *   right and bottom edges.
 * - Water can only flow in four directions (up, down, left, or right) from a cell to another one with height
 *   equal or lower.
 * - Find the list of grid coordinates where water can flow to both the Pacific and Atlantic ocean.
 *
 * - Note:
 *   1. The order of returned grid coordinates does not matter.
 *   2. Both m and n are less than 150.
 * */

public class L417_PacificAtlanticWaterFlow {
    /*
     * 解法1：Flood Fill + Recursion (DFS)
     * - 思路：首先一眼可知该题属于连通性问题，而连通性问题可用 Flood Fill 或 Union Find 求解。而不同于 L130 和 L200，
     *   该题中对”联通”的限制为：
     *     1. 联通的标志是要能到达左/上边界以及右/下边界；
     *     2. 联通的过程是只能由高水位到达同级或更低水位。
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<Integer>> pacificAtlantic(int[][] matrix) {
        return null;
    }

    public static void main(String[] args) {
        log(pacificAtlantic(new int[][] {
          {1, 2, 2, 3, 5},
          {3, 2, 3, 4, 4},
          {2, 4, 5, 3, 1},
          {6, 7, 1, 4, 5},
          {5, 1, 1, 2, 4}
        }));
        /*
         * expects [[0,4], [1,3], [1,4], [2,2], [3,0], [3,1], [4,0]] (positions with parentheses in below matrix)
         *
         *  Pacific ~   ~   ~   ~   ~
         *       ~  1   2   2   3  (5) |
         *       ~  3   2   3  (4) (4) |
         *       ~  2   4  (5)  3   1  |
         *       ~ (6) (7)  1   4   5  |
         *       ~ (5)  1   1   2   4  |
         *          -   -   -   -   - Atlantic
         * */

        log(pacificAtlantic(new int[][] {
            {2, 9, 5, 3, 7},
            {5, 7, 2, 4, 3},
            {6, 6, 6, 8, 4}
        }));
        /*
         * expects [[0,1], [0,4], [1,1], [1,3], [2,0], [2,1], [2,2], [2,3]]
         *
         *  Pacific ~   ~   ~   ~   ~
         *       ~  2  (9)  5   3  (7) |
         *       ~  5  (7)  2  (4)  3  |
         *       ~ (6) (6) (6) (8)  4  |
         *          -   -   -   -   - Atlantic
         * */
    }
}
