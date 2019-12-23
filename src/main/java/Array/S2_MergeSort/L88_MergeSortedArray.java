package Array.S2_MergeSort;

import static Utils.Helpers.log;

/*
 * Merge Sorted Array
 *
 * - Given two sorted integer arrays nums1 and nums2, merge nums2 into nums1 as one sorted array.
 * - The number of elements initialized in nums1 and nums2 are m and n respectively.
 * - You may assume that nums1 has enough space (size that is greater or equal to m + n) to hold elements from nums2.
 * */

public class L88_MergeSortedArray {
    /*
     * 解法1：merge sort
     * - 思路：要将两个有序数组合二为一，并且需要排序，因此整体思路是归并排序。
     * - 时间复杂度为 O(m+n)。
     * */
    private static void merge(int[] nums1, int m, int[] nums2, int n) {
        int[] aux = new int[m + n];  // 开辟辅助空间
        int i = 0, j = 0;

        for (int k = 0; k < aux.length; k++) {  // 遍历第1遍
            if (i >= m)
                aux[k] = nums2[j++];
            else if (j >= n || nums1[i] < nums2[j])
                aux[k] = nums1[i++];
            else
                aux[k] = nums2[j++];
        }

        for (int l = 0; l < aux.length; l++)  // 遍历第2遍，将 aux 中的值逐个赋给 nums1
            nums1[l] = aux[l];
    }

    /*
     * 解法2：
     * - 思路：解法1中之所以需要辅助空间是因为如果在 nums1 中原地排序，若 nums2[j] < nums1[i]，则需要 nums1[k] = nums2[j]，
     *   从冲掉了 nums1 中还未处理的元素，导致排序错误。但题目中说了 nums1 的空间是足够的，即 nums1 的尾部是有额外空间的，因此
     *   可以直接利用这个空间，不需要再额外开辟。但因为是在尾部，所以需要从后往前填充（即从大到小），又因为两个数组已经各自有序，
     *   所以需要从后往前遍历两个数组。
     * - 时间复杂度 O(m)。
     * */
    private static void merge2(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1, j = n - 1;
        for (int k = nums1.length - 1; k >= 0 && j >= 0; k--) {  // 即若 nums2 中的元素都被处理完了，则整个归并排序已完成（即 nums1 中剩下的元素都小于 nums2 中的最小元素，因此位置不用变）
            if (i >= 0 && nums1[i] > nums2[j])
                nums1[k] = nums1[i--];
            else
                nums1[k] = nums2[j--];
        }
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {1, 2, 3, 8, 0, 0, 0};  // nums1 的有效元素比 nums2 多
        int[] arr2 = new int[] {2, 5, 6};

        int[] arr3 = new int[] {1, 3, 6, 0, 0, 0, 0};
        int[] arr4 = new int[] {2, 3, 6, 7};  // nums2 的有效元素比 nums1 多

        merge(arr1, 4, arr2, 3);
        log(arr1);  // expects [1, 2, 2, 3, 5, 6, 8]

        merge(arr3, 3, arr4, 4);
        log(arr3);  // expects [1, 2, 3, 3, 6, 6, 7]
    }
}
