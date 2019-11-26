package RecursionAndBackTracking.Combinations;

import static Utils.Helpers.*;

import java.util.List;

/*
 * Combination Sum III
 *
 * - Find all possible combinations of k numbers that add up to a number n, given that only numbers from 1 to
 *   9 can be used and each combination should be a unique set of numbers.
 *
 * - Notes:
 *   1. All numbers will be positive integers.
 *   2. The solution set must not contain duplicate combinations.
 * */

public class L216_CombinationSumIII {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<Integer>> combinationSum(int k, int n) {
        return null;
    }

    public static void main(String[] args) {
        log(combinationSum(3, 7));  // expects [[1,2,4]]
        log(combinationSum(3, 9));  // expects [[1,2,6], [1,3,5], [2,3,4]]
    }
}
