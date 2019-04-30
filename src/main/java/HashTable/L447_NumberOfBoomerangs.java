package HashTable;

import java.util.*;

import static Utils.Helpers.log;

/*
* Number of Boomerangs
*
* - 平面上有 n 个点，寻找存在多少个由这些点构成的三元组 (i, j, k) 使得 i->j 的距离等于 i->k 的距离，返回个数。
*   其中 n ≤ 500，且所有点的坐标在 [-10000, 10000] 之间。
* */

public class L447_NumberOfBoomerangs {
    public static int numberOfBoomerangs(int[][] points) {
        int count = 0;
        if (points.length < 3) return count;

        for (int i = 0; i < points.length; i++) {
            Map<Double, Integer> map = new HashMap<>();
            for (int j = 0; j < points.length; j++) {
                if (i != j) {
                    double dis = distance(points[i], points[j]);
                    map.put(dis, map.getOrDefault(dis, 0) + 1);
                }
            }
            for (int k : map.values())
                if (k >= 2)
                    count += k * (k - 1);
        }

        return count;
    }

    private static double distance(int[] i, int[] j) {
        return Math.sqrt(Math.pow(i[0] - j[0], 2) + Math.pow(i[1] - j[1], 2));
    }

    public static void main(String[] args) {
        int[] p1 = new int[] {0, 0};
        int[] p2 = new int[] {1, 0};
        int[] p3 = new int[] {2, 0};
        log(numberOfBoomerangs(new int[][] {p1, p2, p3}));  // expects 2 ([[1,0], [0,0], [2,0]], [[1,0], [2,0], [0,0]])
    }
}
