package Misc;

import static Utils.Helpers.log;

/*
 * Merge Intervals
 *
 * - Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals,
 *   and return an array of the non-overlapping intervals that cover all the intervals in the input.
 * */

public class L56_MergeIntervals {
    /*
     * 解法1：
     * -
     * */
    public static int[][] merge(int[][] intervals) {
        return null;
    }

    public static void main(String[] args) {
        log(merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}}));
        // expects [[1,6],[8,10],[15,18]]. Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].

        log(merge(new int[][]{{1, 4}, {4, 5}}));
        // expects [[1, 5]]. Intervals [1,4] and [4,5] are considered overlapping.
    }
}
