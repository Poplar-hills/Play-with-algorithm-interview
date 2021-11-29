package Misc;

import static Utils.Helpers.log;

/*
 * - You are given an integer array nums. You are initially positioned at the array's first index, and each
 *   element in the array represents your maximum jump length at that position. Return true if you can reach
 *   the last index, or false otherwise.
 * */

public class L55_JumpGame {
    /*
     * 解法1：
     * -
     * */
    public static boolean canJump(int[] nums) {
        return false;
    }

    public static void main(String[] args) {
        log(canJump(new int[]{2, 3, 1, 1, 4}));
        // expects true. Jump 1 step from index 0 to 1, then 3 steps to the last index.

        log(canJump(new int[]{3, 2, 1, 0, 4}));
        // expects false. You will always arrive at index 3 no matter what. Its maximum jump length is 0,
        // which makes it impossible to reach the last index.
    }
}