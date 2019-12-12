package RecursionAndBackTracking.FloodFill;

import static Utils.Helpers.*;

import java.util.List;

/*
 * Pacific Atlantic Water Flow
 *
 * - Given an m x n matrix of non-negative integers representing the height of each unit cell in a continent,
 *   the "Pacific ocean" touches the left and top edges of the matrix and the "Atlantic ocean" touches the
 *   right and bottom edges.
 *
 * - Water can only flow in four directions (up, down, left, or right) from a cell to another one with height
 *   equal or lower.
 *
 * - Find the list of grid coordinates where water can flow to both the Pacific and Atlantic ocean.
 *
 * - Note:
 *   1. The order of returned grid coordinates does not matter.
 *   2. Both m and n are less than 150.
 * */

public class L417_PacificAtlanticWaterFlow {
    /*
     * 解法1：Recursion + Backtracking
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
         *    ~  1   2   2   3  (5) |
         *    ~  3   2   3  (4) (4) |
         *    ~  2   4  (5)  3   1  |
         *    ~ (6) (7)  1   4   5  |
         *    ~ (5)  1   1   2   4  |
         *       -   -   -   -   - Atlantic
         * */
    }
}
