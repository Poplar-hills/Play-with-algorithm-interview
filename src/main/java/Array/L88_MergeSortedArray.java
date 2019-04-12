package Array;

import static Utils.Helpers.log;

/*
* Merge Sorted Array
*
* - 因为要将两个有序数组合二为一，并且需要排序，因此整体思路是归并排序：
*   - 解法1复杂度为 O(2k)，其中 k 为 nums1 的长度。因为没有在 nums1 中原地排序，而是开辟了额外空间 aux，最后再逐个赋给 nums1。
*   - 解法1中之所以需要辅助空间是因为如果在 nums1 中原地排序，若 nums2[j] < nums1[i]，则需要 nums1[k] = nums2[j]，从冲掉了
*     nums1 中还未处理的元素，导致排序错误。但题目中说了，nums1 的空间是足够的，即 nums1 的尾部是有额外空间的，因此可以直接利用
*     这个空间就好，不需要再额外开辟。但因为是在尾部，所以需要从后往前填充（即从大到小），又因为两个数组已经各自有序，所以需要从后
*     往前遍历两个数组。
* */

public class L88_MergeSortedArray {
    private static void merge(int[] nums1, int m, int[] nums2, int n) {  // 解法1：复杂度为 O(2k)，k 为 nums1 的长度
        int[] aux = new int[nums1.length];  // 开辟辅助空间
        int i = 0, j = 0;

        for (int k = 0; k < aux.length; k++) {  // 遍历第1遍
            if (i >= m)
                aux[k] = nums2[j++];
            else if (j >= n || nums1[i] < nums2[j])
                aux[k] = nums1[i++];
            else
                aux[k] = nums2[j++];
        }

        for (int l = 0; l < aux.length; l++)  // 遍历第2遍
            nums1[l] = aux[l];
    }

    private static void merge2(int[] nums1, int m, int[] nums2, int n) {  // 优化后的解法2：复杂度 O(k)，k 为 nums1 的长度
        int i = m - 1, j = n - 1;
        for (int k = nums1.length - 1; k >= 0 && j >= 0; k--) {  // 注意循环条件：j >= 0，即若 nums2 中的元素都被处理完了，则整个归并排序已完成（说明 nums1 中剩下的元素都小于 nums2 中的最小元素，因此位置不用变）
            if (i >= 0 && nums1[i] > nums2[j])
                nums1[k] = nums1[i--];
            else
                nums1[k] = nums2[j--];
        }
    }

    public static void main(String[] args) {
        // Case 1: nums1 的有效元素比 nums2 多
        int[] arr1 = new int[] {1, 2, 3, 8, 0, 0, 0};
        int[] arr2 = new int[] {2, 5, 6};
        int[] arr3 = arr1.clone();
        int[] arr4 = arr2.clone();

        merge(arr1, 4, arr2, 3);
        log(arr1);

        merge2(arr3, 4, arr4, 3);
        log(arr3);

        // Case 2: nums2 的有效元素比 nums1 多
        int[] arr5 = new int[] {1, 3, 6, 0, 0, 0, 0};
        int[] arr6 = new int[] {2, 5, 6, 7};
        int[] arr7 = arr5.clone();
        int[] arr8 = arr6.clone();

        merge(arr5, 3, arr6, 4);
        log(arr5);

        merge2(arr7, 3, arr8, 4);
        log(arr7);
    }
}
