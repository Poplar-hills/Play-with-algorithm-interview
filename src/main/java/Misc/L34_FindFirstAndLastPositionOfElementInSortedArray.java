package Misc;

import static Utils.Helpers.log;

/*
 * Find First and Last Position of Element in Sorted Array
 *
 * - Given an array of integers nums sorted in ascending order, find the starting and ending position of a
 *   given target value. If target is not found in the array, return [-1, -1].
 *
 * - You must write an algorithm with O(log n) runtime complexity.
 * */

public class L34_FindFirstAndLastPositionOfElementInSortedArray {
    /*
     * 解法1：
     * -
     * */
    public static int[] searchRange(int[] nums, int target) {
        return null;
    }

    public static void main(String[] args) {
        log(searchRange(new int[]{5, 7, 7, 8, 8, 10}, 8));  // expects [3, 4]
        log(searchRange(new int[]{5, 7, 7, 8, 8, 10}, 6));  // expects [-1, -1]
        log(searchRange(new int[]{}, 0));                   // expects [-1, -1]
    }
}
