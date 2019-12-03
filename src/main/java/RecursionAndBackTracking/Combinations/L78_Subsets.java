package RecursionAndBackTracking.Combinations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Subsets
 *
 * - Given a set of distinct integers, return all possible subsets (the power set).
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
     *   由此可见：
     *     - 在填充过程中得到的所有中间结果和最终结果都是有效解；
     *     - 递归填充过程的结束时机是到达3，即 nums 的最后一个元素。
     *
     * - 时间复杂度 O(2^n)：C(n,1) + C(n,2) + ... + C(n,n) = 2^n - C(n,0) = O(2^n)；
     * - 空间复杂度 O(n)。
     * */
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        res.add(new ArrayList<>());          // 空列表解
        helper(nums, 0, new ArrayList<>(), res);
        return res;
    }

    private static void helper(int[] nums, int i, List<Integer> list, List<List<Integer>> res) {
        if (i == nums.length) return;        // 到达 nums 尾部时递归结束
        for (int j = i; j < nums.length; j++) {
            list.add(nums[j]);
            res.add(new ArrayList<>(list));  // 收集所有填充结果
            helper(nums, j + 1, list, res);
            list.remove(list.size() - 1);
        }
    }

    /*
     * 解法2：Iteration
     * - 思路：对于 nums 中的每个元素都可以选用或不选用：
     *     1. 若选用，则将该元素追加到所有已找到的 subset 中；
     *     2. 若不选用，则所有已找到的 subset 不变。
     *   例如对于 [1,2,3] 来说，最初有空集解 []，之后：
     *     - 若选用1，则将1追加到 [] 中，得到解 [1]；若不选用1，则还是 []；
     *       将两个解合并得到 [[],[1]]。
     *     - 若选用2，则将2追加到 [[],[1]] 中的每个解里，得到解 [[2],[1,2]]；若不选用2，则还是 [[],[1]]；
     *       将两部分合并得到 [[],[1],[2],[1,2]]。
     *     - 若选用3，则将3追加到 [[],[1],[2],[1,2]] 中的每个解里，得到解 [[3],[1,3],[2,3],[1,2,3]]；若不选用2，则还是
     *       [[],[1],[2],[1,2]]；将两部分合并得到 [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]。
     * - 时间复杂度 O(n * 2^n)：从上面分析可知，对 nums 中的每个元素都有“选用”或“不选用”两种选择，且每个元素都会把解的个数
     *   翻倍 ∴ 复杂度是 O(2^n)。
     * - 空间复杂度 O(1)。
     * */
    public static List<List<Integer>> subsets2(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        res.add(new ArrayList<>());

        for (int n : nums) {
            int size = res.size();
            for (int i = 0; i < size; i++) {    // 遍历 res 中的每个 subset
                List<Integer> list = new ArrayList<>(res.get(i));  // 复制 subset，并往里追加 n
                list.add(n);
                res.add(list);
            }
        }

        return res;
    }

    public static void main(String[] args) {
        log(subsets2(new int[]{1, 2, 3}));  // expects [[3], [1], [2], [1,2,3], [1,3], [2,3], [1,2], []]
        log(subsets2(new int[]{3, 2, 1}));  // expects [[3], [1], [2], [1,2,3], [1,3], [2,3], [1,2], []]
    }
}
