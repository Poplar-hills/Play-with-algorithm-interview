package Array.S2_MergeSort;

import static Utils.Helpers.log;

/*
 * Merge Sorted Array
 *
 * - Given two sorted integer arrays nums1 and nums2, merge nums2 into nums1 as one sorted array.
 *
 * - Notes:
 *   1. The number of elements initialized in nums1 and nums2 are m and n respectively.
 *   2. Assume nums1 has enough space to hold elements from nums2 (nums1.length ≥ m + n).
 * */

public class L88_MergeSortedArray {
    /*
     * 解法1：Merge sort
     * - 思路：要将两个有序数组合二为一，并且结果也要有序 ∴ 整体思路是归并排序。
     * - 实现：基本的归并排序的实现有3个要点：
     *   1. 要开辟辅助空间；
     *   2. 遍历的是该辅助空间里的每个位置，而非遍历待合并数组；
     *   3. 遍历过程中先讨论越界情况，再讨论没越界情况。
     * - 时间复杂度为 O(m+n)，空间复杂度 O(m+n)。
     * */
    private static void merge(int[] nums1, int m, int[] nums2, int n) {
        int[] aux = new int[m + n];             // 开辟辅助空间
        int i = 0, j = 0;

        for (int k = 0; k < aux.length; k++) {  // 第1次遍历
            if (i >= m)                         // 先讨论越界的情况（左半部分已处理完）
                aux[k] = nums2[j++];
            else if (j >= n)                    // 越界情况（右半部分已处理完）
                aux[k] = nums1[i++];
            else if (nums1[i] <= nums2[j])      // 再讨论没越界时的情况
                aux[k] = nums1[i++];
            else
                aux[k] = nums2[j++];
        }

        for (int l = 0; l < aux.length; l++)    // 第2次遍历，将 aux 中的值逐个赋给 nums1
            nums1[l] = aux[l];
    }

    /*
     * 解法2：Merge sort（解法1的时空优化版）
     * - 思路：与解法1一致，都是归并排序。
     * - 实现：解法1中之所以需要辅助空间是因为若在 nums1 中原地排序，当 nums1[i] > nums2[j] 时，需 nums1[k] = nums2[j]，
     *   从而可能冲掉了还未处理的元素 nums1[k]，导致排序错误。但该题中说了 nums1 尾部有足够空间 ∴ 可以直接利用尾部空间而不再
     *   额外开辟新空间。而 ∵ 是尾部空间 ∴ 需要从后往前填充（即从大到小填充），又 ∵ 两个数组各自有序 ∴ 只需从后往前遍历即可：
     *
     *   对于 nums1 = [1, 2, 3, 8, 0, 0, 0], nums2 = [2, 5, 6]：
     *     -> 8 > 6 ∴ [1, 2, 3, 8, 0, 0, 8]
     *     -> 3 < 6 ∴ [1, 2, 3, 8, 0, 6, 8]
     *     -> 3 < 5 ∴ [1, 2, 3, 8, 5, 6, 8]
     *     -> 3 > 2 ∴ [1, 2, 3, 3, 5, 6, 8]
     *     -> 2 = 2 ∴ [1, 2, 2, 3, 5, 6, 8]
     *     -> 1 < 2 ∴ [1, 2, 2, 3, 5, 6, 8]  - 至此 nums2 中的元素已全部处理完毕
     *
     *   该实现中藏有一个优化 —— 当 nums2 中的元素已全部处理完时，整个排序就已经完成（∵ 此时 nums1 中剩余的元素都 < nums2 中
     *   的最小元素 ∴ nums1 中剩余元素的位置不用再变）。
     *
     * - 时间复杂度 O(m)，空间复杂度 O(1)。
     * */
    private static void merge2(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1, j = n - 1;
        for (int k = nums1.length - 1; k >= 0 && j >= 0; k--) {  // 加上条件 j>=0 就是要在 nums2 中的元素全部处理完时结束整个排序
            if (i >= 0 && nums1[i] >= nums2[j])
                nums1[k] = nums1[i--];
            else
                nums1[k] = nums2[j--];
        }
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{1, 2, 3, 8, 0, 0, 0};  // nums1 的有效元素比 nums2 多的情况
        int[] arr2 = new int[]{2, 5, 6};
        merge(arr1, 4, arr2, 3);
        log(arr1);                                    // expects [1, 2, 2, 3, 5, 6, 8]

        int[] arr3 = new int[]{1, 3, 6, 0, 0, 0, 0};
        int[] arr4 = new int[]{2, 3, 6, 7};           // nums2 的有效元素比 nums1 多的情况
        merge(arr3, 3, arr4, 4);
        log(arr3);                                    // expects [1, 2, 3, 3, 6, 6, 7]
    }
}
