package Array;

import static Utils.Helpers.log;

/*
* 此题本质上就是数组排序，但是不同于一般的排序，题中给出了更多信息：只有3种颜色，且3种颜色的值是固定的 0, 1, 2。
*
* 因此有3种思路：
*   1. 使用快排、归并排序等通用排序算法，则最低是 O(nlogn) 的复杂度；
*   2. 从信息中可知数组元素的取值范围是固定的，且范围很小，因此适用计数排序，复杂度为 O(2n)（因为遍历数组2次）。
*   3.
* */

public class L75_SortColors {
    private static void sortColors1(int[] arr) {  // 解法1：计数排序，遍历数组2遍，复杂度 O(n)
        // 构造 bucket 数组（遍历 arr 第1遍）
        int[] buckets = new int[3];
        for (int i = 0; i < arr.length; i++) {
            assert(arr[i] >= 0 && arr[i] <= 2);  // 注意边界
            buckets[arr[i]]++;
        }

        // 根据 bucket 数组填充 arr（遍历 arr 第2遍）
        int i = 0;
        for (int n = 0; n < buckets.length; n++)
            for (int j = 0; j < buckets[n]; j++)
                arr[i++] = n;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {2, 0, 2, 1, 1, 0};
        sortColors1(arr1);
        log(arr1);  // expects [0, 0, 1, 1, 2, 2]
    }
}
