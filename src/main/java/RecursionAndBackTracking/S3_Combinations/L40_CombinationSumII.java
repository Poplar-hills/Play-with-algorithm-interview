package RecursionAndBackTracking.S3_Combinations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Combination Sum II
 *
 * - Given a collection of candidate numbers and a target number, find all unique combinations in the
 *   candidates where the candidates sums to the target. (注意不同于 L39，candidates 中的元素可能有重复)
 *
 * - Notes:
 *   1. Each number in candidates may only be used once in the combination.
 *   2. All numbers (including target) will be positive integers.
 *   3. The solution set must not contain duplicate combinations.
 * */

public class L40_CombinationSumII {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：将组合问题转化为树形问题，采用回溯搜索求解。
     * - 实现：L39_CombinationSum 中的限制是不能产生重复解，而该题中除了该限制之外还多了不能重复使用 candidate 的限制。因此：
     *   - 为了不重复使用 candidate，需在向下递归的过程中，让每层在遍历 candidates 时的起始位置+1；
     *   - 为了不产生重复解，还需对 candidates 排序，并在每次遍历时跳过相同的元素。
     *   例如，对于 cancdidates=[2,5,2,1,2], target=5 来说，排序后的 cancdidates=[1,2,2,2,5]，于是有：
     *                                5
     *                   1/     2/    2|    2\     5\    - 遍历 candidates[0..)
     *                   4       3     ×     ×      0    - 跳过重复的2
     *             2/ 2| 2| 5\  2|                       - 遍历 candidates[1..)
     *              2  ×  ×  ×   1
     *             2|        2/ 2| 5\
     *              0        ×   ×   ×
     * - 时间复杂度 << O(n^n)，空间复杂度 O(target)。
     * */
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (candidates == null || candidates.length == 0) return res;
        Arrays.sort(candidates);
        backtrack(candidates, target, 0, new ArrayList<>(), res);
        return res;
    }

    private static void backtrack(int[] candidates, int target, int i, List<Integer> list, List<List<Integer>> res) {
        if (target == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int j = i; j < candidates.length && target >= candidates[j]; j++) {
            int c = candidates[j];
            if (j > i && c == candidates[j - 1]) continue;     // 跳过可能产生重复解的元素
            list.add(c);
            backtrack(candidates, target - c, j + 1, list, res);  // 下一层递归从第 j+1 个元素开始遍历，以保证不使用已用过的元素
            list.remove(list.size() - 1);
        }
    }

    public static void main(String[] args) {
        log(combinationSum(new int[]{2, 5, 2, 1, 2}, 5));         // expects [[1,2,2], [5]]
        log(combinationSum(new int[]{10, 1, 2, 7, 6, 1, 5}, 8));  // expects [[1,7], [1,2,5], [2,6], [1,1,6]]
    }
}
