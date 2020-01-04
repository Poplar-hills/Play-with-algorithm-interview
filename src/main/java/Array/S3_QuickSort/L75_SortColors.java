package Array.S3_QuickSort;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
 * Sort Colors
 *
 * - Given an array with n objects colored red, white or blue, sort them in-place so that objects of the same
 *   color are adjacent, with the colors in the order red, white and blue.
 * - Here, we will use the integers 0, 1, and 2 to represent the color red, white, and blue respectively.
 * - Note: You are not suppose to use the library's sort function for this problem.
 *
 * - 此题本质上就是数组排序，但是不同于一般的排序，题中给出了更多信息：只有3种颜色，且3种颜色的值是固定的0, 1, 2 ∴ 有3种思路：
 *   1. 直接使用快排、归并排序等通用排序算法，则最低是 O(nlogn) 的复杂度；
 *   2. 从信息中可知数组元素的取值范围是固定的，且范围很小，因此适用计数排序，复杂度为 O(2n)（因为遍历数组2次）。
 *   3. 因为数组元素刚好只有3种取值，因此可采用三路排序的思路，将 == 0 的放到头部，== 1 的放到中间，== 2 的放到尾部。
 *      而且因为只有3种取值，因此只需遍历数组一遍即可排好，因此复杂度为 O(n)。
 *
 * - 可见很多面试题实际上是对经典算法思想的应用，因此：
 *   1. 对经典算法思想要熟练掌握；
 *   2. 对这些思想的应用场景和使用套路要有感觉。
 * */

public class L75_SortColors {
    /*
     * 解法1：计数排序
     * - 时间复杂度 O(2n)，遍历数组2遍。
     * */
    private static void sortColors(int[] arr) {
        int[] buckets = new int[3];               // 构造 bucket 数组，其三个位置分别存储 arr 中 0，1，2 的个数（计数过程）
        for (int i = 0; i < arr.length; i++) {    // 遍历 arr
            assert(arr[i] >= 0 && arr[i] <= 2);   // 检验 arr 中的元素的有效性
            buckets[arr[i]]++;
        }
        int i = 0;
        for (int b = 0; b < buckets.length; b++)  // 遍历 bucket，反向填充 arr
            for (int n = 0; n < buckets[b]; n++)
                arr[i++] = b;
    }

    /*
     * 解法2：三路快速排序
     * - 时间复杂度 O(n)，只遍历数组1遍。
     * */
    private static void sortColors2(int[] arr) {
        int last0Index = -1;                  // 指向最后一个等于0的元素
        int first2Index = arr.length;         // 指向第一个等于2的元素
        for (int i = 0; i < first2Index; ) {  // 手动控制 i 的自增（因为 == 2 时 i 不需要自增）
            if (arr[i] == 0)
                swap(arr, i++, ++last0Index);
            else if (arr[i] == 2)
                swap(arr, i, --first2Index);
            else i++;                         // arr[i] == 1 的情况
        }
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{2, 0, 2, 1, 1, 0};
        int[] arr2 = new int[]{2, 2, 2, 1, 1, 0};

        sortColors(arr1);
        log(arr1);  // expects [0, 0, 1, 1, 2, 2]

        sortColors(arr2);
        log(arr2);  // expects [0, 1, 1, 2, 2, 2]
    }
}
