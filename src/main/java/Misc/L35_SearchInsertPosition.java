package Misc;

import static Utils.Helpers.log;

/**
 * Search Insert Position
 *
 * - Given a sorted array of distinct integers and a target value, return the index if the target
 *   is found. If not, return the index where it would be if it were inserted in order.
 * - You must write an algorithm with O(log n) runtime complexity.
 */
public class L35_SearchInsertPosition {
    /**
     * 解法1：
     */
    public static int searchInsert(int[] nums, int target) {
        return -1;
    }

    public static void main(String[] args) {
        log(searchInsert(new int[]{1, 3, 5, 6}, 5));  // expects 2
        log(searchInsert(new int[]{1, 3, 5, 6}, 2));  // expects 1
        log(searchInsert(new int[]{1, 3, 5, 6}, 7));  // expects 4
    }
}
