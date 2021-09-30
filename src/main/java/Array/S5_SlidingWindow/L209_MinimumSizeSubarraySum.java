package Array.S5_SlidingWindow;

import static Utils.Helpers.log;

/*
 * Minimum Size Subarray Sum
 *
 * - Given an array of positive integers nums and a positive integer s, find the minimal length of a
 *   contiguous subarray (连续子串) of which the sum ≥ s. If there isn't one, return 0 instead.
 *
 * - 题中要求找到元素之和 ≥ s 的最短子串，即对于子串 nums[l...r] 来说，需要在 sum(l...r) ≥ s 的基础上找到最小的 r-l+1。
 *   可见本题需要通过改变 l 和 r 来找到符合要求的子串。
 * - 💎 注意：题中说 nums 中的元素都是 positive integers ∴ 不会有 L560 错误解中的问题，即可以考虑使用滑动窗口求解。
 * */

public class L209_MinimumSizeSubarraySum {
    /*
     * 解法1：Brute force
     * - 思路：用双重循环遍历所有子串（即遍历子串边界 l、r 的所有组合），再对每个子串中的所有元素求和。
     * - 💎 经验：遍历一个数组的所有子串要使用双重循环，复杂度为 O(n^2)。
     * - 时间复杂度 O(n^3)，空间复杂度 O(1)。
     * */
    public static int minSubArrayLen(int s, int[] nums) {
        if(s <= 0 || nums == null) return 0;
        int n = nums.length, minLen = n + 1;

        for (int l = 0; l < n; l++) {
            for (int r = l; r < n; r++) {
                int sum = 0;
                for (int i = l; i <= r; i++)
                    sum += nums[i];
                if (sum >= s)
                    minLen = Math.min(minLen, r - l + 1);
            }
        }

        return minLen == n + 1 ? 0 : minLen;  // 若未找到 ≥ s 的子串则返回0
    }

    /*
     * 解法2：Prefix Sum
     * - 思路：解法1中的问题在于每遍历到一个子串后就要为其求一次和，多次求和过程中有很多重复计算。对此可采用以空间换时间的
     *   prefix sum 技巧，让 sums[i] 记录 nums[0..i] 的和（例如 sums[2] 记录第0、1、2号元素之和），使得不再需要多次
     *   重复计算序列之和。
     * - 💎 经验：Prefix Sum 本质是为每个位置缓存累加和（cummulative sums），是求解“数组区间求和”类问题时的常用技巧。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int minSubArrayLen2(int s, int[] nums) {
        if (s <= 0 || nums == null) return 0;
        int n = nums.length;
        int[] preSums = new int[n];           // prefix sum 数组
        preSums[0] = nums[0];

        for (int i = 1; i < n; i++)           // 单次遍历即可构造 prefix sum 数组（i 的取值要从1开始）
            preSums[i] = preSums[i - 1] + nums[i];

        int minLen = n + 1;
        for (int l = 0; l < n; l++) {         // 外面仍然是双重循环遍历所有子串
            for (int r = l; r < n; r++) {
                if (preSums[r] - preSums[l] + nums[l] >= s) {    // 里面使用 prefix sum 快速得到该子串 nums[l..r] 的元素之和 =
                    minLen = Math.min(minLen, r - l + 1);  // nums[0..r] 之和 - nums[0..l) 之和 = preSums[r] - preSums[l] + nums[l]
                    break;                    // ∵ 已经找到了以 l 为起点的最短符合条件的子串，而后面的子串只会更长（∵ r 在增大）∴ 无需再遍历
                }
            }
        }
        return minLen == n + 1 ? 0 : minLen;  // 若未找到 ≥ s 的子串则返回0
    }

    /*
     * 解法3：窗口滑动
     * - 思路：💎 找连续子串的问题可尝试滑动窗口方法求解 —— 控制窗口左右边界的滑动来找到所需子串。通过观察 test case
     *   可知窗口滑动的控制方式：当窗口中元素之和在 < s 时扩展窗口，在 ≥ s 时收缩窗口并更新最小子串的长度。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int minSubArrayLen3(int s, int[] nums) {
        if (s <= 0 || nums == null) return 0;
        int n = nums.length, minLen = n + 1;
        int l = 0, r = -1, sum = 0;    // 右边界初始化为-1，使得初始窗口不包含任何元素，这样初始 sum 才能为0

        while (l < n) {                // 当 r 抵达数组末尾后，l 还得继续滑动直到也抵达末尾后整个滑动过程才算结束
            if (sum < s && r + 1 < n)  // ∵ 下一句中 r 要在++后访问元素 ∴ 这里要处理越界情况
                sum += nums[++r];
            else                       // 若 sum ≥ s 或 r 已经到头
                sum -= nums[l++];
            if (sum >= s)
                minLen = Math.min(minLen, r - l + 1);
        }

        return minLen == n + 1 ? 0 : minLen;  // 若未找到 ≥ s 的子串则返回0
    }

    /*
     * 解法4：窗口滑动 + 内部双 while 查找
     * - 思路：与解法3一致。
     * - 实现：比解法3略繁琐 ∵ 有两个地方都需要更新 minLen。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int minSubArrayLen4(int s, int[] nums) {
        if (s <= 0 || nums == null) return 0;
        int n = nums.length, minLen = n + 1;
        int l = 0, r = -1, sum = 0;

        while (r < n - 1) {  // ∵ 下面使用 while 查找 ∴ 这里只需 r 抵达数尾后整个滑动过程即结束，又 ∵ 下面的 r+1 不能越界 ∴ 这里是 r < n-1
            while (sum < s && r + 1 < n)
                sum += nums[++r];
            if (sum >= s)                // 窗口停止扩展时 sum ≥ s ∴ 此时要计算 minLen
                minLen = Math.min(minLen, r - l + 1);
            while (sum >= s && l < n) {  // 再开始收缩窗口
                sum -= nums[l++];
                if (sum >= s)            // 每次收缩一步后都再计算一遍 minLen
                    minLen = Math.min(minLen, r - l + 1);
            }
        }

        return minLen == n + 1 ? 0 : minLen;
    }

    /*
     * 解法5：窗口滑动 + Prefix Sum
     * - 思路：结合解法2、3。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int minSubArrayLen5(int s, int[] nums) {
        int n = nums.length, minLen = n + 1;

        int[] preSums = new int[n];
        for (int i = 0; i < preSums.length; i++)  // 与解法2中的 prefix sum 生成、使用方式一致
            preSums[i] = i == 0 ? nums[0] : preSums[i - 1] + nums[i];

        for (int l = 0, r = 0; l < n; ) {  // 比解法2、3 多了 r < n 的条件
            if (preSums[r] - preSums[l] + nums[l] < s) {   // 扩展窗口
                r++;
                if (r == n) break;         // 当 r 自增到 n 时需立即结束循环，不再执行下面逻辑，否则数组会越界
            } else {                       // 收缩窗口
                minLen = Math.min(minLen, r - l + 1);  // 只在收缩收缩窗口时记录最小长度（∵ 此时满足 > s 的条件）
                l++;
            }
        }

        return minLen == n + 1 ? 0 : minLen;
    }

    public static void main(String[] args) {
        log(minSubArrayLen5(7, new int[]{2, 3, 1, 2, 4, 3}));  // expects 2. [4, 3]
        log(minSubArrayLen5(5, new int[]{1, 2, 3, 5, 7}));     // expects 1. [5] or [7]
        log(minSubArrayLen5(4, new int[]{1, 1, 1, 1}));        // expects 4. [1, 1, 1, 1]
        log(minSubArrayLen5(8, new int[]{1, 2, 3}));           // expects 0.
    }
}
