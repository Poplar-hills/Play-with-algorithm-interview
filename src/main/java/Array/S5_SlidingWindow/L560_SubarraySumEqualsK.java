package Array.S5_SlidingWindow;

import static Utils.Helpers.log;

import java.util.HashMap;
import java.util.Map;

/*
 * Subarray Sum Equals K
 *
 * - Given an array of integers and an integer k, you need to find the total number of continuous subarrays
 *   whose sum equals to k.
 *
 * - 分析：若该题中的数组元素都是正数，则可以通过滑动窗口轻易解决（类似 L209_MinimumSizeSubarraySum 解法3）。但由于元素既可以
 *   是正也可以是负（如 test case 4、5）∴ 当窗口中元素之和 > k 时无法判断是应该移动左界还是移动右界 ∴ 无法使用单纯的滑动窗口。
 * */

public class L560_SubarraySumEqualsK {
    /*
     * 超时解：Brute Force
     * - 思路：找到 nums 中的所有 subarray，为每个 subarray 求元素之和，并与 k 比较。遍历过程如下：
     *   [4, 2, -1, 5]
     *    -
     *    ----
     *    --------
     *    -----------
     *       -
     *       -----
     *       --------
     *          --
     *          -----
     *              -
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
     * 解法2：Prefix Sum
     * - 思路：类似 L209_MinimumSizeSubarraySum。解法1中通过双指针遍历所有 subarray 的过程若用区间和的方式来表达：
     *   nums[l..r] 之和 = nums[0..r] 之和 - nums[0..l) 之和。即通过区间和相减的方式即可得到所有的 subarray 的元素之和
     *   （即 nums[l..r] 之和）。而要快速得到任意 nums[l..r] 之和，则要使用 Prefix sums，用 sums[i] 来表示 nums[0..i) 之和。
     * - 💎经验：Prefix Sum 本质是记录每个位置的累加和（cummulative sums），是求解“数组区间求和”（或叫“子串求和”）问题时的常用技巧。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int subarraySum2(int[] nums, int k) {
        int count = 0, n = nums.length;
        int[] sums = new int[n + 1];     // prefix sum 数组（存储 nums[0..i) 之和，多开辟1的空间给 sums[0]=0）
        sums[0] = 0;                     // 前0个元素的和为0（之所以要多出 preSums[0] 这个元素是因为在后面遍历过程中每
                                         // 次要减去 preSums[j]，若没有 preSums[0]=0，则无法遍历到单个元素。
        for (int i = 1; i <= n; i++)     // 单次遍历即可构造 prefix sum 数组（i 的取值也要从1开始）
            sums[i] = sums[i - 1] + nums[i - 1];

        for (int l = 0; l < n; l++)      // 双重循环遍历所有 subarray
            for (int r = l; r < n; r++)
                if (sums[r + 1] - sums[l] == k)
                    count++;

        return count;
    }

    /*
     * 解法3：Prefix Sum + Memoization
     * - 思路：在解法2中，我们通过双重循环挨个尝试是否存在 sum[0..i] - sum[0..j-1] == k，该过程是个典型的 Two Sum 问题，
     *   因而可以采用 L1_TwoSum 解法4的思路求解 —— 在遍历过程中累积 sum[0..i]，然后一边检查其 complement（sum[0..i] - k，
     *   即 sum[0..j-1]）是否存在于 map 中，一边将 sum[0..i] 插入到 map 中。通过这种方式又将时间复杂度降低一个次方。
     *   对于 nums = [4, 2, -1, 5, -5, 5], k = 5：
     *               ↑                    - preSum=4, get(4-5)不存在, count=0, {0:1, 4:1}
     *                  ↑                 - preSum=6, get(6-5)不存在, count=0, {0:1, 4:1, 6:1}
     *                      ↑             - preSum=5,  get(5-5)=1    count=1, {0:1, 4:1, 6:1, 5:1}
     *                         ↑          - preSum=10, get(10-5)=1,  count=2, {0:1, 4:1, 6:1, 5:1, 10:1}
     *                             ↑      - preSum=5,  get(5-5)=1,   count=3, {0:1, 4:1, 6:1, 5:2, 10:1}
     *                                 ↑  - preSum=10, get(10-5)=2,  count=5, {0:1, 4:1, 6:1, 5:2, 10:2}
     * - 注意：代码中 count += 的必须是 sum-k 的频率，而不能是 count++。举例说明：在👆最后一行中，get(10-5)=2 的意义是
     *   “能与当前 prefix sum 相减等于 k（即 10 - sum[0..j-1] == 5）的 subarray 一共有2个”（sum[0..2] 和 sum[0..4]）
     *   ∴ 要把这个个数加到 count 上，而不能只++。
     * - 👉 总结：该题与 L437_PathSumIII 都是 Prefix Sum 和 Two Sum 思想的经典应用。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int subarraySum3(int[] nums, int k) {
        int count = 0, preSum = 0;
        Map<Integer, Integer> map = new HashMap<>();  // 存储 <prefix sum, frequency>
        map.put(0, 1);                                // 需要先插入 <0,1> 用于 preSum == k 的情况（例如👆get(5-5) 的情况）

        for (int n : nums) {
            preSum += n;                              // 累积 prefix sum
            int complement = preSum - k;
            if (map.containsKey(complement))
                count += map.get(complement);         // 给 count 加上 sum-k 的出现次数（即元素和为 k 的 subarray 个数）
            map.put(preSum, map.getOrDefault(preSum, 0) + 1);  // 将 sum 插入 map，并记录/更新其频率
        }

        return count;
    }

    /*
     * 解法4：解法3的代码简化版
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int subarraySum4(int[] nums, int k) {
        int count = 0, preSum = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);

        for (int n : nums) {
            preSum += n;
            count += map.getOrDefault(preSum - k, 0);  // 💎 技巧：map.containsKey + map.get = map.getOrDefault
            map.merge(preSum, 1, Integer::sum);        // 相当于 map.put(sum, map.getOrDefault(sum) + 1)
        }

        return count;
    }

    public static void main(String[] args) {
        log(subarraySum4(new int[]{1, 1, 1}, 2));                 // expects 2. (1+1, 1+1)
        log(subarraySum4(new int[]{1, 2, 3}, 3));                 // expects 2. (1+2, 3)
        log(subarraySum4(new int[]{4, 2, 1, 5, 2, 6, 8, 7}, 8));  // expects 4. (2+1+5, 1+5+2, 2+6, 8)
        log(subarraySum4(new int[]{-1, -1, 1}, 0));               // expects 1. (-1+1)
        log(subarraySum4(new int[]{4, 2, -1, 5, -5, 5}, 5));      // expects 5. (4+2-1, 4+2-1+5-5, 5, 5-5+5, 5)
    }
}
