package RecursionAndBackTracking.Combinations;

import static Utils.Helpers.*;

import java.util.List;

/*
 * Combination Sum II
 *
 * - Given a collection of candidate numbers (candidates) and a target number (target), find all unique
 *   combinations in candidates where the candidate numbers sums to target.
 *
 * - Notes:
 *   1. Each number in candidates may only be used once in the combination.
 *   2. All numbers (including target) will be positive integers.
 *   3. The solution set must not contain duplicate combinations.
 * */

public class L40_CombinationSumII {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        return null;
    }

    public static void main(String[] args) {
        log(combinationSum(new int[]{10, 1, 2, 7, 6, 1, 5}, 8));  // expects [[1,7], [1,2,5], [2,6], [1,1,6]]
        log(combinationSum(new int[]{2, 5, 2, 1, 2}, 5));         // expects [[1,2,2], [5]]
    }
}
