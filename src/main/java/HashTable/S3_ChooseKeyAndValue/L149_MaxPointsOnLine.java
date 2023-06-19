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

    /**
     * 错误解
     * - 👉🏻 思路：∵ 两点确定一线，直线公式为 y = kx + b ∴ 用 Map<Line<k,b>, 点个数> 来给相同的 k,b 分组，同组内记录点个数。
     *   然后用双重 for 不重复的遍历点的两两组合（即 (1,2)->(3,4) 和 (3,4)->(1,2) 为重复），并在该过程中用外部计数器 maxCount
     *   来找到最大的点个数（即为解）。
     * - 问题：该解法看似可行，但用 test case 验证时会发现：
     *        ^
     *      4 |  o
     *      3 |     o        o
     *      2 |        o
     *      1 |  o        o
     *      0 +------------------->
     *        0  1  2  3  4  5  6
     *   <k=-1,b=5> 的分组内最终会有6个点而非4个，这是 ∵ 虽然是不重复的遍历，但点的个数仍然会重复累加，如以 (1,4) 为起点遍历完该
     *   直线上其余3个点后，又会以 (2,3) 为起点遍历 (3,2)、(4,1) ∴ 导致该分组内的点个数重复累加。
     * - 改进：可见以一个点（如 (1,4)）为起点遍历所有其他点之后，就已经能得到穿过该点的所有直接上的最大点数 ∴ 防止上述问题的方式是为
     *   每个点建立一个 Map，并且只需以 k 作为 key 即可（∵ 固定一个点和斜率后 b 也就确定了）。
     */
    private static class Line {
        private final BigDecimal k, b;
        public Line(BigDecimal k, BigDecimal b) {
            this.k = k;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Line line = (Line) o;
            if (k == null && line.k == null) return true;
            if (k == null || line.k == null) return false;
            return k.equals(line.k) && b.equals(line.b);
        }

        @Override
        public int hashCode() {
            return Objects.hash(k, b);
        }
    }

    public static int maxPoints0(int[][] points) {
        if (points == null || points.length == 0) return 0;
        int n = points.length, maxCount = 0;
        if (n <= 2) return n;
        Map<Line, Integer> map = new HashMap<>();  // Map<Line<k,b>, 点个数>

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int[] p1 = points[i], p2 = points[j];
                BigDecimal k = calcSlope0(p1, p2);
                BigDecimal b = calcYIntercept0(p1, k);
                Line line = new Line(k, b);
                map.put(line, map.getOrDefault(line, 0) + 1);
                maxCount = Math.max(maxCount, map.get(line));
            }
        }

        return maxCount;
    }

    private static BigDecimal calcYIntercept0(int[] p1, BigDecimal k) {
        if (k == null) return new BigDecimal(p1[0]);
        return (new BigDecimal(p1[1])).subtract(k.multiply(new BigDecimal(p1[0])));
    }

    private static BigDecimal calcSlope0(int[] p1, int[] p2) {
        BigDecimal diffX = BigDecimal.valueOf(p2[0] - p1[0]);
        BigDecimal diffY = BigDecimal.valueOf(p2[1] - p1[1]);
        if (diffX.equals(BigDecimal.ZERO)) return null;
        return diffY.divide(diffX, new MathContext(20));  // 计算斜率: (y2 - y1) / (x2 - x1)
    }

    /*
     * 解法1：
     * - 思路：根据👆🏻对错误解的改进方式，为每个点单独创建一个 Map，生成该点与所有其他点连成的直线的 Map<斜率, 点个数>。并在该过程中
     *   记录所有 Map 的所有 Entry 中的最大 value 即是所求解。
     * - 实现：
     *   1. 错误解中用双重 for 不重复的遍历所有点的两两组合（for(i: 0 -> n) + for(j: i+1 -> n)）。但该解法中 ∵ 要为每个点建立
     *      一个 Map，计算该点到所有接他点的斜率 k ∴ 需要重复遍历所有点的两两组合，即 for(i: 0 -> n) + for(j: 0 -> n)；
     *   2. 特殊情况单独处理：
     *      a. 点重叠的情况（如 test case 3）：重叠的点之间不存在直线也不存在斜率，但 (1,1) 与 (2,2) 之间存在斜率，即4个点之间
     *         斜率不同，但却在同一条直线上。这种情况下，∵ 有2个 (1,1) ∴ 从该点出发的直线上的点数都要多加1；
     *      b. 斜率不存在的情况：若直线平行于 y 轴，或两点重叠时，斜率不存在，斜率公式分母为零会报错 ∴ 要单独处理。处理方式是用变量
     *         记录与当前点有相同 x 坐标的点的个数，并与 maxCount 比较。
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

            for (int j = 0; j < n; j++) {   // 在固定一个点的基础上遍历所有其他点
                if (i == j) continue;
                int[] p1 = points[i], p2 = points[j];

                if (Arrays.equals(p1, p2))  // 若两点重叠则单独记录，并在后面加到 maxCount 里（∵ 重叠的点肯定在一条线上）
                    overlapCount++;
                if (p1[0] == p2[0]) {       // 对于斜率不存在的情况，用变量单独记录与当前点有相同 x 坐标的点的个数，并与 maxCount 比较
                    maxCount = Math.max(maxCount, ++sameXCount);
                    continue;
                }
                BigDecimal k = calcSlope(p1, p2);  // 上面处理完两个特殊情况后，这里是一般情况
                map.put(k, map.getOrDefault(k, 1) + 1);         // 初始值设为1（要把源点个数算上）
                maxCount = Math.max(maxCount, map.get(k) + overlapCount);  // 记得要加上重叠的点的个数
            }
        }
        return maxCount;
    }

    private static BigDecimal calcSlope(int[] p1, int[] p2) {   // double 可能会精度不足因此使用 BigDecimal（SEE test case 5）
        BigDecimal diffX = BigDecimal.valueOf(p2[0] - p1[0]);
        BigDecimal diffY = BigDecimal.valueOf(p2[1] - p1[1]);
        return diffY.divide(diffX, new MathContext(20));  // 计算斜率: (y2 - y1) / (x2 - x1)
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
         *  3 |  o
         *  2 |     o
         *  1 |  o×2
         *  0 +---------->
         *    0  1  2  3
         * */
        log(maxPoints(new int[][]{{1, 1}, {1, 1}, {2, 2}, {1, 3}}));  // expects 3.（点重叠的情况）

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
