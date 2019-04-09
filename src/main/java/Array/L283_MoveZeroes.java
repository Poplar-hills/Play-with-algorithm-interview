package Array;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

public class L283_MoveZeroes {
    public static void moveZerosV1(int[] arr) {  // 解法1: 时间复杂度为 O(n)，空间复杂度为 O(n)
        int[] aux = new int[arr.length];  // 辅助空间

        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0)
                aux[j++] = arr[i];  // 将非零元素顺序放入辅助空间中，后面未放值的位置默认为零
        }

        for (int i = 0; i < arr.length; i++)
            arr[i] = aux[i];
    }

    public static void moveZerosV2(int[] arr) {  // 解法2：时间复杂度还是 O(n)，空间复杂度 O(1)
        int j = 0;  // 指向下一个非零元素应该放的位置

        for (int i = 0; i < arr.length; i++)  // 将所有非零元素复制到数组前部
            if (arr[i] != 0)
                arr[j++] = arr[i];

        for (int i = j; i < arr.length; i++)  // 后面补零
            arr[i] = 0;
    }

    public static void moveZerosV3(int[] arr) {  // 解法3：时间复杂度还是 O(n)，空间复杂度 O(1)，且不需要解法2中的补零操作
        int j = 0;  // 同样是指向下一个非零元素应该放的位置（因为非零元素都被 swap 到了前面，因此在下次 swap 之前总是指向当前数组中第一个零元素）

        for (int i = 0; i < arr.length; i++)  // 遍历过程中只要碰到非零元素就跟 j 位置上的 0 交换
            if (arr[i] != 0) {
                if (i != j)  // 如果数组中都是非零元素（i == j），则不进行这个判断会让元素自己跟自己交换，造成浪费
                    swap(arr, i, j);
                j++;
            }
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {0, 1, 4, 0, 0, 0, 3, 8};
        moveZerosV3(arr1);
        log(arr1);

        int[] arr2 = new int[] {0, 1};
        moveZerosV3(arr2);
        log(arr2);

        int[] arr3 = new int[] {1, 0};
        moveZerosV3(arr3);
        log(arr3);

        int[] arr4 = new int[] {1, 5, 6};
        moveZerosV3(arr4);
        log(arr4);
    }
}
