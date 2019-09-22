package Greedy.GreedyVsDp;

import static Utils.Helpers.log;

import java.util.Arrays;

/*
* Non-overlapping Intervals
*
* - Given a collection of intervals, find the minimum number of intervals you need to remove to make the rest
*   of the intervals non-overlapping.
* - Note: [1,2] and [2,3] don't overlap each other.
*
* - 初步分析：这也是一个组合问题，那么暴力解法就是遍历出所有区间的组合，然后从中过滤出没有重叠的区间的组合，并选出区间个数最多的
*   组合。∵ 每个区间都有要/不要2种选择 ∴ 遍历所有区间的组合是 O(2^n)；检查每个区间组合是否有重叠是 O(n) ∴ 整体是 O((2^n)*n)。
*   TODO: 排序
* */

public class L435_NonOverlappingIntervals {
    // Definition for an interval
    public static class Interval {
        int start;
        int end;
        Interval() { start = 0; end = 0; }
        Interval(int s, int e) { start = s; end = e; }
    }

    /*
     * 解法1：DP
     * - 思路：将“从一系列区间中移除最少的区间使得剩余区间不重叠”的另一个说法就是“从一些列区间中找出个数最多的不重叠区间的组合”。
     *   这样原问题就转换成了一个组合问题，而所有组合问题都可以用 DP 尝试求解。具体来说，每个区间都有要/不要2种选择，因此：
     *   - 定义子问题：f(i) 表示“前 i 个区间中最大的不重叠区间个数”；
     *   - 状态转移方程：f(i) = max(f(i-1), )
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static int eraseOverlapIntervals(Interval[] intervals) {
        if (intervals == null || intervals.length == 0) return 0;

        Arrays.sort(intervals, (a, b) -> (a.start != b.start)  // 先对区间根据 start 排序（若 start 一样则根据 end 排）
            ? a.start - b.start
            : a.end - b.end);

        int[] dp = new int[intervals.length];
        Arrays.fill(dp, 1);
        for (int i = 1; i < intervals.length; i++)
            for (int j = 0; j < i; j++)
                if (intervals[i].start >= intervals[j].end)
                    dp[i] = Math.max(dp[i], 1 + dp[j]);

        int maxNum = Arrays.stream(dp).reduce(0, Integer::max);

        return intervals.length - maxNum;
    }

    /*
     * 解法2：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static int eraseOverlapIntervals2(Interval[] intervals) {

        return 0;
    }

    /*
     * 解法3：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static int eraseOverlapIntervals3(Interval[] intervals) {

        return 0;
    }

    /*
     * 解法4：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static int eraseOverlapIntervals4(Interval[] intervals) {

        return 0;
    }

    public static void main(String[] args) {
        log(eraseOverlapIntervals(new Interval[]{
            new Interval(1, 2),
            new Interval(2, 3),
            new Interval(3, 4),
            new Interval(1, 3)
        }));  // expects 1. Remove [1,3]

        log(eraseOverlapIntervals(new Interval[]{
            new Interval(1, 2),
            new Interval(1, 2),
            new Interval(1, 2)
        }));  // expects 2. Remove two [1,2]

        log(eraseOverlapIntervals(new Interval[]{
            new Interval(1, 2),
            new Interval(2, 3)
        }));  // expects 0
    }
}

