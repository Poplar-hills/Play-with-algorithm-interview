package DP.S5_LongestSubsequence;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
 * Wiggle Subsequence
 *
 * - 若一个序列中相邻数字是升序、降序轮流交替的，则该序列是一个 wiggle subsequence。例如：
 *   ✅ [1, 7, 4, 9]  ❎ [1, 5, 4, 3]  ❎ [1, 7, 4, 4]
 *   给定一个数组，求其中是 wiggle subsequence 的最长子序列的长度。
 * - Follow up: Can you do it in O(n) time?
 *
 * - 分析：仍然是2种思路：
 *   a). DFS 递归：同 L300_LongestIncreasingSubsequence 的初步分析；
 *   b). DP 递推：f(i) 的值取决于 f(i-1) 的值 ∴ 存在最优子结构，可以进行递推（即 DP）。但与 L300 不同，该问题有两个维度需要进
 *       行动态规划 —— 处于峰/谷两种状态下进行动态规划。
 * */

public class L376_WiggleSubsequence {
    /*
     * 超时解1：DFS
     * - 思路：对于 [5, 10, 13, 15, 11, 7, 16, 8]：
     *             1                              - 设5上是谷（第一个元素可以是峰也可以是谷）∴ f(5) = 1
     *             1   2                          - ∵ 5上是谷 ∴ 10可以接到5后面形成 wiggle seqence ∴ f(10) = f(5) + 1 = 2
     *             1   2   2                      - ∵ 10上已经是峰了 ∴ 13只能接到5后面 ∴ f(13) = f(5) + 1 = 2
     *             1   2   2   2                  - 同理15只能接到5后面 ∴ f(15) = f(5) + 1 = 2
     *             1   2   2   2   3              - 11作为谷接到13/15后面时序列最长 ∴ f(11) = f(13/15) + 1 = 3
     *             1   2   2   2   3   3          - ∵ 11上已经是谷了 ∴ 7只能接到10/13/15 = f(10/13/15) + 1 = 3
     *             1   2   2   2   3   3  4       - 16作为峰接到11/7后面时序列最长 ∴ f(16) = f(11/7) + 1 = 4
     *             1   2   2   2   3   3  4  5    - 8作为谷接到16后面时序列最长 ∴ f(8) = f(16) + 1 = 5
     * - 实现：由上面可知，整个过程分别两部分：
     *   1. 从左到右遍历 nums；
     *   2. 对每个元素 nums[i] 都在 [0,i) 范围内搜索前一个能与其连成 wiggle sequence 同时长度最大的元素。
     *   - 注意我们假设第一个元素上是谷得到的最长序列是5，还要求第一个元素上是峰时的情况，两者之中的最大值才是最终结果。
     *   - ∴ 用递归实现时可以从最后一个元素开始，向前递归 —— f(n) -> f(n-1) -> ... -> f(0)。
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
     * */
    public static int wiggleMaxLength(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        return Math.max(helper(nums, nums.length - 1, true),  // ∵ 最开始可以为升序也可以为降序 ∴ 取两者中最大的
                        helper(nums, nums.length - 1, false));
    }

    private static int helper(int[] nums, int i, boolean isPeak) {
        if (i == 0) return 1;  // 递归到底时返回1
        int maxLen = 1;
        for (int j = 0; j < i; j++)                                               // 在 [0,i) 范围内搜索
            if ((isPeak && nums[j] < nums[i]) || (!isPeak && nums[j] > nums[i]))  // 能与 nums[i] 连成 wiggle sequence
                maxLen = Math.max(maxLen, 1 + helper(nums, j, !isPeak));          // 且 sequence 长度最大
        return maxLen;
    }

    /*
     * 超时解2：DFS
     * - 思路：与超时解1一致。
     * - 实现：与超时解1不同在于，本解法从第一个元素开始，向后递归；对每个元素 nums[i] 都在 (i,n] 范围内从前往后搜索下一个能与
     *   nums[i] 连成 wiggle sequence 且长度最大的元素。
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
     * */
    public static int wiggleMaxLength0(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        return Math.max(helper0(nums, 0, true), helper0(nums, 0, false));
    }

    private static int helper0(int[] nums, int i, boolean isPeak) {
        if (i == nums.length) return 0;            // 递归到底时返回0
        int maxLen = 1;
        for (int j = i + 1; j < nums.length; j++)  // 在 [i+1,..] 范围内搜索
            if ((isPeak && nums[j] < nums[i]) || (!isPeak && nums[j] > nums[i]))
                maxLen = Math.max(maxLen, 1 + helper0(nums, j, !isPeak));
        return maxLen;
    }

    /*
     * 解法1：Recursion + Memoization (DFS with cache)
     * - 思路：在超时解1的基础上加入 Memoization。∵ helper 的输入参数有两个：i、isPeak ∴ cache 是二维的。而又因为 isPeak
     *   只有两种取值，因此 cache 只需开辟两行即可，即在峰/谷两种状态下为 i 各设置一行缓存。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int wiggleMaxLength1(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[][] cache = new int[2][n];  // 开辟2行缓存，分别记录 i 处于峰/谷时的计算结果
        for (int[] row : cache)
            Arrays.fill(row, -1);

        return Math.max(helper1(nums, n - 1, true, cache), helper1(nums, n - 1, false, cache));
    }

    private static int helper1(int[] nums, int i, boolean isPeak, int[][] cache) {
        if (i == 0) return 1;
        int[] cacheRow = isPeak ? cache[0] : cache[1];  // 先根据 isPeak 取得相应的缓存行的引用
        if (cacheRow[i] != -1) return cacheRow[i];

        int maxLen = 1;
        for (int j = 0; j < i; j++)
            if ((isPeak && nums[j] < nums[i]) || (!isPeak && nums[j] > nums[i]))
                maxLen = Math.max(maxLen, 1 + helper1(nums, j, !isPeak, cache));

        return cacheRow[i] = maxLen;
    }

    /*
     * 解法2：DP
     * - 思路：
     *   - 定义子问题：f(i) 表示“在前 i 个数字中，以第 i 个数结尾的并且是 wiggle subsequence 的最长子序列的长度”；
     *   - 状态转移方程：f(i, w) = max(1 + f(j, !w))，其中 j ∈ [0,i)，w 表示当前处于峰/谷。
     * - 实现：将解法1中的两行缓存拆成两个 dp 数组来用（其实也可以用一个二维 dp 数组，但拆开更方便些），在遍历过程中同时进行递推。
     *            [1, 17, 5, 10, 13, 15, 10, 5, 16, 8]
     *        up  [1  2   2  4   4   4   4   4   6  6]
     *      down  [1  1   3  3   3   3   5   5   5  7]    - 最后比较这两个数组的最后一个元素即可
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static int wiggleMaxLength2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] up = new int[n];    // up[i] 保存前 i 个数中，以 nums[i] 结尾时处于峰，且是 wiggle subsequence 的最长子序列的长度
        int[] down = new int[n];  // down[i] 保存前 i 个数中，以 nums[i] 结尾时处于谷，且是 wiggle subsequence 的最长子序列的长度
        Arrays.fill(up, 1);
        Arrays.fill(down, 1);

        for (int i = 1; i < n; i++) {     // 从第2个元素开始遍历
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i])    // 若 i 处于峰，则前一个元素一定为降序，即 f(i, up) = max(1 + f(j, down))
                    up[i] = Math.max(up[i], 1 + down[j]);
                if (nums[j] > nums[i])    // 若 i 处于谷，则前一个元素一定为升序，即 f(i, down) = max(1 + f(j, up))
                    down[i] = Math.max(down[i], 1 + up[j]);
            }
        }

        return Math.max(up[n - 1], down[n - 1]);
    }

    /*
     * 解法3：DP（解法2的时间优化版）
     * - 思路：只用一层循环求解。动画演示 SEE: https://leetcode.com/problems/wiggle-subsequence/solution/ (Approach #3)
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int wiggleMaxLength3(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] up = new int[n];
        int[] down = new int[n];
        up[0] = down[0] = 1;      // 不再需要初始化整个数组

        for (int i = 1; i < n; i++) {
            if (nums[i - 1] < nums[i]) {
                up[i] = Math.max(up[i], 1 + down[i - 1]);
                down[i] = down[i - 1];
            } else if (nums[i - 1] > nums[i]) {
                down[i] = Math.max(down[i], 1 + up[i - 1]);
                up[i] = up[i - 1];
            } else {
                up[i] = up[i - 1];
                down[i] = down[i - 1];
            }
        }

        return Math.max(up[n - 1], down[n - 1]);
    }

    /*
     * 解法4：DP（解法3的空间优化版）
     * - 思路：∵ 在解法3中我们只用到了 up[i-1]、down[i-1] ∴ 不需要维护整个数组，只需用两个变量记录 i-1 处的值即可：
     *            [1, 17, 5, 10, 13, 15, 10, 5, 16, 8]
     *        up   1  2      4   4   4          6
     *      down   1      3              5   5      7
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int wiggleMaxLength4(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int up = 1, down = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] < nums[i])
                up = Math.max(up, down + 1);
            if (nums[i - 1] > nums[i])
                down = Math.max(down, up + 1);
        }

        return Math.max(up, down);
    }

    /*
     * 解法5：Greedy
     * - 思路：通过 https://leetcode.com/problems/wiggle-subsequence/solution/ 中 Approach #5 中的解释以及下面第
     *   一条 comment 可知，每次在考虑是否选用 nums[i] 时，若这个元素的值越极端，则给后面留出的选择空间就越大 ∴ 可以采用贪心
     *   算法，即找到 nums 中的所有极值，极值的个数就是 wiggle sequence 的最大长度。
     * - 实现：在遍历过程中记录前后 diff 来识别当前极值（用 test case 1 画图理解）。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int wiggleMaxLength5(int[] nums) {
        int n = nums.length;
        if (n < 2) return n;

        int prevDiff = nums[1] - nums[0];
        int count = prevDiff != 0 ? 2 : 1;  // 初始化 count

        for (int i = 2; i < n; i++) {
            int diff = nums[i] - nums[i - 1];
            if ((diff > 0 && prevDiff <= 0) || (diff < 0 && prevDiff >= 0)) {  // 用前后2个 diff 来判断 wiggle up/down
                count++;
                prevDiff = diff;
            }
        }

        return count;
    }

    /*
     * 解法6：Greedy
     * - 思路：与解法5一致。
     * - 实现：另一种识别极值的方法是通过比较前后两个元素以及一个记录之前峰/谷状态的标志位来判断。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int wiggleMaxLength6(int[] nums) {
        int n = nums.length;
        if (n < 2) return n;

        int count = 1;
        int isUp = 0;  // 1为峰，-1为谷，0不是峰也不是谷

        for (int i = 1; i < n; i++) {
            if ((nums[i - 1] < nums[i] && isUp != 1)) {  // peak
                count++;
                isUp = 1;
            } else if (nums[i - 1] > nums[i] && isUp != -1) {  // valley
                count++;
                isUp = -1;
            }
        }

        return count;
    }
    public static void main(String[] args) {
        log(wiggleMaxLength6(new int[]{5, 10, 13, 15, 11, 7, 16, 8}));  // expects 5. 其中之一是 [10,13,11,16,8]
        log(wiggleMaxLength6(new int[]{1, 7, 4, 9, 2, 5}));             // expects 6. 整个序列都是
        log(wiggleMaxLength6(new int[]{3, 3, 3, 2, 5}));                // expects 3.
        log(wiggleMaxLength6(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}));    // expects 2.
        log(wiggleMaxLength6(new int[]{1, 0}));                         // expects 2.
        log(wiggleMaxLength6(new int[]{0, 0}));                         // expects 1.
        log(wiggleMaxLength6(new int[]{1}));                            // expects 1.
    }
}