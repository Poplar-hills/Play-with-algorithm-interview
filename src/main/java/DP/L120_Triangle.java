package DP;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static Utils.Helpers.log;

/*
* Triangle
*
* - Given a triangle, find the minimum path sum from top to bottom.
* - Note: each step you may move to adjacent numbers on the row below (比如下面的 test case 2 中，第2行的2只能移动
*   到第3行中的1或-1上，而不能移动到-3上，因此不是从每行中找到最小值就行).
* - Bonus point if you are able to do this using only O(n) extra space, where n is the total number of rows in the triangle.
* */

public class L120_Triangle {
    public static int minimumTotal(List<List<Integer>> triangle) {

    }

    public static void main(String[] args) {
        log(minimumTotal(Arrays.asList(
                Arrays.asList(2),
                Arrays.asList(3, 4),
                Arrays.asList(6, 5, 7),
                Arrays.asList(4, 1, 8, 3)
        )));  // expects 11 (2 + 3 + 5 + 1)

        log(minimumTotal(Arrays.asList(
                Arrays.asList(-1),
                Arrays.asList(2, 3),
                Arrays.asList(1, -1, -3)
        )));  // expects -1 (-1 + 3 + -3) 注意不是从每行中找到最小值就行，如：(-1 + 2 + -1)
    }
}
