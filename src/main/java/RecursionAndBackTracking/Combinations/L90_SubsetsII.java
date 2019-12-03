package RecursionAndBackTracking.Combinations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Subsets II
 *
 * - Given a collection of integers that might contain duplicates, return all possible subsets (the power set).
 *
 * - Note: The solution set must not contain duplicate subsets.
 * */

public class L90_SubsetsII {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：该题与 L40_CombinationSumII 解法1极为类似，区别在于该题中除了最终结果要还收集所有中间结果。
     *   例如对于 [2,1,2] 来说，排序后为 [1,2,2]：
     *               []
     *            1/   2\
     *          [1]     [2]
     *          2|      2|
     *         [1,2]   [2,2]
     *          2|
     *        [1,2,2]
     *
     * - 时间复杂度 O(2^n)：分析方法与 L78_Subsets 解法1一样，但 ∵ 该题中会跳过某些路径 ∴ 复杂度 < O(2^n)。
     * - 空间复杂度 O(n)。
     * */
    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        Arrays.sort(nums);  // ∵ 后面对同层级的重复分支的检查是用 nums[j] 与 nums[j-1] 比较 ∴ 这里要先对 nums 排序
        res.add(new ArrayList<>());
        helper(nums, 0, new ArrayList<>(), res);
        return res;
    }

    private static void helper(int[] nums, int i, List<Integer> list, List<List<Integer>> res) {
        if (i == nums.length) return;
        for (int j = i; j < nums.length; j++) {
            if (j > i && nums[j] == nums[j - 1]) continue;  // 注意是 j>i 而不能是 j>0 ∵ 要跳过的是同一层级的相同分支 ∴ 要从 i+1 开始检测
            list.add(nums[j]);
            res.add(new ArrayList<>(list));  // 收集所有中间结果和最终结果
            helper(nums, j + 1, list, res);
            list.remove(list.size() - 1);
        }
    }

    public static void main(String[] args) {
        log(subsetsWithDup(new int[]{1, 2, 2}));  // expects [[2], [1], [1,2,2], [2,2], [1,2], []]
        log(subsetsWithDup(new int[]{2, 1, 2}));  // expects [[2], [1], [1,2,2], [2,2], [1,2], []]
        log(subsetsWithDup(new int[]{1, 2, 3}));  // expects [[3], [1], [2], [1,2,3], [1,3], [2,3], [1,2], []]
    }
}
