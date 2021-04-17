package Array.S5_SlidingWindow;

import static Utils.Helpers.log;

import java.util.HashMap;
import java.util.Map;

/*
 * Subarray Sum Equals K
 *
 * - Given an integer array and an integer k, find the total number of continuous subarrays whose sum equals to k.
 *
 * - 👉 此题非常经典，5个解法，从超时 -> O(n^3) -> O(n^2) -> O(n) -> 代码优化，层层递进，且涉及多种解决经典问题的技巧。
 *
 * - 分析：若该题中的数组元素都是正数，则可以通过滑动窗口轻易解决（类似 L209_MinimumSizeSubarraySum 解法3）。但由于元素既可以
 *   是正也可以是负（如 test case 4、5）∴ 当窗口中元素之和 > k 时无法判断是应该移动左界还是移动右界 ∴ 无法使用单纯的滑动窗口。
 * */

public class L560_SubarraySumEqualsK {
    /*
     * 错误解：滑动窗口
     * - 💎 原因：该题乍一看可以使用类似 L438、L209 的滑动窗口方法求解，但要注意的是，滑动窗口的是基于扩展、收缩来求解的过程，
     *   因此需要根据一个逻辑来判断何时该扩展窗口、何时该收缩。但在 test case 1-3 中，可以根据 subarray sum < k 时扩展、
     *   > k 时收缩来判断。但在 test case 4 中，由于存在负数，使该判断方式失灵 ∴ 该题无法使用滑动窗口求解。
     * */
    public static int subarraySum0(int[] nums, int k) {
        if (nums == null || nums.length == 0) return 0;
        int sum = 0, count = 0, l = 0, r = 0;

        while (r < nums.length) {
            if (sum < k)
                sum += nums[r++];
            while (sum >= k) {
                if (sum == k) count++;
                sum -= nums[l++];
            }
        }

        return count;
    }

    /*
     * 超时解：Brute Force
     * - 思路：找到 nums 中的所有 subarray，为每个 subarray 求元素之和，并与 k 比较。遍历过程如下：
     *   nums=[4, 2, -1, 5], k=4
     *         -              √
     *         ----
     *         --------
     *         -----------
     *            -
     *            -----
     *            --------
     *               --
     *               -----    √
     *                   -
     * - 实现：1. 找到一个数组的所有 subarray：双指针遍历（l ∈ [0,n), r ∈ [l,n)）；
     *        2. 求一个 subarray 的元素之和：单指针遍历（i ∈ [l, r]）。
     *        ∴ 整个过程使用三个指针、三重循环实现。
     * - 时间复杂度 O(n^3)，空间复杂度 O(1)。
     * */
    public static int subarraySum(int[] nums, int k) {
        int count = 0, n = nums.length;

        for (int l = 0; l < n; l++) {
            for (int r = l; r < n; r++) {
                int sum = 0;
                for (int i = l; i <= r; i++)
                    sum += nums[i];
                if (sum == k) count++;
            }
        }

        return count;
    }

    /*
     * 解法1：双指针 + 累加计数
     * - 思路：超时解中的累加求和过程其实可以与右移 r 的过程同步进行，从而去掉最内从的循环，将时间复杂度降低一个次方。
     * - 时间复杂度 O(n^2)，空间复杂度 O(1)。
     * */
    public static int subarraySum1(int[] nums, int k) {
        int count = 0, n = nums.length;

        for (int l = 0; l < n; l++) {
            int sum = 0;
            for (int r = l; r < n; r++) {
                sum += nums[r];
                if (sum == k) count++;
            }
        }

        return count;
    }

    /*
     * 解法2：双指针 + Prefix Sum
     * - 思路：类似 L209_MinimumSizeSubarraySum。解法1中通过双指针遍历所有 subarray 的过程若用区间和的方式来表达：
     *   nums[l..r] 之和 = nums[0..r] 之和 - nums[0..l) 之和。即通过区间和相减的方式即可得到所有的 subarray 的元素之和
     *   （即 nums[l..r] 之和）。而要快速得到任意 nums[l..r] 之和，则要使用 Prefix sums，用 sums[i] 来表示 nums[0..i] 之和。
     * - 💎 经验：Prefix Sum 本质是记录每个位置的累加和（cummulative sums），是求解“数组区间求和”（或叫“子串求和”）问题的常用技巧。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int subarraySum2(int[] nums, int k) {
        int count = 0, n = nums.length;
        int[] sums = new int[n];        // prefix sum 数组
        sums[0] = nums[0];

        for (int i = 1; i < n; i++)     // 单次遍历即可构造 prefix sum 数组（i 的取值要从1开始）
            sums[i] = sums[i - 1] + nums[i];

        for (int l = 0; l < n; l++)      // 双重循环遍历所有 subarray 检查是否有符合条件的 subarray
            for (int r = l; r < n; r++)
                if (sums[r] - sums[l] + nums[l] == k)
                    count++;             // 找到解之后不能像 L209 那样终止 ∵ 后面可能有负数正数抵消的情况，使得还存在其他解

        return count;
    }

    /*
     * 解法3：双指针 + Prefix Sum + Two Sum
     * - 思路：解法2通过双重循遍历所有的 subarray 来检查是否存在 sums[r] - sums[l] + nums[l] == k，该过程是个典型的
     *   Two Sum 问题 ∴ 可以采用 L1_TwoSum 解法4的思路将时间复杂度再降低一个次方。
     * - 实现：
     *     1. 一边累积 sum[i]（即 nums[0..i] 之和）；
     *     2. 一边检查 sum[i] 的 complement（即 sum[i] - k）是否存在于 Map 中（注意不能是 k - sum[i]）；
     *     3. 一边将 sum[i] 在 Map 中插入或更新。
     * - 注意：代码中 count += 的必须是 sum-k 的频率，而不能是 count++。举例说明：在👆最后一行中，complimenet=5 在 Map
     *   中值为2的意义是“能与当前 prefix sum 相减等于 k（即 10 - sums[0..j-1] == 5）的 subarray 一共有2个”（sum[0..2]
     *   和 sum[0..4]）∴ 要把这个个数加到 count 上。
     * - 👉 总结：该题与 L437_PathSumIII 都是 Prefix Sum 和 Two Sum 思想的经典应用。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int subarraySum3(int[] nums, int k) {
        int count = 0, sum = 0;
        Map<Integer, Integer> map = new HashMap<>();  // 存储 {prefix sum: frequency}
        map.put(0, 1);                                // 需要先插入 {0:1} 用于 sum == k 的情况（例如👆sum=10 的情况）

        for (int n : nums) {
            sum += n;                                 // 累积 prefix sum
            int complement = sum - k;                 // 得到其 complement（sum - complement == k）
            if (map.containsKey(complement))
                count += map.get(complement);         // map 中 complement 的频次即是能与 sum 相加 == k 的 subarray 的个数
            map.put(sum, map.getOrDefault(sum, 0) + 1);  // 插入或更新 sum 频率
        }

        return count;
    }

    /*
     * 解法4：解法3的代码简化版
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int subarraySum4(int[] nums, int k) {
        int count = 0, sum = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);

        for (int n : nums) {
            sum += n;
            count += map.getOrDefault(sum - k, 0);  // 技巧：map.containsKey + map.get = map.getOrDefault
            map.merge(sum, 1, Integer::sum);        // 技巧：相当于 map.put(sum, map.getOrDefault(sum) + 1)
        }

        return count;
    }

    public static void main(String[] args) {
        log(subarraySum0(new int[]{1, 1, 1}, 2));                 // expects 2. (1+1, 1+1)
        log(subarraySum0(new int[]{1, 2, 3}, 3));                 // expects 2. (1+2, 3)
        log(subarraySum0(new int[]{4, 2, 1, 5, 2, 6, 8, 7}, 8));  // expects 4. (2+1+5, 1+5+2, 2+6, 8)
        log(subarraySum0(new int[]{-1, -1, 1}, 0));               // expects 1. (-1+1)
        log(subarraySum0(new int[]{4, 2, -1, 5, -5, 5}, 5));      // expects 5. (4+2-1, 4+2-1+5-5, 5, 5-5+5, 5)
    }
}
