package HashTable;

import java.util.*;
import java.util.stream.Collectors;

import static Utils.Helpers.log;

/*
* Max Points on a Line
*
* - Given n points on a 2D plane, find the maximum number of points that lie on the same straight line.
*
* - 思路：怎么判断多个点在一条直线上？每两点组成的直线斜率相同，用 map 记录结构为 { 斜率: 点个数 }
*   但存在两条线斜率 k 相等，但偏移量 b 不等的情况（y = kx+ b），因此不能用一个 map 统计所有斜率，需要对每个点单独
*   统计它到其他点的斜率
* */

public class L149_MaxPointsOnLine {
    public static int maxPoints(int[][] points) {
        if (points.length <= 0) return 0;
        if (points.length <= 2) return points.length;
        int res = 0;
        for (int i = 0; i < points.length; i++) {
            Map<Double, Integer> map = new HashMap<>();
            int sameXCount = 1;
            int samePointCount = 0;
            for (int j = 0; j < points.length; j++) {
                if (i != j) {
                    int[] p = points[i], q = points[j];
                    if (p[0] == q[0]) {
                        sameXCount++;
                        res = Math.max(res, sameXCount);
                        continue;
                    }
                    if (p[0] == q[0] && p[1] == q[1]) {
                        samePointCount++;
                        continue;
                    }
                    double s = slope(p, q);
                    map.put(s, map.getOrDefault(s, 1) + 1);
                    res = Math.max(res, map.get(s) + samePointCount);
                }
            }
        }
        return res;
    }

    private static double slope(int[] p, int[] q) {
        return (double)(q[1] - p[1]) / (double)(q[0] - p[0]);  // equation for calculating the slope: (y2 - y1) / (x2 - x1)
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

        int[] q1 = new int[] {1, 1};
        int[] q2 = new int[] {1, 1};
        int[] q3 = new int[] {2, 2};
        int[] q4 = new int[] {2, 2};
        log(maxPoints(new int[][] {q1, q2, q3, q4}));  // expects 4.（重复的点也算）
    }
}
