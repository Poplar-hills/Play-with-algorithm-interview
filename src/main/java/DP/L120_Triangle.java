package DP;

import java.util.Arrays;
import java.util.List;

import static Utils.Helpers.log;

/*
* Triangle
*
* - Given a triangle, find the minimum path sum from top to bottom. Each step you may move to adjacent numbers on the row below.
* - Bonus point if you are able to do this using only O(n) extra space, where n is the total number of rows in the triangle.
* */

public class L120_Triangle {
    public static int minimumTotal(List<List<Integer>> triangle) {
        return 0;
    }

    public static void main(String[] args) {
        log(minimumTotal(Arrays.asList(
                Arrays.asList(2),
                Arrays.asList(3, 4),
                Arrays.asList(6, 5, 7),
                Arrays.asList(4, 1, 8, 3)
        )));  // expects 11 (2 + 3 + 5 + 1)
    }
}
