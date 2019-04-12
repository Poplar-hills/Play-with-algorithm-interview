package Array;

import java.util.Arrays;

import static Utils.Helpers.log;

public class L88_MergeSortedArray {
    private static void merge(int[] nums1, int m, int[] nums2, int n) {  // 解法1：归并排序思路
        int[] aux = new int[nums1.length];
        int i = 0, j = 0;

        for (int k = 0; k < aux.length; k++) {
            if (i >= m)
                aux[k] = nums2[j++];
            else if (j >= n || nums1[i] < nums2[j])
                aux[k] = nums1[i++];
            else
                aux[k] = nums2[j++];
        }

        for (int l = 0; l < aux.length; l++)
            nums1[l] = aux[l];
    }

//    private static void merge2(int[] nums1, int[] nums2) {  // 解法2：
//        int i = nums1.length - 1;
//        int j = nums2.length - 1;
//        int k =
//    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {1, 2, 3, 0, 0, 0};
        int[] arr2 = new int[] {2, 5, 6};

        merge(arr1, 3, arr2, 3);
        log(arr1);
    }
}
