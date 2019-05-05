package HashTable;

import static Utils.Helpers.log;

/*
* Max Points on a Line
*
* - Given n points on a 2D plane, find the maximum number of points that lie on the same straight line.
* */

public class L149_MaxPointsOnLine {
    public static int maxPoints(int[][] points) {

    }

    public static void main(String[] args) {
        int[] p1 = new int[] {1, 1};
        int[] p2 = new int[] {2, 2};
        int[] p3 = new int[] {3, 3};
        log(maxPoints(new int[][] {p1, p2, p3}));
        /*
        * expects 3:
        *   ^
        *   |
        *   |        o
        *   |     o
        *   |  o
        *   +------------->
        *   0  1  2  3  4
        * */

        int[] p4 = new int[] {1, 1};
        int[] p5 = new int[] {3, 2};
        int[] p6 = new int[] {5, 3};
        int[] p7 = new int[] {4, 1};
        int[] p8 = new int[] {2, 3};
        int[] p9 = new int[] {1, 4};
        log(maxPoints(new int[][] {p4, p5, p6, p7, p8, p9}));
        /*
        * expects 4:
        *   ^
        *   |
        *   |  o
        *   |     o        o
        *   |        o
        *   |  o        o
        *   +------------------->
        *   0  1  2  3  4  5  6
        * */
    }
}
