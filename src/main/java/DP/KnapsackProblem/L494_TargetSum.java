package DP.KnapsackProblem;

import java.util.Arrays;

import static Utils.Helpers.*;

/*
* Target Sum
*
* - 给定一个非零数字序列，在这些数字前面加上 + 或 - 号，求一共有多少种方式使其计算结果为给定的整数 S。
*
* - 分析：容量为 S，nums 中的每个元素都有 + 或 - 两种选择
*   - 定义子问题：f(i, s) 表示"用前 i 个元素填充 s 共有几种方式"；
*   - 状态转移方程：f(i, s) = sum(f(i-1, s-nums[i]), f(i-1, s+nums[i]))。
*   - 填表：
*     i\s  0  1  2  3
*      0   0  1  0  0
*      1   2  0  1  0
*      2   0  3  0  1
*      3   6  0  4  0
*      4   0  10 0  5
* */

public class L494_TargetSum {
    /*
    * 解法1：Back-tracking（即 DFS，也是 Brute force)
    * - TODO: Brute force 与 Back-tracking 与 DFS 的关系？？？
    * - 思路：∵ nums 中的每个元素都有 + 或 - 两种选择 ∴ 每个选择都会产生两条路径。比如对 nums=[1,2,3], S=0 来说：
    *                       0
    *                +1/        \-1
    *              1               -1
    *         +2/  -2\          +2/  -2\
    *        3       -1        1       -3
    *    +3/ -3\  +3/ -3\  +3/ -3\  +3/ -3\
    *    6     0  2    -4   4   -2   0    -6
    *   可见形成了一个树形结构，而树天然具有递归性，因此可采用回溯法（即 DFS）求解。
    * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
    * */
    public static int findTargetSumWays(int[] nums, int S) {
        if (nums == null || nums.length == 0) return 0;
        int sum = Arrays.stream(nums).reduce(0, Integer::sum);
        if (S > sum || S < -sum) return 0;  // 注意 edge cases
        return dfs(nums, S, 0);
    }

    private static int dfs(int[] nums, int s, int i) {
        if (i == nums.length) return s == 0 ? 1 : 0;  // i = nums.length 时递归到底，若此时 s 为0则说明找到一个解
        return dfs(nums, s - nums[i], i + 1) + dfs(nums, s + nums[i], i + 1);
    }

    /*
    * 解法1：
    * */
    public static int findTargetSumWays1(int[] nums, int S) {
        return 0;
    }

    public static void main(String[] args) {
        log(findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3));
        /*
        * expects 5:
        *   -1+1+1+1+1 = 3
        *   +1-1+1+1+1 = 3
        *   +1+1-1+1+1 = 3
        *   +1+1+1-1+1 = 3
        *   +1+1+1+1-1 = 3
        * */

        log(findTargetSumWays(new int[]{2, 1, 1, 2}, 0));
        /*
        *  expects 4:
        *    +2-1+1-2 = 0
        *    -2+1-1+2 = 0
        *    +2+1-1-2 = 0
        *    -2-1+1+2 = 0
        * */
    }
}
