package RecursionAndBackTracking.Combinations;

import static Utils.Helpers.*;

import java.util.List;

/*
 * Combination Sum
 *
 * - Given a set of candidate numbers (candidates) (without duplicates) and a target number (target), find
 *   all unique combinations in candidates where the candidate numbers sums to target.
 *
 * - Notes:
 *   1. The same repeated number may be chosen from candidates unlimited number of times.
 *   2. All numbers (including target) will be positive integers.
 *   3. The solution set must not contain duplicate combinations.
 * */

public class L36_CombinationSum {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        return null;
    }

    public static void main(String[] args) {
        log(combinationSum(new int[]{2, 3, 6, 7}, 7));  // expects [[7], [2,2,3]]
        log(combinationSum(new int[]{2, 3, 5}, 8));     // expects [[2,2,2,2], [2,3,3], [3,5]]
    }
}
