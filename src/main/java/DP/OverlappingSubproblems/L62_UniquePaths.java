package DP.OverlappingSubproblems;

import static Utils.Helpers.log;

/*
* Unique Paths
*
* - A robot is located at the top-left corner of a m x n grid. The robot can only move either down or right
*   at any point in time. The robot is trying to reach the bottom-right corner of the grid. How many possible
*   unique paths are there?
* - Note: m and n will be at most 100.
*
* */

public class L62_UniquePaths {
    public static int uniquePaths(int m, int n) {
        return 0;
    }

    public static void main(String[] args) {
        log(uniquePaths(3, 2));  // expects 3. (R->R->D, R->D->R, D->R->R)
        log(uniquePaths(7, 3));  // expects 28.
    }
}