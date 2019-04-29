package HashTable.NSum;

import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
* 4Sum II
*
* 给定4个数组 A, B, C, D，查找有多少 i, j, k, l 的组合使得 A[i] + B[j] + C[k] + D[l] == 0。
* 其中 A, B, C, D 元素个数 n 相等，且 0 ≤ n ≤ 500。返回 i, j, k, l 的组合数。
* */

public class L454_4SumII {
    /*
    * 解法1：查找表
    * - 时间复杂度 O(n^3)，空间复杂度 O(n)。
    * */
    public static int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
        int count = 0;
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < D.length; i++)
            map.put(D[i], map.getOrDefault(D[i], 0) + 1);  // 考虑有重复的元素，因此记录要频次

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B.length; j++) {
                for (int k = 0; k < C.length; k++) {
                    int complement = 0 - A[i] - B[j] - C[k];
                    if (map.containsKey(complement))
                        count += map.get(complement);  // 加上该元素的出现频次
                }
            }
        }

        return count;
    }
    /*
     * 解法2：查找表2
     * - 由于题目中说 n ≤ 500，因此隐含条件是设计一个 O(n^2) 的算法即可满足要求。
     * - 优化：解法1中使用查找表记录 D[i] 的所有可能，如果再进一步，用查找表记录 C[i] + D[j] 的所有可能，则可以优化到 O(n^2)。
     *        又因为 n ≤ 500，因此用一个 Map 记录 500^2 个键值对是完全可以接受的。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n^2)。
     * */
    public static int fourSumCount2(int[] A, int[] B, int[] C, int[] D) {
        int count = 0;
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < C.length; i++)
            for (int j = 0; j < D.length; j++)
                map.put(C[i] + D[j], map.getOrDefault(C[i] + D[j], 0) + 1);  // 考虑有重复的情况，因此记录要频次

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B.length; j++) {
                int complement = 0 - A[i] - B[j];
                if (map.containsKey(complement))
                    count += map.get(complement);
            }
        }

        return count;
    }

    public static void main(String[] args) {
        int[] A = new int[] {1, 2};
        int[] B = new int[] {-2, -1};
        int[] C = new int[] {-1, 2};
        int[] D = new int[] {0, 2};
        log(fourSumCount2(A, B, C, D));  // expects 2 ([1, -2, -1, 2], [2, -1, -1, 0])

        int[] A2 = new int[] {0, 1, -1};
        int[] B2 = new int[] {-1, 1, 0};
        int[] C2 = new int[] {0, 0, 1};  // 数组包含元素重复的情况
        int[] D2 = new int[] {-1, 1, 1};
        log(fourSumCount2(A2, B2, C2, D2));  // expects 17
    }
}
