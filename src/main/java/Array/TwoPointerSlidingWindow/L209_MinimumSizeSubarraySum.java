package Array.TwoPointerSlidingWindow;

import static Utils.Helpers.log;

/*
* Minimum Size Subarray Sum
*
* - Given an array of n positive integers and a positive integer s, find the minimal length of a
*   contiguous subarray (连续子数组) of which the sum ≥ s. If there isn't one, return 0 instead.
* - 题中要求找到元素之和 ≥ s 的最短子串，即对于子串 nums[l...r] 来说，需要在 sum(l...r) ≥ s 的基础上找到
*   最小的 r-l+1。可见本题需要通过改变 l 和 r 来找到符合要求的子串。
* */

public class L209_MinimumSizeSubarraySum {
    /*
    * 解法1：brute force
    * - 时间复杂度 O(n^3)，空间复杂度 O(1)。
    * - 思路是用双重循环遍历 l 和 r 的所有组合，从而遍历所有子串，再对每个子串中的所有元素求和。
    * */
    public static int minSubArrayLen(int s, int[] nums) {
        if(s <= 0 || nums == null)
            throw new IllegalArgumentException("Illegal Arguments");

        int minLen = nums.length + 1;
        for (int l = 0; l < nums.length; l++) {
            for (int r = 0; r < nums.length; r++) {
                int sum = 0;
                for (int i = l; i <= r; i++)
                    sum += nums[i];
                if (sum >= s)
                    minLen = Math.min(r - l + 1, minLen);
            }
        }

        return minLen == nums.length + 1 ? 0 : minLen;  // 注意若未找到 ≥ s 的子串则返回0
    }

    /*
    * 解法2：optimised brute force
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * - 思路是通过空间换时间，将元素之和按索引缓存起来，从而加速解法1中的"求每个子串中的元素之和"这一步（这在子串求和问题中是个常见策略）。
    * */
    public static int minSubArrayLen2(int s, int[] nums) {
        if (s <= 0 || nums == null)
            throw new IllegalArgumentException("Illegal Arguments");

        // sums[i] 存放 nums[0...i-1] 的和，这样有了 sums，后面就不再需要对每组 l 和 r 都求一遍元素之和了
        int[] sums = new int[nums.length + 1];
        for (int i = 1; i < sums.length; i++)
            sums[i] = sums[i - 1] + nums[i - 1];

        int minLen = nums.length + 1;
        for (int l = 0; l < nums.length; l++)
            for (int r = l; r < nums.length; r++)
                if (sums[r + 1] - sums[l] >= s)  // sums[r+1] - sums[l] = nums[0..r] 之和 - nums[0..l-1] 之和 = nums[l..r] 之和
                    minLen = Math.min(r - l + 1, minLen);

        return minLen == nums.length + 1 ? 0 : minLen;  // 注意若未找到 ≥ s 的子串则返回0
    }

    public static void main(String[] args) {
        int[] nums = new int[] {2, 3, 1, 2, 4, 3};
        log(minSubArrayLen(7, nums));  // expects 2. The subarray [4,3] has the minimal length

        int[] nums3 = new int[] {2, 3, 1, 2, 4, 3};
        log(minSubArrayLen2(7, nums3));  // expects 2
    }
}
