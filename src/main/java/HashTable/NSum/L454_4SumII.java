package HashTable.NSum;

import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
* 4Sum II
*
* 给定4个数组 A, B, C, D，查找有多少 i, j, k, l 的组合使得 A[i] + B[j] + C[k] + D[l] == 0。
* 其中 A, B, C, D 元素个数相等，且在 [0, 500] 范围内。返回 i, j, k, l 的组合数。
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
            map.put(D[i], map.getOrDefault(D[i], 0) + 1);  // 考虑有重复元素的情况，因此记录要频次

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

    public static void main(String[] args) {
        int[] A = new int[] {1, 2};
        int[] B = new int[] {-2, -1};
        int[] C = new int[] {-1, 2};
        int[] D = new int[] {0, 2};
        log(fourSumCount(A, B, C, D));  // expects 2 ([1, -2, -1, 2], [2, -1, -1, 0])

        int[] A2 = new int[] {0, 1, -1};
        int[] B2 = new int[] {-1, 1, 0};
        int[] C2 = new int[] {0, 0, 1};  // 数组包含元素重复的情况
        int[] D2 = new int[] {-1, 1, 1};
        log(fourSumCount(A2, B2, C2, D2));  // expects 17
    }
}
