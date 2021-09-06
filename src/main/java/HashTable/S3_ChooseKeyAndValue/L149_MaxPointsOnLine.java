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
     * - 思路：该问题的关键在于如何判断多个点在一条直线上。首先想到可以通过两点一线的斜率（slope）来判断。具体来说，遍历所有点
     *   的两两组合，用 Map 记录 {斜率: 点个数} 的映射。但根据公式 y = kx + b，若两条线斜率 k 相等，但偏移量 b 不等，则这
     *   4个点并不在一条直线上，该方法没有考虑这种情况。此时可以考虑另一思路 —— 为每个点单独创建一个 Map，生成该点与所有其他点
     *   连成的直线的 {斜率: 点个数} 映射。而所有这些 Map 中的最大 value 即是所求解。
     * - 实现：点重叠的情况、没有斜率的情况（直线平行于 y 轴）要特殊处理。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int maxPoints(int[][] points) {
        if (points == null || points.length == 0) return 0;
        int n = points.length, maxCount = 0;
        if (n <= 2) return n;

        for (int i = 0; i < n; i++) {                        // 遍历每个点
            Map<BigDecimal, Integer> map = new HashMap<>();  // 为每个点创建 <斜率, 点个数> 映射
            int sameXCount = 1;                              // 记录 x 坐标相同的点的个数（初始值设为1）
            int overlapCount = 0;                            // 记录重叠点的个数

            for (int j = 0; j < n; j++) {    // 在固定一个点的基础上遍历所有其他点
                if (i == j) continue;
                int[] p1 = points[i], p2 = points[j];

                if (Arrays.equals(p1, p2))   // 两点重叠则单独记录，并在后面加到 maxCount 里（∵ 重叠的点肯定在一条线上）
                    overlapCount++;
                if (p1[0] == p2[0]) {        // 若两点 x 坐标相等（包括两点重叠的情况）则直线平行于 y 轴，没有斜率，斜率公式分母为零会报错 ∴ 要单独处理
                    maxCount = Math.max(maxCount, ++sameXCount);  // 处理方式是用变量单独记录与当前点有相同 x 坐标的点的个数，并与 maxCount 比较
                    continue;
                }
                BigDecimal k = calcSlope(p1, p2);       // 上面处理完两个特殊情况后，这里是一般情况
                map.put(k, map.getOrDefault(k, 1) + 1);  // 初始值设为1（要把源点个数算上）
                maxCount = Math.max(maxCount, map.get(k) + overlapCount);  // 记得要加上重叠的点的个数
            }
        }
        return maxCount;
    }

    private static BigDecimal calcSlope(int[] p1, int[] p2) {   // double 可能会精度不足因此使用 BigDecimal（SEE test case 5）
        BigDecimal diffX = BigDecimal.valueOf(p2[0] - p1[0]);
        BigDecimal diffY = BigDecimal.valueOf(p2[1] - p1[1]);
        return diffY.divide(diffX, new MathContext(20));       // 计算斜率: (y2 - y1) / (x2 - x1)
    }

    public static void main(String[] args) {
        /*
         *    ^
         *  3 |        o
         *  2 |     o
         *  1 |  o
         *  0 +----------->
         *    0  1  2  3
         * */
        log(maxPoints(new int[][]{{1, 1}, {2, 2}, {3, 3}}));  // expects 3

        /*
         *    ^
         *  4 |  o
         *  3 |     o        o
         *  2 |        o
         *  1 |  o        o
         *  0 +------------------->
         *    0  1  2  3  4  5  6
         * */
        log(maxPoints(new int[][]{{1, 1}, {3, 2}, {5, 3}, {4, 1}, {2, 3}, {1, 4}}));  // expects 4

        /*
         *    ^
         *  3 |
         *  2 |     o×2
         *  1 |  o×2
         *  0 +---------->
         *    0  1  2  3
         * */
        log(maxPoints(new int[][]{{1, 1}, {1, 1}, {2, 2}, {2, 2}}));  // expects 4.（点重叠的情况）

        /*
         *    ^
         *  2 |
         *  1 |  o×3
         *  0 +------->
         *    0  1  2
         * */
        log(maxPoints(new int[][]{{1, 1}, {1, 1}, {1, 1}}));  // expects 3.（所有点都重叠的情况）

        log(maxPoints(new int[][]{{0, 0}, {94911151, 94911150}, {94911152, 94911151}}));
        // expects 2.（若使用 double 计算斜率则 r1->r2 的直线与 r1->r3 的直线斜率会相等，导致认为这三点在一条线上）
    }
}
