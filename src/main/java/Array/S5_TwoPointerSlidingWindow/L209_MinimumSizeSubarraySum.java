package Array.S5_TwoPointerSlidingWindow;

import static Utils.Helpers.log;

/*
 * Minimum Size Subarray Sum
 *
 * - Given an array of positive integers and a positive integer s, find the minimal length of a contiguous
 *   subarray (连续子串) of which the sum ≥ s. If there isn't one, return 0 instead.
 *
 * - 题中要求找到元素之和 ≥ s 的最短子串，即对于子串 nums[l...r] 来说，需要在 sum(l...r) ≥ s 的基础上找到最小的 r-l+1。
 *   可见本题需要通过改变 l 和 r 来找到符合要求的子串。
 * */

public class L209_MinimumSizeSubarraySum {
    /*
     * 解法1：brute force
     * - 思路：用双重循环遍历 l、r 的所有组合（即遍历所有可能的子串），再对每个子串中的所有元素求和。
     * - 时间复杂度 O(n^3)，空间复杂度 O(1)。
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

        return minLen == nums.length + 1 ? 0 : minLen;  // 若未找到 ≥ s 的子串则返回0
    }

    /*
     * 解法2：Optimised brute force（Prefix sum）
     * - 思路：采用空间换时间的 prefix sum 技巧，让 sums[i] 记录 nums[0..i-1] 的和，从而不再需要像解法1中那样遍历每个
     *   l, r 组合（即每个子串）来求元素之和。
     * - 实现：之所以让 sums[i] 记录 nums[0..i-1] 而非 nums[0..i] 之和是因为想留出 sums[0]=0 以便于后面的计算
     *   （sums[r+1] - sums[l]）不会越界。
     * - 💎 经验：Prefix sum 在子串求和问题中是个常见策略。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int minSubArrayLen2(int s, int[] nums) {
        if (s <= 0 || nums == null)
            throw new IllegalArgumentException("Illegal Arguments");

        int[] sums = new int[nums.length + 1];  // ∵ 要让 sums[i] 记录 nums[0..i-1] 之和 ∴ 需要多开辟一个空间
        for (int i = 1; i < sums.length; i++)   // i 取值也要从1开始
            sums[i] = sums[i - 1] + nums[i - 1];

        int minLen = nums.length + 1;
        for (int l = 0; l < nums.length; l++)
            for (int r = l; r < nums.length; r++)
                if (sums[r + 1] - sums[l] >= s)  // sums[r+1] - sums[l] = nums[0..r] 之和 - nums[0..l-1] 之和 = nums[l..r] 之和
                    minLen = Math.min(r - l + 1, minLen);

        return minLen == nums.length + 1 ? 0 : minLen;  // 若未找到 ≥ s 的子串则返回0
    }

    /*
     * 解法3：窗口滑动
     * - 思路：∵ 是求最小子串 ∴ 可尝试滑动窗口方法求解 —— 控制窗口左右边界的滑动来找到所需子串。通过观察 test case 可得到窗口
     *   滑动的控制方式：当窗口中元素之和 < s 时扩展窗口，≥ s 时收缩窗口比较最小的子串的长度。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int minSubArrayLen3(int s, int[] nums) {
        if (s <= 0 || nums == null)
            throw new IllegalArgumentException("Illegal Arguments");

        int minLen = nums.length + 1;
        int l = 0, r = -1, sum = 0;  // 右边界初始化为-1，使得初始窗口不包含任何元素，这样初始 sum 才能为0

        while (l < nums.length) {    // 只要 l 还在数组内就继续滑动（当 r 抵达数组末尾后，l 还得继续滑动直到也抵达末尾后整个滑动过程才算结束）
            if (sum < s && r + 1 < nums.length)  // 下一句中 r 会先++再访问数组 ∴ 这里要处理越界情况
                sum += nums[++r];
            else                     // 若 sum ≥ s 或 r 已经到头
                sum -= nums[l++];
            if (sum >= s)
                minLen = Math.min(r - l + 1, minLen);
        }

        return minLen == nums.length + 1 ? 0 : minLen;  // 若未找到 ≥ s 的子串则返回0
    }

    /*
     * 解法4：窗口滑动的另一实现（略繁琐）
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int minSubArrayLen4(int s, int[] nums) {
        if (s <= 0 || nums == null)
            throw new IllegalArgumentException("Illegal Arguments");

        int minLen = nums.length + 1;
        int l = 0, r = -1, sum = 0;

        while (r + 1 < nums.length) {  // ∵ 下面使用 while 查找 ∴ 这里只需 r 抵达数尾后整个滑动过程即结束，而又 ∵ 下面的 r+1 不能越界 ∴ 这里是 r+1 < nums.length
            while (sum < s && r + 1 < nums.length)
                sum += nums[++r];
            if (sum >= s)              // 窗口停止扩展时的 sum 一定 ≥ s ∴ 计算一下 minLen
                minLen = Math.min(r - l + 1, minLen);
            while (sum >= s && l < nums.length) {  // 再开始收缩窗口
                sum -= nums[l++];
                if (sum >= s)                      // 每次收缩一步后都再计算一遍 minLen
                    minLen = Math.min(r - l + 1, minLen);
            }
        }

        return minLen == nums.length + 1 ? 0 : minLen;
    }

    public static void main(String[] args) {
        log(minSubArrayLen2(7, new int[]{2, 3, 1, 2, 4, 3}));  // expects 2. [4, 3]
        log(minSubArrayLen2(6, new int[]{1, 2, 3, 5}));        // expects 2. [3, 5]
        log(minSubArrayLen2(5, new int[]{1, 2, 3, 5, 7}));     // expects 1. [5] or [7]
        log(minSubArrayLen2(4, new int[]{1, 1, 1, 1}));        // expects 4. [1, 1, 1, 1]
        log(minSubArrayLen2(8, new int[]{1, 2, 3}));           // expects 0.
    }
}
