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
*   组合。∵ 每个区间都有用/不用2种选择 ∴ 遍历所有区间的组合是 O(2^n)；检查每个区间组合是否有重叠是 O(n) ∴ 整体是 O((2^n)*n)。
*   TODO: 排序
* */

public class L435_NonOverlappingIntervals {
    /*
     * 解法1：DP
     * - 思路：将“从一系列区间中移除最少的区间使得剩余区间不重叠”的另一个说法就是“从一系列区间中找出具有最多个不重叠区间的组合”，
     *   最后，所有区间个数 - 不重叠区间个数 = 最少需要移除的区间个数。这样原问题就转化成了一个组合问题，而所有组合问题都可以用
     *   DP 尝试求解。另外该问题与 L300_LongestIncreasingSubsequence 的思路非常类似，都是对每个元素回头去看它是否能跟在它
     *   前面的某个元素之后，并从中找到元素个数最多的组合。注意 ∵ 每次要回头看是否能接在前面的区间之后 ∴ 所有区间应该是有序的。
     *     - 定义子问题：f(i) 表示“前 i 个区间中，以第 i 个区间结尾（即一定选用第 i 个区间）的最大的不重叠区间个数”；
     *     - 状态转移方程：f(i) = max(1 + f(j))，其中 j ∈ [0,i)，且 intervals[j] < intervals[i]。
     *     - 验证：对于 [[2,3],[1,2],[3,4],[1,3]] 来说，排序后：
     *            [[1,2],[1,3],[2,3],[3,4]]
     *               1                      - f(0) = 1
     *               1     1                - [1,3] 无法接在 [1,2] 后面 ∴ f(1) = 1
     *               1     1     2          - [2,3] 可以接在 [1,2] 后面 ∴ f(2) = f(0) + 1 = 2
     *               1     1     2     3    - [3,4] 可以接在 [2,3] 后面 ∴ f(3) = f(2) + 1 = 3
     *     因此，3即是最大不重叠的区间个数，而原题所求的最少需要移除的区间个数就是 4 - 3 = 1。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int eraseOverlapIntervals(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return 0;

        Arrays.sort(intervals, (a, b) -> a[0] != b[0]  // 先对根据起始点对区间进行排序（若起始点一样则根据结束点排）
            ? a[0] - b[0]
            : a[1] - b[1]);

        int[] dp = new int[intervals.length];
        Arrays.fill(dp, 1);                         // ∵ 只有1个区间时肯定没有重叠 ∴ 初值设为1

        for (int i = 1; i < intervals.length; i++)  // 从2个区间开始考虑
            for (int j = 0; j < i; j++)             // j ∈ [0,i)，即对于每个区间都检查一遍是否能接在其前面的某个区间后面
                if (intervals[i][0] >= intervals[j][1])  // 对于已有序的区间来说，保证区间不重叠只需判断前一个的结束点和后一个的起始点的位置即可
                    dp[i] = Math.max(dp[i], 1 + dp[j]);

        return intervals.length - dp[dp.length - 1];
    }

    /*
     * 解法2：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static int eraseOverlapIntervals2(int[][] intervals) {

        return 0;
    }

    /*
     * 解法3：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static int eraseOverlapIntervals3(int[][] intervals) {

        return 0;
    }

    /*
     * 解法4：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static int eraseOverlapIntervals4(int[][] intervals) {

        return 0;
    }

    public static void main(String[] args) {
        log(eraseOverlapIntervals(new int[][]{
            new int[]{2, 3},
            new int[]{1, 2},
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

