package Array.TwoPointerSwapOrAssignment;

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
    * - 时间复杂度为 O(n)，空间复杂度为 O(n)
    * */
    public static void moveZerosV1(int[] arr) {
        int[] aux = new int[arr.length];  // 辅助空间

        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0)
                aux[j++] = arr[i];  // 将非零元素顺序放入辅助空间中，后面未放值的位置默认为零
        }

        for (int i = 0; i < arr.length; i++)
            arr[i] = aux[i];
    }

    /*
     * 解法2：In-place modification
     * - 时间复杂度为 O(n)，空间复杂度为 O(1)
     * */
    public static void moveZerosV2(int[] arr) {
        int j = 0;  // 指向下一个非零元素应该放的位置

        for (int i = 0; i < arr.length; i++)  // 将所有非零元素复制到数组前部
            if (arr[i] != 0)
                arr[j++] = arr[i];

        for (int i = j; i < arr.length; i++)  // 后面补零
            arr[i] = 0;
    }

    /*
    * 解法3：In-place modification - swap
    * - 不需要解法2中的补零操作。
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static void moveZerosV3(int[] arr) {
        int j = 0;                              // 指向下一个非零元素应该放的位置
        for (int i = 0; i < arr.length; i++) {  // 遍历过程中只要碰到非零元素就跟 j 位置上的 0 swap
            if (arr[i] != 0) {
                if (i != j)  // 如果数组中都是非零元素（i == j），则不进行这个判断会让元素自己跟自己交换，造成浪费
                    swap(arr, i, j);
                j++;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{0, 1, 4, 0, 0, 0, 3, 8};
        moveZerosV3(arr1);
        log(arr1);  // expects [1, 4, 3, 8, 0, 0, 0, 0]

        int[] arr2 = new int[]{0, 1};
        moveZerosV3(arr2);
        log(arr2);  // expects [1, 0]

        int[] arr3 = new int[]{1, 0};
        moveZerosV3(arr3);
        log(arr3);  // expects [1, 0]

        int[] arr4 = new int[]{1, 5, 6};
        moveZerosV3(arr4);
        log(arr4);  // expects [1, 5, 6]
    }
}
