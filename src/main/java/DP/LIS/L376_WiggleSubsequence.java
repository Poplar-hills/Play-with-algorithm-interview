package DP.LIS;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Wiggle Subsequence
*
* - 若一个序列中相邻数字是升序、降序轮流交替的，则该序列是一个 wiggle subsequence。例如：
*   ✅ [1, 7, 4, 9, 2, 5]  ❎ [1, 4, 7, 2, 5]  ❎ [1, 7, 4, 5, 5]
*   给定一个数组，求其中是 wiggle subsequence 的最长子序列的长度。
* - Follow up: Can you do it in O(n) time?
*
* - 初步分析：同 L300_LongestIncreasingSubsequence。
* */

public class L376_WiggleSubsequence {
    /*
    * 超时解：Recursion + Memoization
    * - 思路：
    *   - 定义子问题：f(i) 表示“在前 i 个数字中，以第 i 个数结尾的并且是 wiggle subsequence 的最长子序列的长度”；
    *     ∵ f(i) 的值取决于 f(i-1) 的值 ∴ 该问题存最优子结构，可以进行递推。
    *   - 状态转移方程：f(i, ord) = max(1 + f(j, !ord))，其中 j ∈ [0,i)，ord 表示升/降序。核心逻辑是：对于每个 nums[i]，
    *     都在 (i, nums.length-1] 范围中寻找下一个能与 nums[i] 连成 wiggle sequence 的数字。
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    public static int wiggleMaxLength(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int[] cache = new int[nums.length];
        Arrays.fill(cache, -1);
        return 1 + Math.max(helper(nums, 0, true, cache), helper(nums, 0, false, cache));  // ∵ 最开始可以为升序也可以为降序 ∴ 取两者中最大的
    }

    private static int helper(int[] nums, int i, boolean up, int[] cache) {
        if (i == nums.length) return 0;
        if (cache[i] != -1) return cache[i];

        int maxLen = 0;
        for (int j = i; j < nums.length; j++)  // 从前往后遍历，若当前是 up 则往后找 !up 的数字，直到最后 i == nums.length
            if ((up && nums[j] > nums[i]) || (!up && nums[j] < nums[i]))
                maxLen = Math.max(maxLen, 1 + helper(nums, j, !up, cache));

        return maxLen;
    }

    /*
    * 解法1：DP
    * - 思路：状态转移方程还是一样的：f(i, ord) = max(1 + f(j, !ord))，其中 j ∈ [0,i)，ord 表示升/降序。但考虑到
    *   ∵ wiggle sequence 是峰谷相连的，且 f(i) 的值取决于 f(i-1) 的值 ∴ 可以维护2个数组，分别记录在第 i 个位置上为
    *   峰时 f 的值，以及在第 i 个位置上为谷时 f 的值，最后取其中最大者即可。
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    public static int wiggleMaxLength1(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] up = new int[n];    // up[i] 保存在前 i 个数字中，以第 i 个数结尾时为升序，并且是 wiggle subsequence 的最长子序列的长度
        int[] down = new int[n];  // down[i] 保存在前 i 个数字中，以第 i 个数结尾时为降序，并且是 wiggle subsequence 的最长子序列的长度

        Arrays.fill(up, 1);
        Arrays.fill(down, 1);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {    // 注意这里与超时解中的范围不一样，这里 j ∈ [0,i)
                if (nums[j] < nums[i])       // 若 i 上为升序，则 f(i, up) = max(1 + f(j, down))
                    up[i] = Math.max(up[i], 1 + down[j]);
                else if (nums[j] > nums[i])  // 若 i 上为降序，则 f(i, down) = max(1 + f(j, up))
                    down[i] = Math.max(down[i], 1 + up[j]);
            }
        }

        return Math.max(up[n - 1], down[n - 1]);
    }

    public static void main(String[] args) {
        log(wiggleMaxLength1(new int[]{1, 17, 5, 10, 13, 15, 10, 5, 16, 8}));  // expects 7. 其中之一是 [1,17,10,13,10,16,8]
        log(wiggleMaxLength1(new int[]{1, 7, 4, 9, 2, 5}));                    // expects 6. 整个序列都是
        log(wiggleMaxLength1(new int[]{3, 3, 3, 2, 5}));                       // expects 3.
        log(wiggleMaxLength1(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}));           // expects 2.
        log(wiggleMaxLength1(new int[]{1, 0}));                                // expects 2.
        log(wiggleMaxLength1(new int[]{0, 0}));                                // expects 1.
        log(wiggleMaxLength1(new int[]{1}));                                   // expects 1.
    }
}