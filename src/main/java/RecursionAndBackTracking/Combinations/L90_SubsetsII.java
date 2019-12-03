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

    /*
     * 解法2：Recursion + Backtracking
     * - 思路：类似 L78_Subsets 解法2的思路，但加入了对重复元素的检测。
     *   例如对于 [2,1,2] 来说，首先排序后为 [1,2,2]。最初有空集解 []，之后：
     *     - 若选用1，则将1追加到 [] 中，得到解 [1]；若不选用1，则还是 []；
     *       将两个解合并得到 [[],[1]]。
     *     - 若选用2，则将2追加到 [[],[1]] 中的每个里，得到解 [[2],[1,2]]；若不选用2，则还是 [[],[1]]；
     *       将两部分合并得到 [[],[1],[2],[1,2]]。
     *     - 若选用2 ∵ 与上一个元素重复 ∴ 只将2追加到 [[],[1],[2],[1,2]] 中的后2个解里，得到解 [[2,2],[1,2,2]]，
     *       将两部分合并得到 [[],[1],[2],[1,2],[2,2],[1,2,2]]。
     * - 时间复杂度 O(2^n)，空间复杂度 O(1)。
     * */
    public static List<List<Integer>> subsetsWithDup2(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        Arrays.sort(nums);
        res.add(new ArrayList<>());

        int size = 0, start;
        for (int i = 0; i < nums.length; i++) {
            start = (i >= 1 && nums[i] == nums[i - 1]) ? size : 0;  // 若出现重复元素，则跳过上次 res 中的所有 subset；若不是重复元素，则正常从0开始
            size = res.size();
            for (int j = start; j < size; j++) {  // 遍历 res 中从索引 start 处开始的 subset
                List<Integer> list = new ArrayList<>(res.get(j));
                list.add(nums[i]);
                res.add(list);
            }
        }

        return res;
    }

    public static void main(String[] args) {
        log(subsetsWithDup2(new int[]{1, 2, 2}));  // expects [[2], [1], [1,2,2], [2,2], [1,2], []]
        log(subsetsWithDup2(new int[]{2, 1, 2}));  // expects [[2], [1], [1,2,2], [2,2], [1,2], []]
        log(subsetsWithDup2(new int[]{1, 2, 3}));  // expects [[3], [1], [2], [1,2,3], [1,3], [2,3], [1,2], []]
    }
}
