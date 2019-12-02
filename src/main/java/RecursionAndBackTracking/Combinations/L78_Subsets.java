package RecursionAndBackTracking.Combinations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Subsets
 *
 * - Given a set of distinct integers, nums, return all possible subsets (the power set).
 *
 * - Note: The solution set must not contain duplicate subsets (e.g. [1,2] and [2,1] are duplicate subsets).
 * */

public class L78_Subsets {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：分析题目可知，题目的另一种描述是使用 nums 中的元素填充空列表，一共有多少种不重复的填法。举例分析，对于 [1,2,3]：
     *                          []
     *                 1/       2|       3\
     *                [1]       [2]      [3]
     *              2/   3\     3|
     *            [1,2] [1,3]  [2,3]
     *             3|
     *           [1,2,3]
     *   可见：
     *     - 在填充过程中得到的所有中间结果和最终结果都是有效解；
     *     - 递归填充过程的结束时机是到达3，即 nums 的最后一个元素。
     *
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        res.add(new ArrayList<>());
        helper(nums, 0, new ArrayList<>(), res);
        return res;
    }

    private static void helper(int[] nums, int i, List<Integer> list, List<List<Integer>> res) {
        if (i == nums.length) return;
        for (int j = i; j < nums.length; j++) {
            list.add(nums[j]);
            res.add(new ArrayList<>(list));  // 收集所有填充结果
            helper(nums, j + 1, list, res);
            list.remove(list.size() - 1);
        }
    }

    public static void main(String[] args) {
        log(subsets(new int[]{1, 2, 3}));  // expects [[3], [1], [2], [1,2,3], [1,3], [2,3], [1,2], []]
    }
}
