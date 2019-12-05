package RecursionAndBackTracking.Combinations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Binary Watch
 *
 * - A binary watch has 4 LEDs on the top which represent the hours, and the 6 LEDs on the bottom represent
 *   the minutes. Each LED represents a zero or one, with the least significant bit on the right. For example:
 *   the following binary watch reads "3:25".
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
     * - 思路：该题是一个组合问题，根据题意可知该问题可分解为以下子问题：
     *     1. 从表的 hours 部分中选出的 n 个数字的和要 ∈ [0, 12)（注意 ∵ 是12小时制 ∴ 不会有12:00，只有0:00）；
     *     2. 从表的 minutes 部分中选出的 m 个数字的和要 ∈ [0, 60)；
     *     3. n + m = num；
     *     4. 最后的结果集是选出的 n 个小时数和 m 个分钟数的组合。
     *   其中子问题1、2本质上是同一个问题，即“从 x 个元素中选出不重复的 y 个，一共有几种方式？”，该问题可以使用回溯法：
     *   例如要从表的 hours 部分中选取数字：
     *                      []
     *           8/      4/   2\    1\
     *           8       4      2     1   - 只选取1个数字时的解为 [8,4,2,1]
     *       4/ 2| 1\  2/ 1\   1|
     *      12  10  9  6   5    3         - ∵ 小时数要 < 12 ∴ 节点12不能再有分支；对于节点6，4和2已经用过了 ∴ 只能有1分支
     *          1|    1|
     *          11     7                  - 选取3个数字时的解为 [11,7]（最多也只能选取3个数 ∵ 选取4个会 > 12）
     *
     * - 时间复杂度 O(num * (C(4, n) + C(6, n) + n^2))，空间复杂度 O(num)。
     * */

    final static int[] hours = {8, 4, 2, 1};
    final static int[] minutes = {32, 16, 8, 4, 2, 1};

    public static List<String> readBinaryWatch(int num) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i <= num; i++) {    // i ∈ [0,num] 例如 num=2，则可以从小时数中取0个、1个或2个
            List<Integer> hourStrs = select(hours, i, 12);
            List<Integer> minuteStrs = select(minutes, num - i, 60);
            combine(hourStrs, minuteStrs, res);
        }
        return res;
    }

    private static List<Integer> select(int[] nums, int n, int max) {  // 从 nums 中选出 n 个数，且数值要 < max
        List<Integer> res = new ArrayList<>();                         // 复杂度为 O(C(len(nums), n))
        helper(nums, n, max, 0, 0, res);
        return res;
    }

    private static void helper(int[] nums, int n, int max, int i, int sum, List<Integer> res) {
        if (n == 0) {
            res.add(sum);  // 选出的 n 个数字的和是一个解
            return;
        }
        for (int j = i; j < nums.length; j++)
            if (sum + nums[j] < max)
                helper(nums, n - 1, max, j + 1, sum + nums[j], res);
    }

    private static void combine(List<Integer> hours, List<Integer> minutes, List<String> res) {
        for (int h : hours)
            for (int m : minutes)
                res.add(h + ":" + (m < 10 ? "0" + m : m));  // 使用 String.format("%d:%02d", h, m) 也行，但效率低
    }                                                       // "%02d" 表示保留2位数，不足2位数则前面补0

    public static void main(String[] args) {
        log(readBinaryWatch(1));
        // expects ["1:00", "2:00", "4:00", "8:00", "0:01", "0:02", "0:04", "0:08", "0:16", "0:32"]

        log(readBinaryWatch(2));
        // expects ["0:03","0:05","0:06","0:09","0:10","0:12","0:17","0:18","0:20","0:24","0:33","0:34","0:36",
        // "0:40","0:48","1:01","1:02","1:04","1:08","1:16","1:32","2:01","2:02","2:04","2:08","2:16","2:32",
        // "3:00","4:01","4:02","4:04","4:08","4:16","4:32","5:00","6:00","8:01","8:02","8:04","8:08","8:16",
        // "8:32","9:00","10:00"]，共44个
    }
}
