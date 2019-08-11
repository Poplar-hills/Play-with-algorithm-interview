package DP.OverlappingSubproblems;

import static Utils.Helpers.log;

import java.util.LinkedList;
import java.util.Queue;
import javafx.util.Pair;

/*
* Unique Paths II
*
* - 在 L62 的条件上，now consider if some obstacles are added to the grids. How many unique paths would there be?
* */

public class L63_UniquePathsII {
    /*
    * 解法1：
    * - 思路：
    * - 时间复杂度 O()，空间复杂度 O()。
    * */
    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        return 0;
    }

    public static void main(String[] args) {
        log(uniquePathsWithObstacles(new int[][]{
            new int[]{0, 0, 0},
            new int[]{0, 1, 0},
            new int[]{0, 0, 0},
        }));  // expects 2. (R->R->D->D, D->D->R->R)
    }
}