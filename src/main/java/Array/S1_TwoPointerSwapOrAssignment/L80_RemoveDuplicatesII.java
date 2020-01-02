package Array.S1_TwoPointerSwapOrAssignment;

import static Utils.Helpers.log;

/*
 * Remove Duplicates II
 *
 * - Given a sorted array, remove the duplicates in-place such that duplicates appeared at most twice and return
 *   the new length (对数组去重，每个元素最多出现两次).
 * */

public class L80_RemoveDuplicatesII {
    /*
     * 解法1：Copy and paste
     * - 思路：与 L26_RemoveDuplicates 一致，只是判断条件不同。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int removeDuplicates(int[] arr) {
        int nextValidIdx = 2;
        for (int i = 2; i < arr.length; i++) {     // 从第3个元素开始遍历
            if (arr[i] > arr[nextValidIdx - 2]) {  // 在有序数组中判断一个元素出现了3次的方法
                arr[nextValidIdx] = arr[i];
                nextValidIdx++;
            }
        }
        return nextValidIdx;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {1, 1, 1, 2, 2, 3};
        log(removeDuplicates(arr1));  // expects 5
        log(arr1);                    // expects [1, 1, 2, 2, 3, 3] (It doesn't matter what you leave beyond the returned length.)

        int[] arr2 = new int[] {2, 2, 3, 3};
        log(removeDuplicates(arr2));  // expects 4
        log(arr2);                    // expects [2, 2, 3, 3]
    }
}
