package RecursionAndBackTracking.Combinations;

import static Utils.Helpers.*;

import java.util.List;

/*
 * Subsets
 *
 * - Given a collection of integers that might contain duplicates, nums, return all possible subsets (the power set).
 *
 * - Note: The solution set must not contain duplicate subsets.
 * */

public class L90_SubsetsII {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        return null;
    }

    public static void main(String[] args) {
        log(subsetsWithDup(new int[]{1, 2, 2}));  // expects [[2], [1], [1,2,2], [2,2], [1,2], []]
    }
}
