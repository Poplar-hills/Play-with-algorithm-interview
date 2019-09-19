package Greedy.GreedyVsDp;

import static Utils.Helpers.log;

/*
* Non-overlapping Intervals
*
* - Given a collection of intervals, find the minimum number of intervals you need to remove to make the rest
*   of the intervals non-overlapping.
* - Note: [1,2] and [2,3] don't overlap each other.
* */

public class L435_NonOverlappingIntervals {

    public static int eraseOverlapIntervals(int[][] intervals) {
        return 0;
    }

    public static void main(String[] args) {
        log(eraseOverlapIntervals(new int[][]{
            new int[]{1, 2},
            new int[]{2, 3},
            new int[]{3, 4},
            new int[]{1, 3}
        }));  // expects 1. Remove [1,3]

        log(eraseOverlapIntervals(new int[][]{
            new int[]{1, 2},
            new int[]{1, 2},
            new int[]{1, 2}
        }));  // expects 2. Remove two [1,2]

        log(eraseOverlapIntervals(new int[][]{
            new int[]{1, 2},
            new int[]{2, 3}
        }));  // expects 0
    }
}

