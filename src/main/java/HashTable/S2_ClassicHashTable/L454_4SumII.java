package HashTable.S2_ClassicHashTable;

import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
 * 4Sum II
 *
 * - 给定4个数组 A, B, C, D，查找有多少 i, j, k, l 的组合使得 A[i] + B[j] + C[k] + D[l] == 0。
 *   其中 A, B, C, D 的元素个数 n 相等，且 0 ≤ n ≤ 500。返回 i, j, k, l 的组合数。
 * */

public class L454_4SumII {
    /*
     * 解法1：Brute force
     * - 时间复杂度 O(n^4)，空间复杂度 O(1)。
     * */
    public static int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
        int count = 0;
        for (int n : A)
            for (int m : B)
                for (int p : C)
                    for (int q : D)
                        if (n + m + p + q == 0)
                            count++;
        return count;
    }

    /*
     * 解法2：查找表
     * - 思路：借鉴 L18_4Sum 的思路，将问题转化为 3Sum，即先用查找表记录一个数组（例如 D）的所有元素，之后通过3层 for 循环
     *   遍历 A, B, C 的所有组合，在查找表中搜索是否有元素 l 满足 dMap.get(l) == 0 - A[i] - B[j] - C[k]。通过这样查找
     *   使得无需遍历数组 D ∴ 时间将复杂度从解法1的 O(n^4) 降低为 O(n^3)。
     * - 实现：注意 ∵ 数组中可能存在重复元素（如 [0,0,1]），而本题是可以使用重复元素的 ∴ 查找表需使用 Map 结构记录 {元素: 频次}。
     * - 时间复杂度 O(n^3)，空间复杂度 O(n)。
     * */
    public static int fourSumCount2(int[] A, int[] B, int[] C, int[] D) {
        int count = 0;
        Map<Integer, Integer> map = new HashMap<>();

        for (int d : D)
            map.put(d, map.getOrDefault(d, 0) + 1);  // 考虑可能有重复的元素 ∴ 要记录每个元素的频次

        for (int a : A)
            for (int b : B)
                for (int c : C)
                    count += map.getOrDefault(0 - a - b - c, 0);  // 加上该元素的出现频次

        return count;
    }

    /*
     * 解法3：查找表（解法2的进一步优化）
     * - 线索：由于题目中说 n ≤ 500 ∴ 隐含的条件就是 O(n^2) 级别的算法才可以满足性能要求（若 O(n^3) 则会很慢）。
     * - 思路：解法2中使用查找表记录 D[i] 的所有可能，若再进一步，用查找表记录 C[i] + D[j] 的所有组合，则可以优化到 O(n^2)。
     * - 实现：∵ n ≤ 500 ∴ 用一个 Map 记录 500^2=250000 个键值对是完全可以接受的。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n^2)。
     * - 思考：查找表的核心问题是：我们到底要查找什么？
     * */
    public static int fourSumCount3(int[] A, int[] B, int[] C, int[] D) {
        int count = 0;
        Map<Integer, Integer> map = new HashMap<>();

        for (int c : C)        // 用查找表记录 C[i] + D[j] 的所有组合，并记录每种组合的频次
            for (int d : D)
                map.merge(c + d, 1, Integer::sum);

        for (int a : A)        // 从 A[k] + B[l] 的所有组合与 C[i] + D[j] 的所有组合中找到满足条件的组合的频次，加到 count 上
            for (int b : B)
                count += map.getOrDefault(0 - a - b, 0);

        return count;
    }

    public static void main(String[] args) {
        int[] A = new int[]{1, 2};
        int[] B = new int[]{-2, -1};
        int[] C = new int[]{-1, 2};
        int[] D = new int[]{0, 2};
        log(fourSumCount3(A, B, C, D));      // expects 2 ([1, -2, -1, 2], [2, -1, -1, 0])

        int[] A2 = new int[]{0, 1, -1};
        int[] B2 = new int[]{-1, 1, 0};
        int[] C2 = new int[]{0, 0, 1};       // 数组包含元素重复的情况
        int[] D2 = new int[]{-1, 1, 1};
        log(fourSumCount3(A2, B2, C2, D2));  // expects 17
    }
}
