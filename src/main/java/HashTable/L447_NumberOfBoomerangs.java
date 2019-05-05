package HashTable;

import java.util.*;

import static Utils.Helpers.log;

/*
* Number of Boomerangs
*
* - 平面上有 n 个点，寻找存在多少个由这些点构成的三元组 (i, j, k) 使得 i->j 的距离等于 i->k 的距离，返回这样的三元组的个数。
*   其中 n ≤ 500，且所有点的坐标在 [-10000, 10000] 之间。
* */

public class L447_NumberOfBoomerangs {
    /*
    * 解法1：
    * - 思路：因为满足条件的三元组是 j <- i -> k 的形状，即 i 是 j, k 之间的枢纽，因此可以将每一个点当做枢纽点，统计所有其他点到
    *   达该枢纽点的距离，即对于一个点需要记录：到枢纽点的距离 - 等距点个数。之后再对其中点个数大于2的项进行统计，看 n 个等距点能
    *   与1个枢纽点组成几个三元组，这是个典型的排列组合问题了。
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    public static int numberOfBoomerangs(int[][] points) {
        int count = 0;

        for (int i = 0; i < points.length; i++) {        // 将所有点当做枢纽点进行遍历
            Map<Double, Integer> map = new HashMap<>();  // 为每个枢纽点建立（距离 - 等距点个数）的记录
            for (int j = 0; j < points.length; j++) {
                if (i != j) {
                    double dis = distance(points[i], points[j]);
                    map.put(dis, map.getOrDefault(dis, 0) + 1);
                }
            }
            for (int n : map.values())      // 统计等距点能组成的三元组个数
                if (n >= 2)
                    count += n * (n - 1);   // 如3个等距点能与1个枢纽点组成几个三元组？第一次3选1，有3种可能，第二次2选1，有2种可能，即3*2=6
        }

        return count;
    }

    private static double distance(int[] i, int[] j) {
        return Math.sqrt(Math.pow(i[0] - j[0], 2) + Math.pow(i[1] - j[1], 2));
    }

    /*
    * 解法二：minor optimisation
    * 1. 等距点的个数统计不再最后进行，而是在遍历过程中完成 —— 每次等距点个数+1，能组成的三元组个数就会在原有基础上翻倍。
    * 2. 所有枢纽点复用同一个 map，用完后 clear。
    * */
    public static int numberOfBoomerangs2(int[][] points) {
        int count = 0;
        Map<Double, Integer> map = new HashMap<>();  // 复用 map

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (i != j) {
                    double dis = distance(points[i], points[j]);
                    int prevNum = map.getOrDefault(dis, 0);
                    count += prevNum * 2;            // 每次等距点个数+1，能组成的三元组个数就会在原有基础上翻倍
                    map.put(dis, prevNum + 1);
                }
            }
            map.clear();
        }

        return count;
    }

    public static void main(String[] args) {
        int[] p1 = new int[] {0, 0};
        int[] p2 = new int[] {1, 0};
        int[] p3 = new int[] {2, 0};
        log(numberOfBoomerangs(new int[][] {p1, p2, p3}));  // expects 2. ([[1,0], [0,0], [2,0]], [[1,0], [2,0], [0,0]])
        log(numberOfBoomerangs2(new int[][] {p1, p2, p3}));  // expects 2. ([[1,0], [0,0], [2,0]], [[1,0], [2,0], [0,0]])
    }
}
