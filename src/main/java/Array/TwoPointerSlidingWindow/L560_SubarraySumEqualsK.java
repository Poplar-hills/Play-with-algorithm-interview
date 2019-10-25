package Array.TwoPointerSlidingWindow;

import static Utils.Helpers.log;

import java.util.HashMap;
import java.util.Map;

/*
 * Subarray Sum Equals K
 *
 * - Given an array of integers and an integer k, you need to find the total number of continuous subarrays
 *   whose sum equals to k.
 *
 * - 分析：若该题中的数组元素都是正数，则可以通过滑动窗口方式简单解决。但该题中数组元素既可以是正也可以是负（如 test case 5），
 *   当 window sum > k 时无法判断是应该右移左边界还是右移右边界，因此无法使用单纯的滑动窗口。
 * */

public class L560_SubarraySumEqualsK {
    /*
     * 超时解：Brute-force
     * - 思路：通过双指针遍历 nums 中的所有 subarray，并对每个 subarray 累加求 sum 并与 k 比较。遍历所有 subarray 过程：
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
     * - 时间复杂度 O(n^3)，空间复杂度 O(1)。
     * */
    public static int subarraySum(int[] nums, int k) {
        int count = 0, len = nums.length;

        for (int i = 0; i < len; i++) {
            for (int j = i; j < len; j++) {
                int sum = 0;
                for (int n = i; n <= j; n++)
                    sum += nums[n];
                if (sum == k) count++;
            }
        }

        return count;
    }

    /*
     * 解法1：双指针 + 累加计数
     * - 思路：超时解中的累加过程其实可以在移动右指针时同步进行，从而去掉最内从的循环，将时间复杂度降低一个次方。
     * - 时间复杂度 O(n^2)，空间复杂度 O(1)。
     * */
    public static int subarraySum1(int[] nums, int k) {
        int count = 0, len = nums.length;

        for (int i = 0; i < len; i++) {
            int sum = 0;
            for (int j = i; j < len; j++) {
                sum += nums[j];
                if (sum == k) count++;
            }
        }

        return count;
    }

    /*
     * 解法2：双指针 + Prefix Sum
     * - 思路：解法1中通过双指针滑动来遍历所有 subarray 的过程其实可以通过将不同段的 sum 相减来表达：
     *   sums[j..i] = sums[0..i] - sums[0..j]，这样一来只需每次查看 sum[j..i] 是否等于 k 即可。而要将遍历所有 subarray
     *   的过程用 sum 来表达，需要先计算出每个位置 i 上的累加和 sums[i]，然后再通过移动双指针来实现该公式。
     * - 💎经验：Prefix Sum 本质是为每个位置缓存累加和，即 save cummulative sums，是在求解区间求和相关问题中的一种常用技巧。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int subarraySum2(int[] nums, int k) {
        int count = 0, len = nums.length;
        int[] sums = new int[len + 1];  // sums[i] 上存储 nums[0..i] 之和
        sums[0] = 0;                    // 前0个元素的和为0（之所以要多出 sums[0]=0 这个元素是因为在后面遍历过程中每次要
                                        // 减去 sums[j]，若没有 sums[0]=0，则无法遍历到单个元素。
        for (int i = 1; i <= len; i++)
            sums[i] = sums[i - 1] + nums[i - 1];  // 填充 sums 数组

        for (int i = 1; i <= len; i++)
            for (int j = 0; j < i; j++)
                if (sums[i] - sums[j] == k)
                    count++;

        return count;
    }

    /*
     * 解法3：Map
     * - 思路：在解法2中，我们通过双重循环挨个尝试是否存在 sums[0..i] - sums[0..j] == k。该过程是个典型的 Two Sum 问题，
     *   可以通过 L1_TwoSum 中解法2的方式进行优化 —— 将所有 sums[0..j] 存储在 map 中，之后每次只需查询 sums[0..i] - k
     *   是否存在于 map 中即可，通过这种方式又将时间复杂度降低一个次方。
     *     nums = [4, 2, -1, 5, -5], k = 5
     *             ↑                 sum=4, get(4-5)不存在, count=0, {0:1, 4:1}
     *                ↑              sum=6, get(6-5)不存在, count=0, {0:1, 4:1, 6:1}
     *                    ↑          sum=5,  get(5-5)=1    count=1, {0:1, 4:1, 6:1, 5:1}
     *                       ↑       sum=10, get(10-5)=1,  count=2, {0:1, 4:1, 6:1, 5:1, 10:1}
     *                           ↑   sum=5,  get(5-5)=1,   count=3, {0:1, 4:1, 6:1, 5:2, 10:1}
     * - 注意：代码里给 count 加上的是 map 里的 value，而不能是 count++。因为当 sum-k 存在于 map 中时，说明 nums 中存在
     *   元素之和等于 k 的 subarray，但个数不一定只有一个（∵ nums 中有负数 ∴ 可能存在多个）。具体有几个这样的 subarray 是
     *   记录在 map 的 value 上的，即 map.get(sum-k)，因此要把它加到 count 上。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int subarraySum3(int[] nums, int k) {
        int count = 0, sum = 0;
        Map<Integer, Integer> map = new HashMap<>();  // 存储 <sums[i], sums[i] 的出现次数>
        map.put(0, 1);                                // 将0放入 map 中，值设为1

        for (int n : nums) {
            sum += n;                                 // 遍历过程中求 sums[i]
            if (map.containsKey(sum - k))
                count += map.get(sum - k);            // 给 count 加上 sum-k 的出现次数（即元素和为 k 的 subarray 个数）
            map.put(sum, map.getOrDefault(sum, 0) + 1);  // 将 sum 放入 map，若之前出现过，则个数+1
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
            count += map.getOrDefault(sum - k, 0);  // 不需要解法3中的 containsKey 检查
            map.merge(sum, 1, (a, b) -> a + b);     // 若 map 中已有 sum，则相当于 map.put(sum, map.get(sum) + 1)，
        }                                           // 若 map 中没有 sum，则相当于 map.put(sum, 1)

        return count;
    }

    public static void main(String[] args) {
        log(subarraySum4(new int[]{1, 1, 1}, 2));                 // expects 2. (1+1, 1+1)
        log(subarraySum4(new int[]{1, 2, 3}, 3));                 // expects 2. (1+2, 3)
        log(subarraySum4(new int[]{4, 2, 1, 5, 2, 6, 8, 7}, 8));  // expects 4. (2+1+5, 1+5+2, 2+6, 8)
        log(subarraySum4(new int[]{-1, -1, 1}, 0));               // expects 1. (-1+1)
        log(subarraySum4(new int[]{4, 2, -1, 5, -5}, 5));         // expects 3. (4+2-1, 4+2-1+5-5, 5)
    }
}
