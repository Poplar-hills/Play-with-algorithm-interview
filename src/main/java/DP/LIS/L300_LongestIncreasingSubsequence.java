package DP.LIS;

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
* - 分析：
*   - 可以先思考暴力解法，即求出所有的子序列，然后过滤出其中的上升子序列，最后再得到最大长度。其中 ∵ 每个元素都有放/不放两种选择
*     ∴ 子序列的个数为 2^n；判断每个子序列是否是上升的需要 O(n) 复杂度；因此总复杂度为 O((2^n)*n)。
*   - 从上面分析中可见，子序列的问题本质上是组合问题，而组合问题肯定都可以通过递归求解，关键就在于是否找到重叠子问题和最优子结构，
*     从而使用 Memoization 或 DP 进行优化，若可以的话时间复杂度会大大降低。
*
* */

public class L300_LongestIncreasingSubsequence {
    /*
    * 解法1：Recursion + Memoization
    * - 思路：
    *   - 定义子问题：f(i) 表示“在前 i 个元素中，以第 i 个元素为结尾（即一定放第 i 个元素）的最长子序列的长度”；
    *   - 状态转移方程：f(i) = max(1 + f(j))，其中 j ∈ [0,i)，且 nums[j] < nums[i]。
    *   - 验证：对于 nums=[1, 5, 8, 3, 0, 9]，从头到尾遍历：
    *          f(0) = 1；                             - j 无值可取
    *          f(1) = 1 + f(0) = 2；                  - j 只有0可取
    *          f(2) = max(1 + f(0), 1 + f(1)) = 3；   - j 可取0、1
    *          f(3) = 1 + f(0) = 2；                  - ∵ 前3个元素中只有1能与3组成上升子序列 ∴ j 只能取0
    *          f(4) = 1；                             - ∵ 前4个元素中没有能与3组成上升子序列的元素 ∴ j 无值可取
    *          f(5) = max(1 + f(0), 1 + f(1), ..., 1 + f(4)) = 4；
    *
    *     由此可得到：nums=[1, 5, 8, 3, 0, 9]，最后遍历得到最大 LIS 长度为4。
    *                     1  2  3  2  1  4
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    public static int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int[] lens = new int[nums.length];
        Arrays.fill(lens, -1);

        for (int i = 0; i < nums.length; i++)
            lens[i] = lengthOfLIS(nums, i, lens);

        return Arrays.stream(lens).reduce(Math::max).getAsInt();
    }

    private static int lengthOfLIS(int[] nums, int i, int[] lens) {
        if (i == 0) return 1;
        if (lens[i] != -1) return lens[i];

        int maxLen = 1;  // 每个元素的 LIS 至少为1
        for (int j = 0; j < i; j++)
            if (nums[j] < nums[i])
                maxLen = Math.max(maxLen, 1 + lengthOfLIS(nums, j, lens));

        return maxLen;
    }

    /*
    * 解法2：DP
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    public static int lengthOfLIS2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);  // 每个元素的 LIS 都初始化为1

        int res = 1;           // 只要 nums 中有元素，res 就至少为1（见 test case 3）
        for (int i = 1; i < nums.length; i++)
            for (int j = 0; j < i; j++)
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], 1 + dp[j]);
                    res = Math.max(res, dp[i]);
                }

        return res;
    }

    /*
    * 解法3：DP
    * - 思路：题中 Follow up 中问是否有 O(nlogn) 的解法。当有大 O 中有 log 时要联想到二分查找，而二分查找需要数组是 sorted。
    *   因此可想如何通过构造有序数组来求得上升子序列。视频讲解 SEE: https://www.youtube.com/watch?v=YoeWZ3ELMEk (7'58'')。
    * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
    * */
    public static int lengthOfLIS3(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int[] dp = new int[nums.length];  // 保存最长上升子序列的元素
        int len = 0;

        for (int num : nums) {
            int i = Arrays.binarySearch(dp, 0, len, num);  // 在 dp[0..len) 中查找 num 的索引（只有 num 重复时 i 才为正）
            if (i < 0) i = -(i + 1);    // 若找不到，则将 i 转换成插入点（见 Arrays.binarySearch 文档）
            dp[i] = num;                // 替换元素
            if (i == len) len++;        // 若 num 比 dp 中的元素都大，则不会替换 dp 中的任何元素，而是直接插入，因此要维护 len
        }

        return len;                     // len 即是 LIS 的长度
    }

    public static void main(String[] args) {
        log(lengthOfLIS3(new int[]{1, 5, 8, 3, 0, 9}));            // expects 4. One of the LISs is [1,5,8,9]
        log(lengthOfLIS3(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));  // expects 4. One of the LISs is [2,3,7,101]
        log(lengthOfLIS3(new int[]{0}));                           // expects 1.
    }
}