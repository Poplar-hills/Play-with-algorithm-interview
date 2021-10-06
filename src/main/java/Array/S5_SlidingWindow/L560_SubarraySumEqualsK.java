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
     *   因此需要根据一个逻辑来判断何时该扩展窗口、何时该收缩。在 test case 1-3 中，可以根据 subarray sum < k 时扩展、> k
     *   时收缩来判断。但在 test case 4-5 中，由于存在负数，使该判断方式失灵 ∴ 该题无法使用滑动窗口求解。
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
     * 解法1：双指针 + 累加
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
     *   （即 nums[l..r] 之和）。而要快速得到任意 nums[l..r] 之和，则要使用 Prefix Sum，用 preSums[i] 来表示 nums[0..i] 之和。
     * - 💎 经验：Prefix Sum 本质是记录每个位置的累加和（cumulative sums），是求解“数组区间求和”（或叫“子串求和”）问题的常用技巧。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int subarraySum2(int[] nums, int k) {
        int count = 0, n = nums.length;
        int[] preSums = new int[n];     // prefix sum 数组
        preSums[0] = nums[0];

        for (int i = 1; i < n; i++)     // 单次遍历即可构造 prefix sum 数组（i 的取值要从1开始）
            preSums[i] = preSums[i - 1] + nums[i];

        for (int l = 0; l < n; l++)     // 双重循环遍历所有 subarray 检查是否有符合条件的 subarray
            for (int r = l; r < n; r++)
                if (preSums[r] - preSums[l] + nums[l] == k)  // nums[l,r] 的区间和 = preSums[0,r] - preSums[0,l)
                    count++;            // 找到解之后不能像 L209 那样终止 ∵ 后面可能有负数正数抵消的情况，使得还存在其他解

        return count;
    }

    /*
     * 解法3：累加 + Two Sum 查找表
     * - 💎 思路：解法2通过双重循遍历所有的 subarray 来查找是否存在 preSums[0,r] - preSums[0,l) == k，该过程其实是一个
     *   典型的 Two Sum 查找问题 ∴ 可以采用 L1_TwoSum 解法4的查找表思路将时间复杂度再降低一个次方。
     * - 实现：在单次遍历中：
     *    1. 一边累加 preSums[0,r]（即 sum）；
     *    2. 一边检查 preSums[0,r] 的 complement（即 preSums[0,l) = preSums[0,r] - k = sum - k）是否存在于查找表中
     *       （注意不能是 k - sum），若存在则将 complement 在查找表中的值累加到结果 count 上；
     *    3. 不管是否存在都将当前 sum 当做之后元素可能的 complement 更新到查找表中。
     *   对于：[4,  2, -1,  5, -5,  5],  k = 5
     *         -                        sum=4, complement=-1, miss, count=0, {0:1, 4:1}
     *         -----                    sum=6, complement=1,  miss, count=0, {0:1, 4:1, 6:1}
     *         ---------                sum=5, complement=0,  hit, count=1,  {0:1, 4:1, 6:1, 5:1},
     *         -------------            sum=10, complement=5, hit, count=2,  {0:1, 4:1, 6:1, 5:1, 10:1}
     *         -----------------        sum=5, complement=0,  hit, count=3,  {0:1, 4:1, 6:1, 5:2, 10:1}
     *         ---------------------    sum=10, complement=5, hit, count=5,  {0:1, 4:1, 6:1, 5:2, 10:2}
     *
     * - 💎 语义：查找表中键值对的语义是 Map<complement, frequency>，即能让当前 sum - complement = k 成立的 complement
     *   个数（即让 preSums[0,r] - preSums[0,l) = k 成立的 preSums[0,l) 个数），例如👆最后一行中，sum=10，complement=5，
     *   此时 complement 在 Map 中的值为2的含义就是"让 preSums[0,5] - preSums[0,l) = 5 成立的 preSums[0,l) 个数为2"
     *   ∴ 要把这个个数加到结果 count 上（而不让 count++）。
     * - 👉 总结：该题与 L437_PathSumIII 都是 Two Sum 思想的经典应用。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int subarraySum3(int[] nums, int k) {
        int count = 0, sum = 0;
        Map<Integer, Integer> map = new HashMap<>();  // Map<complement, frequency>
        map.put(0, 1);                                // 用于 sum == k 的情况（例如👆sum=10 的情况）

        for (int n : nums) {                          // 使用 two sum 查找表的话，只需遍历一次（one-pass）
            sum += n;
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
            count += map.getOrDefault(sum - k, 0);  // 技巧：.containsKey() + .get() = .getOrDefault()
            map.merge(sum, 1, Integer::sum);  // 技巧：相当于 .put(sum, map.getOrDefault(sum,0) + 1)
        }

        return count;
    }

    public static void main(String[] args) {
        log(subarraySum4(new int[]{1, 1, 1}, 2));                 // expects 2. (1+1, 1+1)
        log(subarraySum4(new int[]{1, 2, 3}, 3));                 // expects 2. (1+2, 3)
        log(subarraySum4(new int[]{4, 2, 1, 5, 2, 6, 8, 7}, 8));  // expects 4. (2+1+5, 1+5+2, 2+6, 8)
        log(subarraySum4(new int[]{-1, -1, 1}, 0));               // expects 1. (-1+1)
        log(subarraySum4(new int[]{4, 2, -1, 5, -5, 5}, 5));      // expects 5. (4+2-1, 4+2-1+5-5, 5, 5-5+5, 5)
        log(subarraySum4(new int[]{4, 2, -1}, 0));                // expects 0.
    }
}
