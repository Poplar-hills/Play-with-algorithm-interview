package DP.S5_LongestSubsequence;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Longest Increasing Subsequence
*
* - Given an unsorted array of integers, find the length of longest increasing subsequence.
*   There may be more than one LIS combination, it is only necessary for you to return the length.
* - Follow up: Could you improve it to O(nlogn) time complexity?
*
* - 注意澄清需求：
*   1. 区分子串和子序列 —— 子串要求每个数相连，而子序列中间可以不是连着的，在本题中为子序列；
*   2. 定义“increasing” —— 前后两个数相等算不算是 increasing？在本题中不算，要求绝对上升。
*
* - 初步分析：
*   - 可以先思考暴力解法，即求出所有的子序列，然后过滤出其中的上升子序列，最后再得到最大长度。其中 ∵ 每个元素都有放/不放两种选择
*     ∴ 子序列的个数为 2^n；而判断每个子序列是否是上升的需要 O(n) 复杂度；因此总复杂度为 O((2^n)*n)。
*   💎 从上面分析中可见，子序列的问题本质上是组合问题，而组合问题肯定都可以通过递归求解，关键就在于是否找到重叠子问题和最优子结构，
*     从而使用 Memoization 或 DP 进行优化，若可以的话时间复杂度会大大降低。
* */

public class L300_LongestIncreasingSubsequence {
    /*
     * 解法1：Recursion + Memoization
     * - 思路：对每个元素 nums[i] 都回头去看它是否能跟在它前面的某个元素 nums[j] 之后，并从中找到元素个数最多的组合：
     *   - 定义子问题：f(i) 表示“在前 i 个元素中，以第 i 个元素为结尾（即一定放第 i 个元素）的最长子序列的长度”；
     *   - 状态转移方程：f(i) = max(1 + f(j))，其中 j ∈ [0,i)，且 nums[j] < nums[i]。
     *   - 验证：对于 nums=[1, 5, 8, 3, 0, 9] 来说：
     *          [1, 5, 8, 3, 0, 9]
     *           1                   - f(0) = 1
     *           1  2                - 5可以接在1后面 ∴ f(1) = 1 + f(0) = 2
     *           1  2  3             - 8接在5后面所得子序列最长 ∴ f(2) = max(1+f(0), 1+f(1)) = 3
     *           1  2  3  2          - 3只能接在接在1后面 ∴ f(3) = 1 + f(0) = 2
     *           1  2  3  2  1       - 0没法接在前面任何一个元素之后 ∴ f(4) = 1
     *           1  2  3  2  1  4    - 9接在8后面所得子序列最长 ∴ f(5) = max(1+f(0), 1+f(1), ..., 1+f(4)) = 4
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int[] lens = new int[nums.length];
        Arrays.fill(lens, -1);

        for (int i = 0; i < nums.length; i++)
            lens[i] = helper(nums, i, lens);  // lens 同时也作为 cache 传入

        return Arrays.stream(lens).reduce(Math::max).getAsInt();
    }

    private static int helper(int[] nums, int i, int[] lens) {
        if (i == 0) return 1;
        if (lens[i] != -1) return lens[i];

        int maxLen = 1;              // 每个元素的 LIS 至少为1
        for (int j = 0; j < i; j++)
            if (nums[j] < nums[i])
                maxLen = Math.max(maxLen, 1 + helper(nums, j, lens));

        return maxLen;
    }

    /*
     * 解法2：DP
     * - 思路：解法1的 bottom-up 方式。仍然是对每个元素 nums[i] 都回头去看它是否能跟在它前面的某个元素 nums[j] 之后，并从中
     *   找到元素个数最多的组合。状态转移方程：f(i) = max(1 + f(j))，其中 j ∈ [0,i)，且 nums[j] < nums[i]。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int lengthOfLIS2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);    // 每个元素的 LIS 最少为1

        int maxLen = 1;        // 只要 nums 中有元素，res 就至少为1（见 test case 3）
        for (int i = 1; i < nums.length; i++)
            for (int j = 0; j < i; j++)  // j ∈ [0,i)
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], 1 + dp[j]);
                    maxLen = Math.max(maxLen, dp[i]);
                }

        return maxLen;
    }

    /*
     * 解法3：插入排序
     * - 思路：题中 Follow up 中问是否有 O(nlogn) 的解法。当有大 O 中有 log 时要联想到二分查找，而二分查找需要数组是 sorted。
     *   因此可想如何通过构造有序数组来求得上升子序列。视频讲解 SEE: https://www.youtube.com/watch?v=YoeWZ3ELMEk (7'58'')。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
     * */
    public static int lengthOfLIS3(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int[] lis = new int[nums.length];  // 记录最长上升子序列
        int len = 0;

        for (int num : nums) {
            int i = Arrays.binarySearch(lis, 0, len, num);  // 在 lis[0..len) 中查找 num 的索引（只有找到 num 时 i 才 ≥0）
            if (i < 0) i = -(i + 1);  // 若找不到，则将 i 转换成插入点（见 Arrays.binarySearch 文档）
            lis[i] = num;             // 在插入点处插入元素
            if (i == len) len++;      // 若插入点在最右边（num 比 lis 中的元素都大）则直接插入到 lis 末尾，并维护 len
        }

        return len;
    }

    public static void main(String[] args) {
        log(lengthOfLIS(new int[]{1, 5, 8, 3, 0, 9}));            // expects 4. One of the LISs is [1,5,8,9]
        log(lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));  // expects 4. One of the LISs is [2,3,7,101]
        log(lengthOfLIS(new int[]{1, 3, 6, 7, 9, 4, 10, 5, 6}));  // expects 6.
        log(lengthOfLIS(new int[]{0}));                           // expects 1.
    }
}