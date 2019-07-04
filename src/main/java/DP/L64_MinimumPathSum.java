package DP;

import static Utils.Helpers.log;

/*
* Minimum Path Sum
*
* - Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right which minimizes
*   the sum of all numbers along its path.
* - Note: You can only move either down or right at any point in time.
* */

public class L64_MinimumPathSum {
    public static int minPathSum(int[][] grid) {
        return 0;
    }

    public static void main(String[] args) {
        log(minPathSum((new int[][]{
            new int[]{1, 3, 1},
            new int[]{1, 5, 1},
            new int[]{4, 2, 1}
        })));  // expects 7. (1 -> 3 -> 1 -> 1 -> 1)
    }
}
