package Misc;

import static Utils.Helpers.log;

/*
 * Find N Unique Integers Sum up to Zero
 *
 * - Given an integer n, return any array containing n unique integers such that they add up to 0.
 * */

public class L1304_FindNUniqueIntegersSumUpToZero {
    /*
     * 解法1：
     * */
    public static int[] sumZero(int n) {
        return null;
    }

    public static void main(String[] args) {
        log(sumZero(5));  // expects [-7, -1, 1, 3, 4] or [-5, -1, 1, 2, 3] or [-3, -1, 2, -2, 4]
        log(sumZero(3));  // expects [-1, 0, 1]
        log(sumZero(1));  // expects [0]

    }
}
