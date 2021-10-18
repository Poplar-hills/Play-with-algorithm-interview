package Misc;

import java.util.List;

import static Utils.Helpers.log;

/*
 * Find K Closest Elements
 *
 * - Given a sorted integer array arr, two integers k and x, return the k closest integers to x in the array.
 *   The result should also be sorted in ascending order.
 *
 * - An integer a is closer to x than an integer b if:
 *   |a - x| < |b - x|, or
 *   |a - x| == |b - x| and a < b
 * */

public class L658_FindKClosestElements {
    /*
     * 解法1：
     * */
    public static List<Integer> findClosestElements(int[] arr, int k, int x) {
        return null;
    }

    public static void main(String[] args) {
        log(findClosestElements(new int[]{1, 2, 3, 4, 5}, 4, 3));   // expects [1, 2, 3, 4]
        log(findClosestElements(new int[]{1, 2, 3, 4, 5}, 4, -1));  // expects [1, 2, 3, 4]
    }
}