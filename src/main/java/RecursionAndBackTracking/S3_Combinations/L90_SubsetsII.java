package RecursionAndBackTracking.S3_Combinations;

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
     * - 思路：该题与 L78_Subsets 解法1类似，区别在于该解法中加上了对重复分支的判断（∵ 重复分支会产生重复解）。
     *   例如对于 [2,1,2] 来说，排序后为 [1,2,2]：
     *                  []
     *            1/    2|    2\
     *          [1]     [2]     ×
     *        2/  2\    2|
     *      [1,2]   ×  [2,2]
     *       2|
     *     [1,2,2]
     *
     * - 实现：对重复分支的判断逻辑与 L40_CombinationSumII 一致。
     * - 时间复杂度 O(2^n)：分析方法与 L78_Subsets 解法1一样，但 ∵ 该题中会跳过某些路径 ∴ 复杂度 < O(2^n)。
     * - 空间复杂度 O(n)。
     * */
    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        Arrays.sort(nums);      // ∵ 后面对同层级的重复分支的检查是用 nums[j] 与 nums[j-1] 比较 ∴ 这里要先对 nums 排序
        backtrack(nums, 0, new ArrayList<>(), res);
        return res;
    }

    private static void backtrack(int[] nums, int i, List<Integer> list, List<List<Integer>> res) {
        res.add(new ArrayList<>(list));          // 收集所有中间结果和最终结果
        for (int j = i; j < nums.length; j++) {
            if (j > i && nums[j] == nums[j-1]) continue;  // 注意是 j>i 而不能是 j>0 ∵ 要跳过的是同一层级中 i 之后的相同分支
            list.add(nums[j]);                            // ，若写成 j>0 则会丢掉 [1,2,2]、[2,2] 两个解
            backtrack(nums, j + 1, list, res);
            list.remove(list.size() - 1);
        }
    }

    /*
     * 解法2：Recursion + Backtracking
     * - 思路：类似 L78_Subsets 解法2，但加入了对重复元素的检测。例如对于 [2,1,2] 来说，排序后为 [1,2,2]。放入空集解 [] 之后：
     *   1. 若用1，则将1追加到 [] 中，得到解 [1]；若不用1，则还是 []；
     *      将两个解合并得到 [[],[1]]。
     *   2. 若用2，则将2追加到 [[],[1]] 中的每个里，得到解 [[2],[1,2]]；若不用2，则还是 [[],[1]]；
     *      将两部分合并得到 [[],[1],[2],[1,2]]。
     *   3. 若用2 ∵ 与上一个元素重复 ∴ 只将2追加到第2步中新得到的解 [2]、[1,2] 里，得到解 [[2,2],[1,2,2]]，
     *      将两部分合并得到 [[],[1],[2],[1,2],[2,2],[1,2,2]]。
     * - 时间复杂度 O(2^n)，空间复杂度 O(1)。
     * */
    public static List<List<Integer>> subsetsWithDup2(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        Arrays.sort(nums);
        res.add(new ArrayList<>());

        int size = 0, start;
        for (int i = 0; i < nums.length; i++) {
            start = (i > 0 && nums[i] == nums[i - 1]) ? size : 0;  // 若元素重复，则使用上轮遍历之前记录的 res.size 作为遍历起点；若不是重复元素，则正常从0开始
            size = res.size();                                     // 在遍历之前记录 res.size
            for (int j = start; j < size; j++) {                   // 遍历 res 中从索引 start 处开始的 subset
                List<Integer> list = new ArrayList<>(res.get(j));
                list.add(nums[i]);
                res.add(list);
            }
        }

        return res;
    }

    public static void main(String[] args) {
        log(subsetsWithDup2(new int[]{1, 2, 2}));  // expects [[], [1], [1,2], [1,2,2], [2], [2,2]]
        log(subsetsWithDup2(new int[]{2, 1, 2}));  // expects [[], [1], [1,2], [1,2,2], [2], [2,2]]
        log(subsetsWithDup2(new int[]{1, 2, 3}));  // expects [[], [1], [1,2], [1,2,3], [1,3], [2], [2,3], [3]]
    }
}
