package Array.S1_TwoPointerSwapOrAssignment;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
 * Move Zeroes
 *
 * - Given an array, move all 0's to the end of the array while maintaining the relative order of others.
 * */

public class L283_MoveZeroes {
    /*
     * 解法1：Extra space
     * - 时间复杂度 O(n)，空间复杂度 O(n)
     * */
    public static void moveZeros(int[] arr) {
        int[] aux = new int[arr.length];      // 开辟辅助空间

        int nextValidIdx = 0;
        for (int i = 0; i < arr.length; i++)  // 第1次遍历，将所有非零元素复制到 aux 数组头部（尾部默认为0）
            if (arr[i] != 0)
                aux[nextValidIdx++] = arr[i];

        for (int i = 0; i < arr.length; i++)  // 第2次遍历，将 aux 中的元素赋给 arr
            arr[i] = aux[i];
    }

    /*
     * 解法2：赋值 + 补零
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static void moveZeros2(int[] arr) {
        int nextValidIdx = 0;

        for (int i = 0; i < arr.length; i++)  // 第1次遍历，将数组中所有非零元素复制到数组头部
            if (arr[i] != 0)
                arr[nextValidIdx++] = arr[i];

        for (int i = nextValidIdx; i < arr.length; i++)  // 第2次遍历，为 nextValidIdx 到数组末尾区间补零
            arr[i] = 0;
    }

    /*
     * 解法3：双指针赋值（解法2的时间优化版）
     * - 思路：在解法2上改进，只遍历 arr 一次，同时进行复制与置零操作。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static void moveZeros3(int[] arr) {
        int nextValidIdx = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                if (i != nextValidIdx) {
                    arr[nextValidIdx] = arr[i];
                    arr[i] = 0;
                }
                nextValidIdx++;
            }
        }
    }

    /*
     * 解法4：双指针 + Swap
     * - 思路：类似 L27_RemoveElement 解法2。推导过程：
     *  [0, 1, 4, 0, 0, 0, 3, 8]
     *   vi                       arr[i]==0 -> v stays, i++
     *   v  i                     arr[i]!=0 -> swap(v,i), v++, i++
     *  [1, 0, 4, 0, 0, 0, 3, 8]
     *      v  i                  arr[i]!=0 -> swap(v,i), v++, i++
     *  [1, 4, 0, 0, 0, 0, 3, 8]
     *         v  i               arr[i]==0 -> v stays, i++
     *         v     i            arr[i]==0 -> v stays, i++
     *         v        i         arr[i]==0 -> v stays, i++
     *         v           i      arr[i]!=0 -> swap(v,i), v++, i++
     *  [1, 4, 3, 0, 0, 0, 0, 8]
     *            v           i   arr[i]!=0 -> swap(v,i), v++, i++
     *  [1, 4, 3, 8, 0, 0, 0, 0]
     *               v          i
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static void moveZeros4(int[] arr) {
        if (arr == null) return;
        int nextValidIdx = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) continue;  // 若发现0元素，则只让 i++，nextValidIdx 不变
            if (i != nextValidIdx)      // 加个判断更优，不加也可以
                swap(arr, i, nextValidIdx);
            nextValidIdx++;
        }
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{0, 1, 4, 0, 0, 0, 3, 8};
        moveZeros(arr1);
        log(arr1);  // expects [1, 4, 3, 8, 0, 0, 0, 0]

        int[] arr2 = new int[]{0, 1};
        moveZeros(arr2);
        log(arr2);  // expects [1, 0]

        int[] arr3 = new int[]{1, 0};
        moveZeros(arr3);
        log(arr3);  // expects [1, 0]

        int[] arr4 = new int[]{1, 5, 6};
        moveZeros(arr4);
        log(arr4);  // expects [1, 5, 6]
    }
}
