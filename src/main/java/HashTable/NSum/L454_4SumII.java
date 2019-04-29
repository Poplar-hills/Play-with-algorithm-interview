package HashTable.NSum;

import static Utils.Helpers.log;

/*
* 4Sum II
*
* 给定4个数组 A, B, C, D，查找有多少 i, j, k, l 的组合使得 A[i] + B[j] + C[k] + D[l] == 0。
* 其中 A, B, C, D 元素个数相等，且在 [0, 500] 范围内。返回 i, j, k, l 的组合数。
* */

public class L454_4SumII {
    public static int fourSumCount(int[] A, int[] B, int[] C, int[] D) {

    }

    public static void main(String[] args) {
        int[] A = new int[] {1, 2};
        int[] B = new int[] {-2, -1};
        int[] C = new int[] {-1, 2};
        int[] D = new int[] {0, 2};
        log(fourSumCount(A, B, C, D));  // expects 2
    }
}
