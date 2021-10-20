package Misc;

import static Utils.Helpers.log;

/*
 * Search in Rotated Sorted Array
 *
 * - There is an integer array nums sorted in ascending order (with distinct values).
 *
 * - Prior to being passed to your function, nums is possibly rotated at an unknown pivot index
 *   k (1 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0],
 *   nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,5,6,7] might be rotated at pivot index 3
 *   and become [4,5,6,7,0,1,2].
 *
 * - Given the array nums after the possible rotation and an integer target, return the index of target if it
 *   is in nums, or -1 if it is not in nums.
 *
 * - You must write an algorithm with O(log n) runtime complexity.
 * */

public class L33_SearchInRotatedSortedArray {
    /*
     * 解法1：
     * -
     * */
    public static int search(int[] nums, int target) {
        return 0;
    }

    public static void main(String[] args) {
        log(search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));  // expects 4
        log(search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3));  // expects -1
        log(search(new int[]{1}, 0));                    // expects -1
    }
}
