package Greedy.S2_GreedyVsDp;

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
 *
 * - 👉 其他有 DP 和 Greedy 两种解法的题目: L376_WiggleSubsequence, L392_IsSubsequence。
 *
 * - 💎 DP vs. Greedy：
 *   - 可以用贪心算法的问题一定满足“贪心选择性”，即在求解一个最优化问题时，若采用贪心的方式选择了一个元素后，不会影响之后子问题的求解。
 *   - 该性质不易正向证明，但易举例证伪，只要举出一个反例即可说明不能采用贪心。SEE: _ZeroOneKnapsack、L474_OnesAndZeroes、
 *     L279_PerfectSquares。
 *   - 通常贪心只解决某个复杂问题中的一步，而非只是用贪心就能完全解决问题（类似排序）。例如 Play-with-algorithms 中的最短路径
 *     和最小生成树两章就都用到了贪心的思路，但又不光是贪心。另外 LeetCode 上 Greedy 标签下的问题通常也是这样。
 * */

public class L435_NonOverlappingIntervals {
    /*
     * 解法1：DP
     * - 思路：将“从一系列区间中移除最少的区间使得剩余区间不重叠”的另一个说法就是“从一系列区间中找出具有最多个不重叠区间的组合”，
     *   之后，所有区间个数 - 最多不重叠区间个数 = 最少需要移除的区间个数。这样原问题就转化成了一个组合问题，而所有组合问题都
     *   可以用 DP 尝试求解。另外该问题与 L300_LongestIncreasingSubsequence 的思路非常类似，都是对每个元素回头去看它是否
     *   能跟在它前面的某个元素之后，并从中找到元素个数最多的组合。注意，区间需要先排序后才能方便回头看是否能接在前面的区间后。
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
        Arrays.fill(dp, 1);                         // ∵ 只考虑1个区间时肯定没有重叠 ∴ 初值设为1

        for (int i = 1; i < intervals.length; i++)  // 从2个区间开始考虑
            for (int j = 0; j < i; j++)             // j ∈ [0,i)，即对于每个区间都回头检查是否能接在其前面的某个区间之后
                if (intervals[i][0] >= intervals[j][1])  // 对于已有序的区间来说，保证区间不重叠只需判断前一个的结束点和后一个的起始点的位置即可
                    dp[i] = Math.max(dp[i], 1 + dp[j]);

        return intervals.length - dp[dp.length - 1];
    }

    /*
     * 解法2：Greedy
     * - 思路：在求“从一系列区间中找出具有最多个不重叠区间的组合”的思路下，再采用 L376_WiggleSubsequence 解法5的思路，即在每
     *   次选择是否某选用某个区间时，若该区间结束点越小，则为后面留出的选择空间就越大，越有可能容纳更多的不重叠区间 ∴ 可以采用贪心算法：
     *     1. 对所有区间按结束点从小到大排序；
     *     2. 从左到右遍历排序后的区间，每次选择不与前一个区间重叠的区间。
     *   对 [[2,3],[0,2],[0,1],[1,3]] 来说：
     *     Visualised: 0__1__2__3      Sorted: [[0,1],[0,2],[1,3],[2,3]]
     *                 ----                       √
     *                 -------                    √     ×
     *                    -------                 √     ×     ×
     *                       ----                 √     ×     ×     √
     * - 时间复杂度 O(nlogn)，空间复杂度 O(1)。
     * */
    public static int eraseOverlapIntervals2(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return 0;

        Arrays.sort(intervals, (a, b) -> a[1] != b[1]  // 按照区间结束点顺序排列
            ? a[1] - b[1]
            : a[0] - b[0]);

        int prevEndPoint = intervals[0][1];
        int count = 1;

        for (int i = 1; i < intervals.length; i++) {  // ∵ 上面已经按区间结束点排序过了 ∴ 第1个肯定选用 ∴ 遍历从第2个开始
            if (prevEndPoint <= intervals[i][0]) {  // 上面的排序保证了每次选择的都是结束点最小的区间 ∴ 这里只需要保证不与前一个区间重叠即可
                prevEndPoint = intervals[i][1];
                count++;
            }
        }

        return intervals.length - count;
    }

    public static void main(String[] args) {
        log(eraseOverlapIntervals2(new int[][]{{2, 3}, {1, 2}, {3, 4}, {1, 3}}));
        // expects 1. Remove [1,3]

        log(eraseOverlapIntervals2(new int[][]{{2, 3}, {0, 2}, {0, 1}, {0, 3}}));
        // expects 2. Remove [0,2],[0,3] or [0,1],[0,3]

        log(eraseOverlapIntervals2(new int[][]{{1, 2}, {1, 2}, {1, 2}}));
        // expects 2. Remove two [1,2]

        log(eraseOverlapIntervals2(new int[][]{{1, 2}, {2, 3}}));
        // expects 0
    }
}

