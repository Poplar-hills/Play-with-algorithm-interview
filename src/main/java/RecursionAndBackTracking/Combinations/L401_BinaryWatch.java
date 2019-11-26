package RecursionAndBackTracking.Combinations;

import static Utils.Helpers.*;

import java.util.List;

/*
 * Binary Watch
 *
 * - A binary watch has 4 LEDs on the top which represent the hours (0-11), and the 6 LEDs on the bottom represent
 *   the minutes (0-59). Each LED represents a zero or one, with the least significant bit on the right.
 *
 *   For example: the following binary watch reads "3:25".
 *                +------------+--------------------+
 *                | 8  4  2  1 | 32  16  8  4  2  1 |
 *                |       *  * |      *  *        * |
 *                +------------+--------------------+
 *   Given a non-negative integer n which represents the number of LEDs that are currently on, return all
 *   possible times the watch could represent.
 *
 * - Note:
 *   1. The order of output does not matter.
 *   2. The hour must not contain a leading zero, for example "01:00" is not valid, it should be "1:00".
 *   3. The minute must be consist of two digits and may contain a leading zero, for example "10:2" is not
 *      valid, it should be "10:02".
 * */

public class L401_BinaryWatch {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<String> readBinaryWatch(int num) {
        return null;
    }

    public static void main(String[] args) {
        log(readBinaryWatch(1));
        // expects ["1:00", "2:00", "4:00", "8:00", "0:01", "0:02", "0:04", "0:08", "0:16", "0:32"]
    }
}
