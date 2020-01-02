package HashTable.S3_ChooseKeyAndValue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import static Utils.Helpers.log;

/*
* Max Points on a Line
*
* - Given n points on a 2D plane, find the maximum number of points that lie on the same straight line.
* */

public class L149_MaxPointsOnLine {
    /*
    * 解法1：
    * - 思路：该问题的关键在于如何判断多个点在一条直线上。首先想到可以通过两点一线的斜率（slope）来判断。具体来说，遍历所
    *   有点的两两组合，并用 Map 记录斜率 -> 点个数的映射。但这种方式还有问题，即存在两条直线斜率 k 相等，但偏移量 b 不等
    *   的情况（y = kx + b），因此不能用一个 Map 统计所有斜率，需要为每个点单独统计，即为每个点创建一个 Map，记录该点与
    *   所有其他点连成的直线的斜率，并找出所有这些点的 Map 中最大的 value。
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    public static int maxPoints(int[][] points) {
        if (points == null || points.length == 0) return 0;
        if (points.length <= 2) return points.length;
        int res = 0;

        for (int i = 0; i < points.length; i++) {            // 遍历每个点
            Map<BigDecimal, Integer> map = new HashMap<>();  // 为每个点创建查找表，记录 {斜率: 点个数}
            int sameXCount = 1;                              // 记录 x 坐标相同的点的个数（初始值设为1）
            int overlapCount = 0;                            // 记录重叠点的个数

            for (int j = 0; j < points.length; j++) {        // 在固定一个点的基础上遍历所有其他点
                if (i == j) continue;
                int[] p1 = points[i], p2 = points[j];

                if (Arrays.equals(p1, p2))  // 两点重叠的情况。如果这里不处理则会在下面的 if 块中直接 continue，跳过斜率计算，导致少算了一个点
                    overlapCount++;         // 因此要在这里单独记录，并在后面加回到 res 里（因为重叠的点肯定在一条线上）
                if (p1[0] == p2[0]) {       // 若两点 x 坐标相等（包括两点重叠的情况）此时两点所连成的线垂直于 x 轴，没有斜率，斜率公式分母为零会报错，因此单独处理
                    res = Math.max(res, ++sameXCount);  // 处理方式是用变量单独记录与当前点有相同 x 坐标的点的个数，并与 res 比较
                    continue;
                }
                BigDecimal s = slope(p1, p2);           // 上面处理完两个特殊情况后，这里是一般情况
                map.put(s, map.getOrDefault(s, 1) + 1);  // 初始值设为1
                res = Math.max(res, map.get(s) + overlapCount);      // 记得要加上重叠的点的个数
            }
        }
        return res;
    }

    private static BigDecimal slope(int[] p1, int[] p2) {      // double 可能会精度不足因此使用 BigDecimal（SEE test case 5）
        BigDecimal diffY = BigDecimal.valueOf(p2[1] - p1[1]);  // 使用 valueOf() 将 int 转为 BigDecimal
        BigDecimal diffX = BigDecimal.valueOf(p2[0] - p1[0]);
        return diffY.divide(diffX, new MathContext(20));  // calculating slope: (y2 - y1) / (x2 - x1)
    }

    public static void main(String[] args) {
        /*
         *   ^
         *   |        o
         *   |     o
         *   |  o
         *   +------------->
         *   0  1  2  3  4
         * */
        int[] p1 = new int[]{1, 1};
        int[] p2 = new int[]{2, 2};
        int[] p3 = new int[]{3, 3};
        log(maxPoints(new int[][]{p1, p2, p3}));  // expects 3

        /*
         *   ^
         *   |  o
         *   |     o        o
         *   |        o
         *   |  o        o
         *   +------------------->
         *   0  1  2  3  4  5  6
         * */
        int[] p4 = new int[]{1, 1};
        int[] p5 = new int[]{3, 2};
        int[] p6 = new int[]{5, 3};
        int[] p7 = new int[]{4, 1};
        int[] p8 = new int[]{2, 3};
        int[] p9 = new int[]{1, 4};
        log(maxPoints(new int[][]{p4, p5, p6, p7, p8, p9}));  // expects 4

        int[] q1 = new int[]{1, 1};
        int[] q2 = new int[]{1, 1};
        int[] q3 = new int[]{2, 2};
        int[] q4 = new int[]{2, 2};
        log(maxPoints(new int[][]{q1, q2, q3, q4}));  // expects 4.（重复点的情况）

        int[] q5 = new int[]{1, 1};
        int[] q6 = new int[]{1, 1};
        int[] q7 = new int[]{1, 1};
        log(maxPoints(new int[][]{q5, q6, q7}));  // expects 3.（所有都是重复点的情况）

        int[] r1 = new int[]{0, 0};
        int[] r2 = new int[]{94911151, 94911150};
        int[] r3 = new int[]{94911152, 94911151};
        log(maxPoints(new int[][]{r1, r2, r3}));  // expects 2.（若使用 double 计算斜率则 r1->r2 的直线与 r1->r3 的直线斜率会相等，导致认为这三点在一条线上）
    }
}