package RecursionAndBackTracking.S3_Combinations;

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
 *   Given a non-negative integer num which represents the number of LEDs that are currently on, return all
 *   possible times the watch could represent.
 *
 * - Note:
 *   1. The order of output does not matter.
 *   2. The hour must not contain a leading zero, for example "01:00" is not valid, it should be "1:00".
 *   3. The minute must consist of two digits and may contain a leading zero, for example "10:2" is not valid,
 *      it should be "10:02".
 * */

public class L401_BinaryWatch {
    /*
     * 解法1：Recursion + Backtracking
     * - 💎 思路：该题是一个组合问题，根据题意可将题目抽象成以下问题：
     *     1. 从 hours 部分中选出 n 个数字来求和，且和 ∈ [0, 12)（注意 ∵ 是12小时制 ∴ 不会有12:00，只有0:00）；
     *     2. 从 minutes 部分中选出 num - n 个数字来求和，且和 ∈ [0, 60)；
     *     3. 最后的结果集是选出的 n 个小时数和 m 个分钟数的组合。
     *   其中，1、2本质上是同一个问题，即“从 x 个元素中选出不重复的 y 个，一共有几种方式？”，该问题可以使用回溯法：
     *   例如要从表的 hours 部分中选取数字：
     *                       []
     *           8/       4/   2\    1\
     *           8        4      2     1   - 只选取1个数字时的解为 [8,4,2,1]
     *       4/ 2| 1\   2/ 1\   1|
     *      12  10  9   6   5    3         - ∵ 小时数要 < 12 ∴ 节点12不能再有分支；对于节点6，4和2已经用过了 ∴ 只能有1分支
     *          1|     1|
     *          11      7                  - 选取3个数字时的解为 [11,7]（最多也只能选取3个数 ∵ 选取4个会 > 12）
     *
     * - 👉 经验：本题是一道综合型题目，看上去复杂，但进过抽象和分解后就会容易很多 ∴ 关键是👆思路中的抽象过程。
     * - 时间复杂度 O(num * (C(4,n) + C(6,n) + n^2))，空间复杂度 O(num)。
     * */

    private final static int[] hours = {8, 4, 2, 1};
    private final static int[] minutes = {32, 16, 8, 4, 2, 1};

    public static List<String> readBinaryWatch(int num) {
        List<String> res = new ArrayList<>();
        for (int n = 0; n <= num; n++) {  // n ∈ [0,num] 例如 num=2，则可以从小时数中取0个、1个或2个
            List<Integer> hourChoices = selectToSum(hours, n, 12);
            List<Integer> minChoices = selectToSum(minutes, num - n, 60);
            combine(hourChoices, minChoices, res);
        }
        return res;
    }

    private static List<Integer> selectToSum(int[] nums, int n, int max) {  // 从 nums 中选出 n 个数来求和，且和要 < max
        List<Integer> res = new ArrayList<>();                              // 复杂度为 O(C(len(nums), n))
        backtrack(nums, n, max, 0, 0, res);
        return res;
    }

    private static void backtrack(int[] nums, int n, int max, int i, int sum, List<Integer> res) {
        if (n == 0) {
            res.add(sum);  // ∵ 最后要放到 res 里的是选出的 n 个数字的和，而非各个数字 ∴ 只需在 n=0 时 res.add 即可
            return;
        }
        for (int j = i; j < nums.length; j++)  // ∵ 要不重复地选取 ∴ j 要从 i 开始，并且进入下层递归时要 j+1
            if (sum + nums[j] < max)
                backtrack(nums, n - 1, max, j + 1, sum + nums[j], res);
    }

    private static void combine(List<Integer> hours, List<Integer> minutes, List<String> res) {
        for (int h : hours)
            for (int m : minutes)
                res.add(h + ":" + (m < 10 ? "0" + m : m));  // 使用 String.format("%d:%02d", h, m) 也行，但效率低
    }                                                       // "%02d" 表示保留2位数，不足2位数则前面补0

    /*
     * 解法2：Iteration
     * - 思路：类似 TwoSum 的思路。
     * - 实现：Integer.bitCount(i) 返回整型 i 的二进制表示中1的个数。
     * - 时间复杂度 O(12 * 60)，空间复杂度 O(1)。
     * */
    public static List<String> readBinaryWatch2(int num) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            for (int j = 0; j < 60; j++)
                if (Integer.bitCount(i) + Integer.bitCount(j) == num)
                    res.add(String.format("%d:%02d", i, j));
        return res;
    }

    public static void main(String[] args) {
        log(readBinaryWatch(1));
        // expects ["1:00", "2:00", "4:00", "8:00", "0:01", "0:02", "0:04", "0:08", "0:16", "0:32"]

        log(readBinaryWatch(0));
        // expects ["0:00"]

        log(readBinaryWatch(2));
        // expects ["0:03","0:05","0:06","0:09","0:10","0:12","0:17","0:18","0:20","0:24","0:33","0:34","0:36",
        // "0:40","0:48","1:01","1:02","1:04","1:08","1:16","1:32","2:01","2:02","2:04","2:08","2:16","2:32",
        // "3:00","4:01","4:02","4:04","4:08","4:16","4:32","5:00","6:00","8:01","8:02","8:04","8:08","8:16",
        // "8:32","9:00","10:00"]，共44个
    }
}
