package Array.TwoPointerSwapOrAssignment;

import static Utils.Helpers.log;

/*
* Remove Duplicates II
*
* - Given a sorted array, remove the duplicates in-place such that duplicates appeared at most twice and return
*   the new length (对数组去重，使得数组中的每个元素最多保留两个).
* */

public class L80_RemoveDuplicatesII {
    /*
    * 解法1：
    * - 思路：与 L26_RemoveDuplicates 一致，只是判断条件不同。
    * - 解法时间复杂度为 O(n)，空间复杂度为 O(1)。
    * */
    public static int removeDuplicates(int[] arr) {
        int j = 2;
        for (int i = 2; i < arr.length; i++) {
            if (arr[i] > arr[j - 2]) {  // 在有序数组中判断一个元素出现了3次的方法
                arr[j] = arr[i];
                j++;
            }
        }
        return j;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {1, 1, 1, 2, 2, 3};
        log(removeDuplicates(arr1));  // expects 5
        log(arr1);                    // expects [1, 1, 2, 2, 3, ...] (It doesn't matter what you leave beyond the returned length.)

        int[] arr2 = new int[] {3, 2, 2, 3};
        log(removeDuplicates(arr2));  // expects 4
        log(arr2);                    // expects [3, 2, 2, 3]
    }
}
