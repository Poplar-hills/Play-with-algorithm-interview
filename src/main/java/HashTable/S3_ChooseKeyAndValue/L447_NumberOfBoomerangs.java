package HashTable.S3_ChooseKeyAndValue;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Number of Boomerangs
 *
 * - 平面上有 n 个点，寻找存在多少个由这些点构成的三元组 (p, q, r) 使得 p->q 的距离等于 p->r 的距离，返回这样的三元组的个数。
 *   其中 n ≤ 500，且所有点的坐标在 [-10000, 10000] 之间。
 *
 * - 题中告知坐标范围的目的是不需要考虑整数越界 —— ∵ 在计算两点距离时有平方和计算，32位的 int 很可能不够造成整型越界 ∴ 需要
 *   使用64位的 long 型。
 *
 * - 👉🏻 排列组合（Permutation and Combination）：
 *   - 题目：用1-9这9个数字，一共能组成多少个不含有相同数字的3位数？（即没有 AAA、AAB、ABB、ABA 的组合方式，仅由 ABC 组成的三位数）。
 *   - 排列用 A，组合用 C（不考虑排列顺序的就是组合，例如 123 和 321 是不同排列方式，但是同一种组合）。
 *   - ∴ 可知该题目应该使用排列，即 A(9,3) = 9*8*7 = 504（第一次9个中取1个，有9种可能性，第二次8个中取1个，有8种可能性...）。
 *   - 若是组合，则：C(9,3) = A(9,3) / 3! = (9*8*7) / (3*2*1) = 84
 * */

public class L447_NumberOfBoomerangs {
    /*
     * 解法1：查找表
     * - 💎 思路：∵ 在一个满足条件的三元组中，p 是 q, r 之间的枢纽 ∴ 可以将每一个点当做枢纽点，统计它到其他所有点的距离，即为
     *   每个点建立 <到该点的距离, 点个数> 的映射表。之后再对其中点个数大于2的项进行统计，而看 n 个等距点能与1个枢纽点能组成
     *   几个三元组，有2种方法：
     *     1. 找规律： 到枢纽的等距点个数  可组成的三元组个数   观察规律
     *                      0                0          = 0^2 - 0
     *                      1                0          = 1^2 - 1
     *                      2                2          = 2^2 - 2
     *                      3                6          = 3^2 - 3
     *                      4                12         = 4^2 - 4
     *        可见，n 个等距点能与1个枢纽点能组成的三元组个数为：n^n - n。
     *     2. 这是个典型的排列（Permutation）问题 —— 从 n 个等距点中选出2个，有多少种排列？∴ 可用排列公式：
     *        A(n,m) = n! / (n-m)!   -->   A(n,2) = n! / (n-2)! = n * (n-1)。
     *        例如从4个点中选2个：A(4,2) = 4*(4-1) = 4*3 = 12，即第一次4选1，有4种可能；第二次3选1，有3种可能.
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int numberOfBoomerangs(int[][] points) {
        int count = 0, n = points.length;

        for (int i = 0; i < n; i++) {                    // 遍历所有点（即让每个点都做一次枢纽点）
            Map<Double, Integer> map = new HashMap<>();  // 为每个枢纽点建立 Map<到枢纽的距离, 点个数>
            for (int j = 0; j < n; j++) {                // 遍历所有其他点
                if (i == j) continue;
                double dis = distance(points[i], points[j]);
                map.merge(dis, 1, Integer::sum);   // 即 map.put(dis, map.getOrDefault(dis, 0) + 1);
            }
            for (int c : map.values())  // 统计该枢纽点与所有等距点能组成的三元组个数
                count += c * (c - 1);   // 从 c 个等距点中选出2个，即 A(c,2) = c*(c-1)
        }

        return count;
    }

    private static double distance(int[] p, int[] q) {
        double xDiff = p[0] - q[0];  // Math.pow, Math.sqrt 的返回值类型为 double ∴ 入参也需要为 double，否则会丢精度
        double yDiff = p[1] - q[1];
        return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));  // 不开根号也可以（无需精确值，只要能用于区分等距点组即可）
    }

    public static void main(String[] args) {
        /*
         *    ^
         *  3 |
         *  2 |     o
         *  1 |  o     o
         *  0 +------------->
         *    0  1  2  3  4
         * */
        log(numberOfBoomerangs(new int[][]{{1, 1}, {2, 2}, {3, 1}}));
        // expects 2. ([2,2], [1,1], [3,1]), ([2,2], [3,1], [1,1])

        /*
         *    ^
         *  4 |     o
         *  3 |
         *  2 |     o
         *  1 |  o     o
         *  0 +------------->
         *    0  1  2  3  4
         * */
        log(numberOfBoomerangs(new int[][]{{1, 1}, {3, 1}, {2, 2}, {2, 4}}));
        // expects 4. ([1,1], [2,2], [3,1]), ([1,1], [3,1], [2,2])
        //            ([2,4], [1,1], [3,1]), ([2,4], [3,1], [1,1])

        /*
         *    ^
         *  3 |
         *  2 |     o
         *  1 |  o  o  o
         *  0 +------------->
         *    0  1  2  3  4
         * */
        log(numberOfBoomerangs(new int[][]{{1, 1}, {2, 1}, {2, 2}, {3, 1}}));
        // expects 8. ([2,1], [1,1], [3,1]), ([2,1], [3,1], [1,1])
        //            ([2,1], [1,1], [2,2]), ([2,1], [2,2], [1,1])
        //            ([2,1], [3,1], [2,2]), ([2,1], [2,2], [3,1])
        //            ([2,2], [1,1], [3,1]), ([2,2], [3,1], [1,1])

        log(numberOfBoomerangs(new int[][]{{0, 0}, {1, 0}}));
        // expects 0.
    }
}
