package Array;

import static Utils.Helpers.log;

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
        int j = 0;

        for (int i = 0; i < arr.length; i++)  // 将所有非零元素放到前面
            if (arr[i] != 0)
                arr[j++] = arr[i];

        for (int i = j; i < arr.length; i++)  // 后面补零
            arr[i] = 0;
    }

    public static void main(String[] args) {
//        int[] arr1 = new int[] {0, 1, 4, 0, 0, 0, 3, 8};
//        moveZerosV1(arr1);
//        log(arr1);
//
//        int[] arr2 = new int[] {0, 1};
//        moveZerosV1(arr2);
//        log(arr2);
//
//        int[] arr3 = new int[] {1, 0};
//        moveZerosV1(arr3);
//        log(arr3);

        int[] arr1 = new int[] {0, 1, 4, 0, 0, 0, 3, 8};
        moveZerosV2(arr1);
        log(arr1);

        int[] arr2 = new int[] {0, 1};
        moveZerosV2(arr2);
        log(arr2);

        int[] arr3 = new int[] {1, 0};
        moveZerosV2(arr3);
        log(arr3);
    }
}
