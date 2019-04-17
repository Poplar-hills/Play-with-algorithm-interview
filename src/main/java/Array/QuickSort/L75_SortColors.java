package Array.QuickSort;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
* 此题本质上就是数组排序，但是不同于一般的排序，题中给出了更多信息：只有3种颜色，且3种颜色的值是固定的 0, 1, 2。
*
* 因此有3种思路：
*   1. 直接使用快排、归并排序等通用排序算法，则最低是 O(nlogn) 的复杂度；
*   2. 从信息中可知数组元素的取值范围是固定的，且范围很小，因此适用计数排序，复杂度为 O(2n)（因为遍历数组2次）。
*   3. 因为数组元素刚好只有3种取值，因此可采用三路排序的思路，将 == 0 的放到头部，== 1 的放到中间，== 2 的放到尾部。
*      而且因为只有3种取值，因此只需遍历数组一遍即可排好，因此复杂度为 O(n)。
*
* 可见很多面试题实际上是对经典算法思想的应用，因此：
*   1. 对经典算法思想要熟练掌握；
*   2. 对这些思想的应用场景和使用套路要有感觉
* */

public class L75_SortColors {
    private static void sortColors1(int[] arr) {  // 解法1：计数排序，遍历数组2遍，复杂度 O(2n)
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

    private static void sortColors2(int[] arr) {  // 解法2：采用三路排序，只遍历数组1遍，复杂度 O(n)
        int last0Index = -1;                      // 最后一个等于0的元素索引
        int first2Index = arr.length;             // 第一个等于2的元素索引

        for (int i = 0; i < first2Index; ) {      // 手动控制 i 的自增（因为 == 1 的情况不需要自增）
            if (arr[i] == 0)
                swap(arr, i++, ++last0Index);
            else if (arr[i] == 2)
                swap(arr, i, --first2Index);
            else i++;
        }
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {2, 0, 2, 1, 1, 0};
        int[] arr2 = arr1.clone();

        sortColors1(arr1);
        log(arr1);

        sortColors2(arr2);
        log(arr2);
    }
}
