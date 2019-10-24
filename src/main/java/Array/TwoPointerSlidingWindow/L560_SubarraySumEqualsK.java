package Array.TwoPointerSlidingWindow;

import static Utils.Helpers.log;

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
     * - 思路：超时解中的累加过程其实可以在移动右指针时同步进行，从而去掉最内从的循环，将复杂度降低一个次方。
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
     * 解法2：双指针 + Saving cummulative sums
     * - 思路：解法1中双指针的滑动过程其实可以通过将不同段的 sum 相加减来表达：sum[i..j] = sum[0..j] - sum[0..i]。因此另一
     *   思路是先为每个位置 i 计算出 nums[0..i] 的和 sum[i]，然后通过双指针滑动来遍历该公式中的所有情况。
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
     * 解法3：
     *
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int subarraySum3(int[] nums, int k) {
        return 0;
    }

    public static void main(String[] args) {
        log(subarraySum(new int[]{1, 1, 1}, 2));                 // expects 2. (1+1, 1+1)
        log(subarraySum(new int[]{1, 2, 3}, 3));                 // expects 2. (1+2, 3)
        log(subarraySum(new int[]{4, 2, 1, 5, 2, 6, 8, 7}, 8));  // expects 4. (2+1+5, 1+5+2, 2+6, 8)
        log(subarraySum(new int[]{-1, -1, 1}, 0));               // expects 1. (-1+1)
        log(subarraySum(new int[]{4, 2, -1, 5, -5}, 5));         // expects 3. (4+2-1, 4+2-1+5-5, 5)
    }
}
