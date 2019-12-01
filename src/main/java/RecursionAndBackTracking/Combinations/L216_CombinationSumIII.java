package RecursionAndBackTracking.Combinations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Combination Sum III
 *
 * - Find all possible combinations of k numbers that add up to a number n, given that only numbers from 1 to
 *   9 can be used and each combination should be a unique set of numbers. (从1-9中选出 k 个数字，使他们的和为 n)
 *
 * - Notes:
 *   1. All numbers will be positive integers.
 *   2. The solution set must not contain duplicate combinations.
 * */

public class L216_CombinationSumIII {
    /*
     * 解法1：Recursion + Backtracking
     * - 时间复杂度 << O(n^9)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> combinationSum(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        if (k <= 0 || n <= 0) return res;
        helper(k, n, 1, new ArrayList<>(), res);
        return res;
    }

    private static void helper(int k, int n, int i, List<Integer> list, List<List<Integer>> res) {
        if (n == 0 && k == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int j = i; j <= 9 && j <= n; j++) {
            list.add(j);
            helper(k - 1, n - j, j + 1, list, res);
            list.remove(list.size() - 1);
        }
    }

    public static void main(String[] args) {
        log(combinationSum(3, 7));   // expects [[1,2,4]]
        log(combinationSum(3, 9));   // expects [[1,2,6], [1,3,5], [2,3,4]]
        log(combinationSum(2, 18));  // expects []
    }
}
